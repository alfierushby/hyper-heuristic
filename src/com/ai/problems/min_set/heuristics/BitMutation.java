package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

public class BitMutation extends IterableHeuristic {

    public BitMutation(MinSetProblem problem, Random rng) {
        super(problem,rng);
        iterations = getProblem().getIntensityOfMutation();
        skippable= false;
    }

    @Override
    public void applyHeuristicSingle(int sol) {
        // select a random bit in the solution
        int bitIndex = getRng().nextInt(problem.getNumberOfSubsets());

        getProblem().getOperations().bitFlip(bitIndex,sol);
    }
}
