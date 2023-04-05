package com.ai.problems.min_set;

/**
 * A solution class for min set. Contains its objective and
 */
public class Solution implements Cloneable {
    private int objective_value, unaccounted_elements;
    private boolean[] solution;


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

    public boolean[] getSolutionData() {
        return solution;
    }

    public Solution(int size){
        solution = new boolean[size];
    }

    @Override
    public Solution clone() {
        try {
            Solution clone = (Solution) super.clone();
            clone.solution = solution.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
