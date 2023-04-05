package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

import static com.ai.problems.min_set.enums.instance_reader.NumElements;

public class bitMutation extends Heuristic {

    public bitMutation(MinSetProblem problem, Random rng) {
        super(problem,rng);
    }

    @Override
    public void applyHeuristic() {
        // select a random bit in the solution
        int bitIndex = getRng().nextInt(problem.getNumberOfElements());

        getProblem().getOperations().bitFlip(bitIndex);
    }
}
