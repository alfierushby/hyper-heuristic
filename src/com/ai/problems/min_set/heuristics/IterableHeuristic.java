package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

public abstract class IterableHeuristic extends Heuristic {
    int iterations;
    boolean skippable = true;

    public IterableHeuristic(MinSetProblem problem, Random rng) {
        super(problem, rng);
    }

    /**
     * The heuristic single iteration to be executed.
     * @param save_index Solution index to apply to
     */
    public abstract void applyHeuristicSingle(int save_index);

    /**
     * This applies a heuristic 'iterations' times.
     * @param sol_index Solution index to apply to
     * @param save_index Solution where it saves
     */
    public void applyHeuristic(int sol_index, int save_index){
        int prev;
        getProblem().copySolution(sol_index,save_index);
        for(int i = 0; i< iterations; i++){
            prev = getProblem().getObjectiveValue(save_index);
            applyHeuristicSingle(save_index);
            if(prev>=getProblem().getObjectiveValue(save_index)&&skippable)
                return;
        }
    };

}
