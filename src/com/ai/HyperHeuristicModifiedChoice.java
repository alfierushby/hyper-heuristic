package com.ai;

import com.ai.problems.min_set.heuristics.CrossoverHeuristic;
import com.ai.problems.min_set.heuristics.Heuristic;
import com.ai.problems.min_set.records.HeuristicData;
import com.ai.problems.min_set.records.SolutionObjective;
import com.ai.problems.min_set.records.HeuristicPair;
import com.ai.problems.min_set.records.TimeObjectiveValue;

import java.sql.Time;
import java.util.*;

import static com.ai.HeuristicClasses.*;

/**
 * Hyper Heuristic that implements the Modified Choice Function, includes Crossover
 * from a list of the top 5 solutions as its secondary parent solution.
 */
public class HyperHeuristicModifiedChoice implements HyperHeuristic{
    int max_number_of=100;
    Random rng;
    int best_objective_value, current_objective_value, iteration=0;
    final int offset = 5, max_best_heuristics = 5;
    public final int CURRENT_SOLUTION_INDEX = 0;
    public final int BACKUP_SOLUTION_INDEX = 1;
    public final double TIME_WEIGHT = 1;
    Problem problem_domain;
    HeuristicData prev_heuristic;
    ArrayList<Double> phis= new ArrayList<>();
    /**
     * Each entry represents a heuristic ID, and for each ID is a list of executions done for that specific heuristic.
     * To be used in the f1 function.
     */
    ArrayList<ArrayList<TimeObjectiveValue>> heuristic_single_data = new ArrayList<>();

    /**
     * This maps a size 2 pair of hk to hj to a TimeObjectiveValue pair that determines the change in objective value
     * from hk to hj, and the time taken to execute hj.
     * In Array form to represent that this can occur on multiple occasions.
     */
    Map<HeuristicPair, ArrayList<TimeObjectiveValue>> follow_heuristic_times = new HashMap<>();


    /**
     * The absolute times on each case a heuristic was run. Updated on each time a heuristic is run.
     * Defaults to 0.
     */
    long[] times;

    ArrayList<Heuristic> heuristics = new ArrayList<>();
    ArrayList<SolutionObjective> best_solutions = new ArrayList<>();

    public ArrayList<ArrayList<TimeObjectiveValue>> getHeuristicSingleData() {
        return heuristic_single_data;
    }

    public Random getRng() {
        return rng;
    }

    public void setRng(Random rng) {
        this.rng = rng;
    }

    public Map<HeuristicPair, ArrayList<TimeObjectiveValue>> getFollowHeuristicTimes() {
        return follow_heuristic_times;
    }

    public int getBestObjectiveValue() {
        return best_objective_value;
    }

    public void setBestObjectiveValue(int best_objective_value) {
        this.best_objective_value = best_objective_value;
    }

    public int getCurrentObjectiveValue() {
        return current_objective_value;
    }

    public void setCurrentObjectiveValue(int current_objective_value) {
        this.current_objective_value = current_objective_value;
    }

    public Problem getProblemDomain() {
        return problem_domain;
    }

    public void setProblemDomain(Problem problem_domain) {
        this.problem_domain = problem_domain;
    }

    public HeuristicData getPrevHeuristic() {
        return prev_heuristic;
    }

    public void setPrevHeuristic(HeuristicData prev_heuristic) {
        this.prev_heuristic = prev_heuristic;
    }

    public ArrayList<Heuristic> getHeuristics() {
        return heuristics;
    }

    public void resetHeuristicList(){
        heuristics = new ArrayList<>();
    }

    public Heuristic getHeuristic(int id){
        if(id>heuristics.size()){
            System.out.println("Accessing an invalid heuristic id!");
            return null;
        }
        return heuristics.get(id);
    }

    public ArrayList<Double> getPhis() {
        return phis;
    }

    public double getDelta(int heuristicId) {
        return 1 - getPhis().get(heuristicId);
    }

    public int getIteration() {
        return iteration;
    }

