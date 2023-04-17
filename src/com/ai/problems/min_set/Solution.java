package com.ai.problems.min_set;

/**
 * A solution class for min set. Contains its objective and evaluator.
 */
public class Solution implements Cloneable {
    private int objective_value, unaccounted_elements, prev_unaccount=0;
    private boolean[] solution;
    private int[] solution_map;
    private SolutionEvaluator evaluator;

    public int getPrevUnaccount() {
        return prev_unaccount;
    }

    public void setPrevUnaccount(int prev_unaccount) {
        this.prev_unaccount = prev_unaccount;
    }

    public int getObjectiveValue() {
        return objective_value;
    }

    public void setObjectiveValue(int objective_value) {
        this.objective_value = objective_value;
    }

    public int getUnaccountedElements() {
        return unaccounted_elements;
    }

    public void setUnaccountedElements(int unaccounted_elements) {
        this.unaccounted_elements = unaccounted_elements;
    }

    /**
     * @return The solution representing an array of booleans, with each index corresponding to a subset being active
     * or not.
     */
    public boolean[] getSolutionData() {
        return solution;
    }

    /**
     * @return The map of edges from the subsets to the base elements.
     */
    public int[] getSolutionMap() { return solution_map; }

    public SolutionEvaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(SolutionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Solution(int size, int map_size){
        solution_map = new int[map_size];
        solution = new boolean[size];
        evaluator = new SolutionEvaluator(this);
    }

    public Solution(boolean[] solution, int[] map){
        this.solution_map = map;
        this.solution = solution;
        this.evaluator = new SolutionEvaluator(this);
        evaluator.setObjectiveValue();
    }

    @Override
    public Solution clone() {
        try {
            Solution clone = (Solution) super.clone();
            clone.solution = solution.clone();
            clone.solution_map = solution_map.clone();
            clone.setEvaluator(new SolutionEvaluator(clone));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
