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
     * Determines if a solution is valid. Allows infeasible solutions.
     * Counts number of subsets used. If infeasible, selects max number
     * of subsets and then adds how many elements were missed, to allow scaling of infeasible solutions.
     * <p>
     *     <b>DO NOT USE THIS WHEN YOU CAN USE DELTA EVALUATION!</b>
     * </p>
     */
    public void setObjectiveValue(){
        if(feasibleSolution()==0){
            int subsetTotal=0;
            for(int subset : problem.getSolution()){
                if(subset==1)
                    subsetTotal++;
            }
            setObjectiveValue( subsetTotal );
        } else {
            setObjectiveValue( problem.getData().get(NumSubsets) + getUnaccountedElements() );
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
