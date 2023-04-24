package com.ai.problems.min_set.heuristics;

import com.ai.HeuristicClasses;
import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

/**
 * Implements the Steepest Descent hill climbing method.
 */
public class SteepestDescentHC extends IterableHeuristic {
    @Override
    public HeuristicClasses getHeuristicClass() {
        return HeuristicClasses.Hill_Climbing;
    }

    public SteepestDescentHC(MinSetProblem problem, Random rng) {
        super(problem,rng);
        iterations = getProblem().getDepthOfSearch();
    }

    @Override
    public void applyHeuristicSingle(int sol_index) {

        double bestEval = problem.getNumberOfSubsets(), tmpEval;
        boolean improved = false;
        int bestIndex=0;

        for(int j = 0; j < problem.getNumberOfSubsets(); j++){
            problem.getOperations().bitFlip(j,sol_index);
            tmpEval = problem.getObjectiveValue(sol_index);

            if(tmpEval < bestEval){
                bestIndex = j;
                bestEval = tmpEval;
                improved = true;
            }

            problem.getOperations().bitFlip(j,sol_index);
        }

        if(improved){ problem.getOperations().bitFlip(bestIndex,sol_index); };

    }
}
