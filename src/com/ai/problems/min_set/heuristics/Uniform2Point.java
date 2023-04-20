package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;

import java.util.Random;

public class Uniform2Point extends CrossoverHeuristic {

    public Uniform2Point(MinSetProblem problem, Random random) {
        super(problem, random);
    }
    @Override
    public Solution[] applyHeuristicSingle(int parent1_index, int parent2_index) {
        Solution child1 = problem.getSolution(parent1_index).clone();
        Solution child2 = problem.getSolution(parent2_index).clone();

        int ran1 = getRng().nextInt(problem.getNumberOfSubsets());
        int ran2 = getRng().nextInt(problem.getNumberOfSubsets());

        int min = Math.min(ran1,ran2);
        int max = Math.max(ran1,ran2);
        // Max is exclusive to number of subsets so can reach up to length-1 of the array. Thus, inclusive for loop.
        for(int i =min; i<=max; i++){
           // System.out.print("hih " + i);
            problem.getOperations().exchangeBits(child1,child2,i);
        }
        return new Solution[]{child1,child2};
    }

}
