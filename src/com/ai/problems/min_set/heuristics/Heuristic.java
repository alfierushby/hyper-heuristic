package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

/**
 * General Min-Set Problem abstract Heuristic.
 */
public abstract class Heuristic {
    MinSetProblem problem;
    Random rng;
    int iterations;
    boolean skippable = true;

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
     * The heuristic single iteration to be executed.
     * @param save Solution index to apply to
     */
    public abstract void applyHeuristicSingle(int save);

    /**
     * This applies a heuristic X times
     * @param sol Solution index to apply to
     * @param save Solution where it saves
     */
    public void applyHeuristic(int sol, int save){
        int prev;
        getProblem().copySolution(sol,save);
        for(int i = 0; i< iterations; i++){
            prev = getProblem().getObjectiveValue(save);
            applyHeuristicSingle(save);
            if(prev>=getProblem().getObjectiveValue(save)&&skippable)
                return;
        }
    };
}
