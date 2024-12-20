package com.ai.problems.min_set;

import com.ai.HeuristicClasses;
import com.ai.Problem;
import com.ai.problems.min_set.enums.InstanceReader;
import com.ai.problems.min_set.heuristics.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.ai.Config.*;
import static com.ai.HeuristicClasses.*;
import static com.ai.problems.min_set.enums.InstanceReader.*;


public class MinSetProblem implements Problem {

    // Binary encoding of a solution, each index specifying the subset in subsets, 1 being selected, 0 not.
    private final Map <Integer,Solution> solutions = new HashMap<>();
    private final Random rng;
    // List of subsets that map edges.
    private final ArrayList< int[] > subsets = new ArrayList<>();
    // General data of loaded instance.
    // Note that the assumption is that every unique instance counts *up to* the
    // number of elements, ie numbers 1..X inclusive.
    private final Map<Enum<InstanceReader>,Integer> data = new HashMap<>();
    private Operations operations;

    private final Map<Enum<HeuristicClasses>, Heuristic[]> heuristics = new HashMap<>();

    private String instance_name;
    Solution best_solution;

    public void setInstanceName(String instance_name) {
        this.instance_name = instance_name;
    }

    public Operations getOperations() {
        return operations;
    }

    public void setOperations(Operations operations) {
        this.operations = operations;
    }

    /**
     * @param sol_index specifies what solution you want to evaluate
     * @return a solution evaluator for the specific solution index
     */
    public SolutionEvaluator getEvaluator(int sol_index) {
        return solutions.get(sol_index).getEvaluator();
    }

    /**
     * @return number of connected edges per node in array form
     */
    public int[] getSolutionMap(int index) {
        return getSolution(index).getSolutionMap();
    }

    /**
     * @return Solution object that houses the solution map (edge data), solution data, and evaluator/objective values.
     */
    public Solution getSolution(int index) {
        return solutions.get(index);
    }

    /**
     * @return subsets of problem
     */
    public ArrayList<int[]> getSubsets() {
        return subsets;
    }

    /**
     * @return metadata of problem
     */
    public Map<Enum<InstanceReader>, Integer> getData() {
        return data;
    }

    public int getNumberOfSubsets(){
        return getData().get(NumSubsets);
    }

    public Random getRng() {
        return rng;
    }


    /**
     * This gets the objective value of a solution. Note that it does not update the objective value.
     * @param sol_index Solution index to get objective value
     * @return objective value
     */
    public int getObjectiveValue(int sol_index){
        return getEvaluator(sol_index).getObjectiveValue();
    }
    @Override
    public Heuristic[] getHeuristics(HeuristicClasses h_class) {
        return heuristics.get(h_class);
    }

    private int caseConvertion(double val){
        if(val<0.2)
            return 1;
        else if (val<0.4)
            return 2;
        else if (val<0.6)
            return 3;
        else if (val<0.8)
            return 4;
        else if (val<1)
            return 5;
        else
            return 6;
    }

    /**
     * Converts depth of search value to number of iterations.
     * @return the corresponding iteration count to be done in hill climbing methods.
     */
    public int getDepthOfSearch() {
        return caseConvertion(DEPTH_OF_SEARCH);
    }

    /**
     * Converts depth of search value to number of mutations.
     * @return the corresponding iteration count to be done in mutation methods.
     */
    public int getIntensityOfMutation() {
        return caseConvertion(INTENSITY_OF_MUTATION);
    }

    /**
     * @param rng Random element
     */
    public MinSetProblem(Random rng) {
        this.rng = rng;
        setOperations(new Operations(this));

    }

      //////////////////////////////
     // INITIALISATION FUNCTIONS //
    //////////////////////////////


    public void printInfo(int index){
        System.out.println( "size " + solutions.get(index).getSolutionData().length + " " + Arrays.toString(solutions.get(index).getSolutionData()));
        System.out.println( "size " + getSolution(index).getSolutionMap().length + " " + Arrays.toString(getSolution(index).getSolutionMap()));
        System.out.println( "Evaluation : " + getObjectiveValue(index) + " Infeasibility : " + getEvaluator(index).getUnaccountedElements());
    }

    /**
     * This either replaces a solution in memory, or adds it if no memory exists.
     * Avoids adding when not needed. Use when you don't want unexpected shifting of memory.
     * @param solution Solution to set.
     * @param sol_index Index it will be set, no shifting.
     */
    public void setSolution(Solution solution, int sol_index){
        // Set solution to arraylist memory
        solutions.put(sol_index,solution);
    }

    public void copySolution(int from, int to){
        Solution new_sol = getSolution(from).clone();
        solutions.put(to,new_sol);
    }

    @Override
    public int getObjectiveValueGoal() {
        // Minimisation problem.
        return 1;
    }

