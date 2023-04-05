package com.ai.problems.min_set;

import java.util.function.IntBinaryOperator;

import static com.ai.problems.min_set.enums.instance_reader.NumSubsets;

/**
 * Used for evaluating solutions, contains the brunt of the functions that has the purpose of
 * calculating the objective value of the problem.
 */
public class SolutionEvaluator {

    // Values for determining objective value.
    private int objective_value, unaccounted_elements;
    private final MinSetProblem problem;
    private final int[] solution, solution_map;

    public int[] getSolution() {
        return solution;
    }

    public int[] getSolutionMap() {
        return solution_map;
    }

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

    public SolutionEvaluator(MinSetProblem problem, int[] solution, int[] solution_map) {
        this.problem = problem;
        this.solution = solution;
        this.solution_map=solution_map;
    }

      ////////////////////////////
     // SOLUTION MAP FUNCTIONS //
    ////////////////////////////

    /**
     * Generalised function to be used by insert and removal of nodes for public use. Internal function.
     * @param left left array, loops through to do an operation to the right array
     * @param right left array acts upon right array
     * @param operator operation that is executed on each '1' element in the left array to the right array
     */
    private void operatorNode(int[] left, int[] right, IntBinaryOperator operator){
        int count =0;
        for(int i : left){
            if(i == 1){
                int prev = right[count];
                right[count] = operator.applyAsInt(right[count],1);
                // Evaluate unaccounted elements. Add 1 if node made from >0 to 0.
                if(prev>0&&right[count]==0){
                    setUnaccountedElements(getUnaccountedElements()+1);
                } else if(prev==0&&right[count]>0){
                    setUnaccountedElements(getUnaccountedElements()-1);
                }
            }
            count++;
        }
    }

    /**
     * Combines two arrays, returning result. Acts as an insertion of one subset to another, culminating edges.
     * Formally left + right.
     * @param left subset that will be inserted
     * @param right subset that will be inserted to
     */
    public void insertNode(int[] left, int[] right){
        operatorNode(left,right,  (a, b) -> a + b);
    }

    /**
     * Combines two arrays, returning result. Acts as a removal of one subset to another, removing edges.
     * formally right - left
     * @param toRemove subset that will be removed
     * @param source subset that will have contents of toRemove removed
     */
    public void removeNode(int[] toRemove, int[] source){
        operatorNode(toRemove,source,  (a, b) -> a - b);
    }


      ///////////////////////////////
     // OBJECTIVE VALUE FUNCTIONS //
    ///////////////////////////////

    /**
     * Assumes specified input index has been flipped, and calculates new objective value via delta evaluation.
     * Removes need to evaluate the solution's score. Infeasibility is evaluated naturally when inserting to the
     * solution map. Only O(n) cost is insertion and removal to the solution map.
     * @param index Index of solution bit that has been flipped.
     */
    public void deltaObjectiveEvaluation(int index){
        int bit = getSolution()[index];
        int[] subset = problem.getSubsets().get(index);
        int sum;


        int prev_unaccount = getUnaccountedElements();

        if(bit==1){
            // Add to solution map.
            insertNode(subset, getSolutionMap());
            sum=1;
        }else {
            // Bit is 0 so remove it from solution map.
            removeNode(subset, getSolutionMap());
            sum=-1;
        }

        int new_unaccount = getUnaccountedElements();

        if(new_unaccount!=0&& prev_unaccount==0){
            // If going from feasible to unfeasible, add extra cost of infeasible solution.
            sum+= problem.getData().get(NumSubsets) + new_unaccount;
        } else if (new_unaccount==0&&prev_unaccount!=0) {
            // If going from infeasible to feasible, take away constant cost, and the previous number of unaccounted
            // elements, to get the proper objective value.
            sum-= problem.getData().get(NumSubsets) + prev_unaccount;
        }else {
            // If going from infeasible to unfeasible, get difference in infeasible elements.
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
        for(int subset : getSolution()){
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
        for(int node : getSolutionMap()){
            if(node==0)
                setUnaccountedElements(getUnaccountedElements()+1);
        }
        return getUnaccountedElements();
    }

}
