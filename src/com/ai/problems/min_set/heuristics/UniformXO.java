package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;
import com.ai.problems.min_set.SolutionEvaluator;

import java.util.Arrays;
import java.util.Random;

public class UniformXO extends CrossoverHeuristic {

    public UniformXO(MinSetProblem problem, Random random) {
        super(problem, random);
    }
    @Override
    public Solution[] applyHeuristicSingle(int parent1_index, int parent2_index) {
        Solution child1 = problem.getSolution(parent1_index).clone();
        Solution child2 = problem.getSolution(parent2_index).clone();

        for(int i =0; i<problem.getNumberOfSubsets(); i++){
            double rand = getRng().nextDouble();
            if(rand<=0.5){
                System.out.print("hih " + i);
                problem.getOperations().exchangeBits(child1,child2,i);
            }
        }
        return new Solution[]{child1,child2};
    }

}