    public void incrementIteration() {
        this.iteration++;
    }

    /**
     * @return Times in milliseconds for heuristic executions.
     */
    public long[] getTimes() {
        return times;
    }

    public void setTimes(long[] times) {
        this.times = times;
    }

    public ArrayList<SolutionObjective> getBestSolutions() {
        return best_solutions;
    }

    public int getMaxBestHeuristics() {
        return max_best_heuristics;
    }

    public HyperHeuristicModifiedChoice(Random rng) {
        setRng(rng);
    }
    private void setupHeuristicList(){
        ArrayList<Heuristic> list_heuristics = getHeuristics();
        list_heuristics.addAll(List.of(getProblemDomain().getHeuristics(Mutational)));
        list_heuristics.addAll(List.of(getProblemDomain().getHeuristics(Hill_Climbing)));
        list_heuristics.addAll(List.of(getProblemDomain().getHeuristics(Ruin_and_Recreate)));
        list_heuristics.addAll(List.of(getProblemDomain().getHeuristics(Crossover)));

        // Setup array lists for the single time data, used by function 1.
        for(int id = 0; id < list_heuristics.size(); id++){
            getHeuristicSingleData().add(id,new ArrayList<>());
        }

        // Sets the array of times, defaulting to 0.
        long time = System.currentTimeMillis();
        setTimes(new long[list_heuristics.size()]);
        Arrays.fill(getTimes(), time);
    }


    int crossoverCalled = 0;
    /**
     * Compares the input solution with the best of X solutions, adding if it is better
     * or if there is an available space.
     * @param solution_id Id of heuristic to maybe add to the best of X heuristics.
     */
    private int modifyBestSolutionList(int solution_id){
        // Create data for use. Uses another specific structure. I wonder if generics exist? I wouldn't know.

        ArrayList<SolutionObjective> solutions = getBestSolutions();
        int len = solutions.size();

        if(len < getMaxBestHeuristics()){
            int objective_value = getProblemDomain().getObjectiveValue(solution_id);
            // Data is unordered as it is randomly picked from.
            // Copy data to the offset data location that is expected to not change.
            getProblemDomain().copySolution(solution_id,len+offset);
            // Add the record of this best solution.
            getBestSolutions().add(new SolutionObjective(len+offset,objective_value));
            return len;
        }
        Optional<SolutionObjective> worst = solutions.stream().max(Comparator.comparingInt(SolutionObjective::objectiveValue));
        if(worst.isPresent()){
            int i = solutions.indexOf(worst.get());
            // Replace solution in memory.
            getProblemDomain().copySolution(solution_id,worst.get().solutionId());
            // Replace data with newComparator.comparingInt objective value.
            solutions.set(i,new SolutionObjective(worst.get().solutionId(), getProblemDomain().getObjectiveValue(solution_id)));
            return i;
        } else{
            System.out.println("Fatal error detected in modifying best solution list!");
            return -1;
        }
    }

    /**
     * <b>Special case</b>, if there are no best solutions it will initialise a new solution and set it to offset, the
     * first index in solution memory.
     * @return A random solution Id from the best solutions.
     */
    private int getRandomBestSolution(){
        ArrayList<SolutionObjective> solutions = getBestSolutions();
        int len = solutions.size();
        crossoverCalled++;
        if(len==0 || Math.floorMod(crossoverCalled,getMaxBestHeuristics()) == 0){
            System.out.println("Uh what!");
            getProblemDomain().initialiseSolution(BACKUP_SOLUTION_INDEX,BACKUP_SOLUTION_INDEX);
            // Add it to the set of solutions.
            return  solutions.get(modifyBestSolutionList(BACKUP_SOLUTION_INDEX)).solutionId();
        }
        // Single point methods work on saving to current solution at end of iteration.
        int ran = getRng().nextInt(len);
        return solutions.get(ran).solutionId();
    }