    @Override
    public String getInstanceName() {
        return instance_name;
    }

    @Override
    public void setBestSolution(int sol_index) {
        best_solution = getSolution(sol_index);
    }

    /**
     * Note this is set by a hyper heuristic!
     * @return the best solution created in the problem domain
     */
    public Solution getBestSolution() {
        return best_solution;
    }

    /**
     * Initialises the solution randomly.<br>
     * <b>Must be done before using the solution index.</b>
     */
    public void initialiseSolution(int current_solution_index,int backup_solution_index){
        int index = 0;
        // Initialise solution memory.
        Solution solution = new Solution(data.get(NumSubsets),data.get(NumElements));

        // Create current solution:
        setSolution(solution,current_solution_index);
        boolean[] solution_data = solution.getSolutionData();

        for (int[] subset : subsets){
            double ran = rng.nextDouble();
            if(ran < RANDOM_INTIALISATION){
                solution_data[index] = true;
                getOperations().insertNode(subset,solution);
            }
            index++;
        }

        // Initialise Objective Values
        getEvaluator(current_solution_index).setObjectiveValue();

        setSolution(solution.clone(),backup_solution_index);


        //System.out.println( "Evaluation : " + getEvaluator(current_solution_index).getObjectiveValue() + " Infeasibility : "
         //       + getEvaluator(current_solution_index).getUnaccountedElements());

        // Create backup solution:
    }


    /**
     * Inserts values into a subset arraylist specified by a string array.
     * @param index subset index in the subsets array
     * @param values values to be inserted into the subset
     * @return number of element edges inserted into subset
     */
    private int insertSubsetValues(int index, String[] values){
        // First get the arraylist to insert the values to.
        int[] subset;
        int inserted =0;
        if(index >= subsets.size()){
            // No index. Create.
            subset = new int[data.get(NumElements)];
            subsets.add(subset);
        } else {
            subset = subsets.get(index);
        }

        // Now loop through all the array values.
        for(String s : values){
            if (!Objects.equals(s, "")) {
                int i = Integer.parseInt(s);
                // System.out.println(i);
                subset[i-1] = 1;
                inserted++;
            }

        }
        // System.out.println( "size " + subset.size() + " " + subset.toString());
        // Return number of elements inserted.
        return inserted;
    }

    @Override
    public void loadInstance(String path, String file) {
        setInstanceName(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(path + "/" + file + ".txt"))) {
            String line = reader.readLine();
            // Stack to keep track of execution
            String[] arr;
            int current_subset_i=0, insert_limit=1;

            while(line != null){
                // Split into parts.
                arr = line.split(" ");

                // Do simple Parsing. First case is m n form.
                if( data.isEmpty()){
                    data.put(NumSubsets,Integer.parseInt(arr[1]));
                    data.put(NumElements,Integer.parseInt(arr[2]));
                }

                // Second case, check size 1 and NumSubsets and NumElements are filled.
                else if(data.size() == 2){
              //      System.out.println(arr[1]);
                    data.put(SizeOfSubset,Integer.valueOf(arr[1]));
                    insert_limit = data.get(SizeOfSubset);
                }

                // 3rd case, check if possible to insert data.
                else if(arr.length !=0 && data.size() == 3 && insert_limit > 0){
                    int num_inserted = insertSubsetValues(current_subset_i,arr);
                    insert_limit -= num_inserted; // Minus the items to insert.
                }

                // If we're done inserting subset, clear the data.
                if(insert_limit <=0){
                    data.remove(SizeOfSubset);
                    current_subset_i++;
                }

                if(current_subset_i>=data.get(NumSubsets))
                    break;

                // Read next line.
                line = reader.readLine();
            }

            // Create Heuristic arrays for the Problem Domain
            IterableHeuristic[] mutations = {new BitMutation(this,getRng()),
                                            new SwapBits(this,getRng()),
                                            new HighestEdgesMutation(this,getRng())};
            IterableHeuristic[] hill_climbing = {new DavisBitHC(this,getRng()),
                    new SteepestDescentHC(this,getRng()),
                    new FirstImprovementHC(this,getRng()),
                    new RandomMutationHC(this,getRng())};
            CrossoverHeuristic[] crossovers = {new UniformXO(this,getRng()),
                    new Uniform2Point(this,getRng()),
                    new Uniform1Point(this,getRng())};
            RuinRecreateHeuristic[] ruin_recreate = {new RuinRecreateHighest(this,getRng()),
                    new RuinRecreateLowest(this,getRng())};

            // Add to Mapping
            heuristics.put(Mutational,mutations);
            heuristics.put(Hill_Climbing,hill_climbing);
            heuristics.put(Crossover,crossovers);
            heuristics.put(Ruin_and_Recreate, ruin_recreate);

        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}

