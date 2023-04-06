package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

public class SteepestDescentHC extends Heuristic{
    public SteepestDescentHC(MinSetProblem problem, Random rng) {
        super(problem,rng);
        iterations = getProblem().getDepthOfSearch();
    }

    @Override
    public void applyHeuristicSingle(int sol) {

        double bestEval = problem.getNumberOfSubsets(), tmpEval;
        boolean improved = false;
        int bestIndex=0;

        for(int j = 0; j < problem.getNumberOfSubsets(); j++){
            problem.getOperations().bitFlip(j,sol);
            tmpEval = problem.getObjectiveValue(sol);

            if(tmpEval< bestEval){
                bestIndex = j;
                bestEval = tmpEval;
                improved = true;
            }

            problem.getOperations().bitFlip(j,sol);
        }

        if(improved){ problem.getOperations().bitFlip(bestIndex,sol); };

    }
}