    /**
     * Performs a general computation that exists in f1 and f2 functions.
     * @param time_data array of time data to perform the general computation for f1 & f2.
     * @return
     */
    private double generalComputationFunction(ArrayList<TimeObjectiveValue> time_data){
        double phi_val = getPhis().get(getIteration());

        // first time_data items are older, thus have a lower weight placed on them from phi.
        int len = time_data.size();
        double total = 0;
        // It is important to note that the iterable loop will go from the first element, ie the oldest,
        // to the newest.

        // It is therefore important that the first item has the highest power, as Phi will always be < 1, thus higher
        // powers entails a smaller weight.
        // Thus, assuming for a size 5 array the powers are 0,1,2,3,4, reversing it will be 4,3,2,1,0. Ie, n = len-1.
        int n = len-1;

        // So, the oldest elements have the largest power.
        for (TimeObjectiveValue data : time_data){
            double phi_weighted = Math.pow(phi_val,n);
            double time =  TIME_WEIGHT* data.Time(); // Convert to seconds.
            double obj = data.ObjectiveValue();
            if(obj < 0)
                obj=0;
            if(time == 0)
                time = 1;
            total += phi_weighted * (obj/time);
            n--;
        }
        return total;
    }
    /**
     * This is the first function, f1, in the modified choice function.
     * @param heuristicId The heuristic to apply the function to
     * @return The value of the function
     */
    double function1(int heuristicId){
        ArrayList<TimeObjectiveValue> time_data = getHeuristicSingleData().get(heuristicId);
        if(time_data != null)
            return generalComputationFunction(time_data);
        return 0;
    }

    /**
     * This gets all scenarios where heuristic j was executed following heuristic k. This data is stored in
     * follow_heuristic_times, where each mapping contains an array of HeuristicData
     * @param heuristicK The previous heuristic k before executing heuristic j
     * @param heuristicJ The executed heuristic j.
     * @return
     */
    double function2(int heuristicK, int heuristicJ){
        HeuristicPair index = new HeuristicPair(heuristicK,heuristicJ);
        ArrayList<TimeObjectiveValue> time_data = getFollowHeuristicTimes().get(index);
        if(time_data != null)
            return generalComputationFunction(time_data);
        return 0;
    }

    /**
     * @param heuristicId Id to perform function 3 on.
     * @return time, in seconds, on how long it has been since the heuristic was last executed.
     */
    double function3(int heuristicId){
        // Time is in milliseconds, so return in seconds.
        return (double)  TIME_WEIGHT* (System.currentTimeMillis() - getTimes()[heuristicId]);
    }

