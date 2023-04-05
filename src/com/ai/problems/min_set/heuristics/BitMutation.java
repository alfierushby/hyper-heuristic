package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

public class BitMutation extends Heuristic {

    public BitMutation(MinSetProblem problem, Random rng) {
        super(problem,rng);
    }

    @Override
    public void applyHeuristic(int sol, int save) {
        // select a random bit in the solution
        int bitIndex = getRng().nextInt(problem.getNumberOfSubsets());

        getProblem().getOperations().bitFlip(bitIndex,sol,save);
    }
}
