package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

public abstract class CrossoverHeuristic extends Heuristic {


    public CrossoverHeuristic(MinSetProblem problem, Random rng) {
        super(problem, rng);
    }

    public abstract void applyHeuristic(int parent1Index, int parent2Index,
                               int childIndex);

}
