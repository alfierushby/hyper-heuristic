package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

public class RandomMutationHC extends IterableHeuristic {

    public RandomMutationHC(MinSetProblem problem, Random rng) {
        super(problem,rng);
        iterations = getProblem().getDepthOfSearch();
    }
    public void applyHeuristicSingle(int sol_index) {
        int bestEval = problem.getObjectiveValue(sol_index), tmpEval, size = problem.getNumberOfSubsets();
        for(int j = 0; j < size; j++){
            int index = getRng().nextInt(size);
            problem.getOperations().bitFlip(index,sol_index);

            tmpEval = problem.getObjectiveValue(sol_index);

            if(tmpEval <= bestEval){
                bestEval = tmpEval;
            } else {
                problem.getOperations().bitFlip(index,sol_index); // Revert
            }
        }
    }

}
