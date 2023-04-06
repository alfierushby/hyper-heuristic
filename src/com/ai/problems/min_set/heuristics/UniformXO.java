package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;
import com.ai.problems.min_set.SolutionEvaluator;

import java.util.Random;

public class UniformXO extends CrossoverHeuristic {

    public UniformXO(MinSetProblem problem, Random random) {
        super(problem, random);
    }

    @Override
    public void applyHeuristic(int sol_index, int save_index) {

    }

    @Override
    public void applyHeuristic(int parent1Index, int parent2Index, int childIndex) {
        Solution child1 = problem.getSolution(parent1Index).clone();
        Solution child2 = problem.getSolution(parent2Index).clone();

        for(int i =0; i<problem.getNumberOfSubsets(); i++){
            double rand = getRng().nextDouble();
            if(rand<=0.5){
                problem.getOperations().exchangeBits(child1,child2,i);
            }
        }

    }

}
