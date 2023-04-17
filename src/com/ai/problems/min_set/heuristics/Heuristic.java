package com.ai.problems.min_set.heuristics;

import com.ai.HeuristicClasses;
import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

/**
 * General Min-Set Problem abstract Heuristic.
 */
public abstract class Heuristic {
    MinSetProblem problem;
    Random rng;

    public MinSetProblem getProblem() {
        return problem;
    }

    public Random getRng() {
        return rng;
    }

    /**
     * @return The class of heuristic
     */
    public abstract HeuristicClasses getHeuristicClass();

    public Heuristic(MinSetProblem problem, Random rng) {
        this.problem = problem;
        this.rng = rng;
    }

    /**
     * Makes specified bit false. Includes delta evaluation.
     * @param bit_index Bit to make false
     * @param sol_index Solution index to work on
     */
    public void bitFalse(int bit_index,int sol_index){
        if(getProblem().getSolution(sol_index).getSolutionData()[bit_index])
            getProblem().getOperations().bitFlip(bit_index,sol_index);
    }

    /**
     * This applies a heuristic, generic version.
     * @param sol_index Solution index to apply to
     * @param save_index Solution index where it saves
     */
    public abstract void applyHeuristic(int sol_index, int save_index);
}
