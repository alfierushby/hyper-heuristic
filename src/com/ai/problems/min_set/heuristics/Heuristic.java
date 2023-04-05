package com.ai.problems.min_set.heuristics;

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

    public void setProblem(MinSetProblem problem) {
        this.problem = problem;
    }

    public Random getRng() {
        return rng;
    }

    public Heuristic(MinSetProblem problem, Random rng) {
        this.problem = problem;
        this.rng = rng;
    }

    /**
     * @param sol Solution index to apply to
     * @param save Solution where it saves
     */
    public abstract void applyHeuristic(int sol, int save);
}
