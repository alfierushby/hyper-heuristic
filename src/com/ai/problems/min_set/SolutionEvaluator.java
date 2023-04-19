package com.ai.problems.min_set;

/**
 * Used for evaluating solutions, contains the brunt of the functions that has the purpose of
 * calculating the objective value of the problem.
 */
public class SolutionEvaluator {

    private final Solution solution;

    public Solution getSolution() {
        return solution;
    }

    public int[] getSolutionMap() {
        return getSolution().getSolutionMap();
    }

    /**
     * @return calculated objective value of solution
     */
    public int getObjectiveValue() {
        return getSolution().getObjectiveValue();
    }

    /**
     * @return number of unaccounted elements for an infeasible solution
     */
    public int getUnaccountedElements() {
        return getSolution().getUnaccountedElements();
    }

    public void setObjectiveValue(int objective_value) {
        getSolution().setPureObjectiveValue(objective_value);
    }

    public void incrementObjectiveValue(int val) {
        setObjectiveValue(getSolution().getPureObjectiveValue()+val);
    }

    public void setUnaccountedElements(int unaccounted_elements) {
        getSolution().setUnaccountedElements(unaccounted_elements);
    }

    public SolutionEvaluator(Solution solution) {
        this.solution = solution;
    }

    public int getPrevUnaccount() {
        return getSolution().getPrevUnaccount();
    }

    public void setPrevUnaccount(int prev_unaccount) {
        getSolution().setPrevUnaccount(prev_unaccount);
    }
    ///////////////////////////////
     // OBJECTIVE VALUE FUNCTIONS //
    ///////////////////////////////

    /**
     * Assumes specified input index has been flipped, and calculates new objective value via delta evaluation.
     * Removes need to evaluate the solution's score. Infeasibility is evaluated naturally when inserting to the
     * solution map. O(1)
     * @param index Index of solution bit that has been flipped.
     */
    public void deltaObjectiveEvaluation(int index){
        boolean bit = getSolution().getSolutionData()[index];
        int sum;

        if(bit){
            // Add to solution map.
            sum=1;
        }else {
            // Bit is 0 so remove it from solution map.
            sum=-1;
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
        for(boolean subset : getSolution().getSolutionData()){
            if(subset)
                subsetTotal++;
        }

        setObjectiveValue( subsetTotal );
        feasibleSolution();
    }

    /**
     * Sets number of unaccounted elements. Shouldn't be used outside initialisation. O(n).
     */
    private void feasibleSolution(){
        setUnaccountedElements(0);
        for(int node : getSolutionMap()){
            if(node==0)
                setUnaccountedElements(getUnaccountedElements()+1);
        }
        setPrevUnaccount(getUnaccountedElements());
    }

}
