 package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;

import java.util.Random;

public class Uniform1Point extends CrossoverHeuristic {

    public Uniform1Point(MinSetProblem problem, Random random) {
        super(problem, random);
    }
    @Override
    public Solution[] applyHeuristicSingle(int parent1_index, int parent2_index) {
        Solution child1 = problem.getSolution(parent1_index).clone();
        Solution child2 = problem.getSolution(parent2_index).clone();

        int ran = getRng().nextInt(problem.getNumberOfSubsets());
//        System.out.println("ran : " + ran);
        for(int i =ran; i<problem.getNumberOfSubsets(); i++){
//            System.out.print("hih " + i);
            problem.getOperations().exchangeBits(child1,child2,i);
        }
        return new Solution[]{child1,child2};
    }

}