    /**
     * Applies Modified Choice Function with All Move Acceptance. Do note that as it is all move acceptance, there will
     * be no usage of a secondary memory location, as it is possible to remove an unneeded copy from backup to current.
     * @param problem_domain The problem domain to apply the hyper heuristic to.
     */
    @Override
    public void applyHyperHeuristic(Problem problem_domain) {
        setProblemDomain(problem_domain);

        // Setup list of heuristics
        setupHeuristicList();

        //Intialise solution
        getProblemDomain().initialiseSolution(CURRENT_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);

        // Add default phi and delta values;
        getPhis().add(0.99);

        long new_time = System.currentTimeMillis()+ 20*1000;
        int len = getHeuristics().size();
        setBestObjectiveValue(getProblemDomain().getObjectiveValue(CURRENT_SOLUTION_INDEX));

        while(System.currentTimeMillis() < new_time){
            double phi = getPhis().get(getIteration());
            double delta = getDelta(getIteration());

            int best_index = 0;
            double best_score = 0;

            // Calculate modified change function for each heuristic.
            for(int id = 0; id < len; id++){
                double f1 = phi *  function1(id);
                double f2 = 0;
                if(prev_heuristic != null)
                    f2 = phi * function2(prev_heuristic.heuristicID(),id);
                double f3 = delta * function3(id);
                // Decide if this heuristic should be selected.
                double score = f1 + f2 + f3;
                boolean choose = false;

                System.out.println(" heuristic ID " + id + " with a score of " + score);

                // Decide to choose
                if(score > best_score){
                    choose = true;
                }

                if(choose){
                    best_index = id;
                    best_score = score;
                }
            }

            System.out.println("Picked heuristic ID " + best_index + " with a score of " + best_score);
            int prev_objective_value = getProblemDomain().getObjectiveValue(CURRENT_SOLUTION_INDEX);
            // Apply heuristic. //

            Heuristic heuristic = getHeuristic(best_index);
            long before_run = System.currentTimeMillis();
            if(heuristic.getHeuristicClass() == Crossover){
                CrossoverHeuristic cross_heuristic = (CrossoverHeuristic) heuristic;
                int parent2 = getRandomBestSolution();
                System.out.println("Doing crossover");
                System.out.println("Chose random solution " + parent2);
                cross_heuristic.applyHeuristic(CURRENT_SOLUTION_INDEX,parent2,CURRENT_SOLUTION_INDEX);
            } else {
                heuristic.applyHeuristic(CURRENT_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
            }
            long change_time =  System.currentTimeMillis() - before_run;


            // Update current objective value.
            setCurrentObjectiveValue(getProblemDomain().getObjectiveValue(CURRENT_SOLUTION_INDEX));

            // Add data for next iteration //

            // Add data for best solution
            modifyBestSolutionList(CURRENT_SOLUTION_INDEX);

            // Add data for function 1
            // Change in quality. Positive indicates an improvement (from a higher to lower objective value).
            int  change_quality = prev_objective_value- getCurrentObjectiveValue();

                System.out.println("Score changed from " +  prev_objective_value + " to " +
                        getProblemDomain().getObjectiveValue(CURRENT_SOLUTION_INDEX) + " with a change of " + change_quality + " " + change_time);

            // Multiply by 1 or -1 depending on whether the problem domain is a minimization or maximisation problem.
            // 1 entails it is a minimization problem.
            change_quality *= getProblemDomain().getObjectiveValueGoal();
            getHeuristicSingleData().get(best_index).add(new TimeObjectiveValue(change_time,change_quality));

            // Add data for function 2
            if(getPrevHeuristic() != null){
                HeuristicPair key = new HeuristicPair(getPrevHeuristic().heuristicID(),best_index);

                // Make sure it contains an arraylist for the specified key.
                if(!getFollowHeuristicTimes().containsKey(key))
                    getFollowHeuristicTimes().put(key,new ArrayList<>());

                ArrayList<TimeObjectiveValue> time_data = getFollowHeuristicTimes().get(key);

                // Change in objective value, positive entailing the current solution has a lower objective value, thus
                // better. Defaulting to minimisation.
                int change_relative_quality = getPrevHeuristic().data().ObjectiveValue()
                        - getProblemDomain().getObjectiveValue(CURRENT_SOLUTION_INDEX);

                // Modifies to maximisation if required.
                change_relative_quality *= getProblemDomain().getObjectiveValueGoal();

                System.out.println("Previous run heuristic id " + prev_heuristic.heuristicID() + " change in objective" +
                        "value is " + change_relative_quality);

                // Add data
                time_data.add(new TimeObjectiveValue(change_time,change_relative_quality));
            }

            // Set time for function 3.
            // Set time in milliseconds for accuracy.
            getTimes()[best_index] = before_run;

            // Update Phi //
            boolean improved = change_quality > 0;

            System.out.println("Solution improved " + improved);

            if(improved)
                getPhis().add(0.99);
            else
                getPhis().add(Math.max( getPhis().get(getIteration())-0.01,0.01));

            // Set previous heuristic //
            // Sets data to the objective value of new solution and the time it was run (won't be used).
            setPrevHeuristic(new HeuristicData(best_index,new TimeObjectiveValue(before_run,getCurrentObjectiveValue())));

            // Update best objective value.
            if(getCurrentObjectiveValue() < getBestObjectiveValue())
                setBestObjectiveValue(getCurrentObjectiveValue());

            // Increment iteration
            incrementIteration();
        }

        System.out.println("Best objective value " + getBestObjectiveValue());
        // check

    }
}
