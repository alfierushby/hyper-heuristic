package com.ai.problems.min_set;

import javax.swing.*;
import java.util.Arrays;
import java.util.function.IntBinaryOperator;

import static com.ai.problems.min_set.enums.InstanceReader.NumSubsets;

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
     * @return objective value of solution
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
        getSolution().setObjectiveValue(objective_value);
    }

    public void incrementObjectiveValue(int val) {
        setObjectiveValue(getObjectiveValue()+val);
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
     * solution map. Only O(n) cost is insertion and removal to the solution map.
     * @param index Index of solution bit that has been flipped.
     */
    public void deltaObjectiveEvaluation(int index){
        boolean bit = getSolution().getSolutionData()[index];
        int[] subset = getSolution().getSolutionMap();
        int sum;

        if(bit){
            // Add to solution map.
            sum=1;
        }else {
            // Bit is 0 so remove it from solution map.
            sum=-1;
        }

        int new_unaccount = getUnaccountedElements();

        if(new_unaccount!=0&& getPrevUnaccount()==0){
            // If going from feasible to unfeasible, add extra cost of infeasible solution.
            sum+= getSolution().getSolutionData().length + new_unaccount;
        } else if (new_unaccount==0&&getPrevUnaccount()!=0) {
            // If going from infeasible to feasible, take away constant cost, and the previous number of unaccounted
            // elements, to get the proper objective value.
            sum-= getSolution().getSolutionData().length + getPrevUnaccount();
        }else {
            // If going from infeasible to unfeasible, get difference in infeasible elements.
            sum+=  new_unaccount - getPrevUnaccount();
        }
        // Perform delta evaluation.
        incrementObjectiveValue(sum);
        setPrevUnaccount(new_unaccount);

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

        if(feasibleSolution()==0){
            setObjectiveValue( subsetTotal );
        } else {
            // Include subsetTotal and unaccounted elements, thus shows improvement in two important areas.
            // Always worse than the worse feasible solution.
            setObjectiveValue(  getSolution().getSolutionData().length + subsetTotal + getUnaccountedElements() );
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
        setPrevUnaccount(getUnaccountedElements());
        return getUnaccountedElements();
    }

}
