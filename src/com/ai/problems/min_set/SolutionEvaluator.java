package com.ai.problems.min_set;

import static com.ai.problems.min_set.enums.instance_reader.NumSubsets;

public class SolutionEvaluator {

    // Values for determining objective value.
    private int objective_value, unaccounted_elements;
    private final MinSetProblem problem;

    /**
     * @return objective value of solution
     */
    public int getObjectiveValue() {
        return objective_value;
    }

    /**
     * @return number of unaccounted elements for an infeasible solution
     */
    public int getUnaccountedElements() {
        return unaccounted_elements;
    }

    public void setObjectiveValue(int objective_value) {
        this.objective_value = objective_value;
    }

    public void incrementObjectiveValue(int val) {
        setObjectiveValue(getObjectiveValue()+val);
    }

    public void setUnaccountedElements(int unaccounted_elements) {
        this.unaccounted_elements = unaccounted_elements;
    }

    public SolutionEvaluator(MinSetProblem problem) {
        this.problem = problem;
    }

    ////////////////////////////////
    // OBJECTIVE VALUE FUNCTIONS //
    //////////////////////////////

    /**
     * Assumes specified input index has been flipped, and calculates new objective value via delta evaluation.
     * @param index Index of solution bit that has been flipped.
     */
    public void deltaObjectiveEvaluation(int index){
        int bit = problem.getSolution()[index];
        int[] subset = problem.getSubsets().get(index);
        int sum;

        if(bit==1){
            // Add to solution map.
            problem.insertNode(subset, problem.getSolutionMap());
            sum=1;
        }else {
            // Bit is 0 so remove it from solution map (assuming it was flipped from 1)
            problem.removeNode(subset, problem.getSolutionMap());
            sum=-1;
        }

        int prev_unaccount = getUnaccountedElements();
        int new_unaccount = feasibleSolution();

        if(new_unaccount!=0&& prev_unaccount==0){
            // Make sure it was a previous feasible solution before adding extra cost of infeasible solution.
            sum+= problem.getData().get(NumSubsets) + new_unaccount;
        } else if (new_unaccount==0&&prev_unaccount!=0) {
            // If going from unfeasible to feasible, take away constant cost, and the previous number of unaccounted
            // elements, to get the proper objective value.
            sum-= problem.getData().get(NumSubsets) + prev_unaccount;
        }else {
            // If going from unfeasible to unfeasible, get difference.
            sum+=  new_unaccount - prev_unaccount;
        }
        // Perform delta evaluation.
        incrementObjectiveValue(sum);

    }

    /**
     * Determines if a solution is valid. Allows infeasible solutions.
     * Counts number of subsets used. If infeasible, selects max number,
     * of subsets and then adds how many elements were missed, along with the subset total for delta evaluation, to
     * allow scaling of infeasible solutions.
     * <p>
     *     <b>DO NOT USE THIS WHEN YOU CAN USE DELTA EVALUATION!</b>
     * </p>
     */
    public void setObjectiveValue(){
        int subsetTotal=0;
        for(int subset : problem.getSolution()){
            if(subset==1)
                subsetTotal++;
        }

        if(feasibleSolution()==0){
            setObjectiveValue( subsetTotal );
        } else {
            // Include subsetTotal and unaccounted elements, thus shows improvement in two important areas.
            // Always worse than the worse feasible solution.
            setObjectiveValue(  problem.getData().get(NumSubsets) + subsetTotal + getUnaccountedElements() );
        }
    }

    /**
     * Decides if a solution is feasible or not. O(n).
     * @return number of elements not connected
     */
    private int feasibleSolution(){
        setUnaccountedElements(0);
        for(int node : problem.getSolutionMap()){
            if(node==0)
                setUnaccountedElements(getUnaccountedElements()+1);
        }
        return getUnaccountedElements();
    }

}
