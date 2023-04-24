package com.ai.problems.min_set.heuristics;

import com.ai.HeuristicClasses;
import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

/**
 * Implements the first improvement hill climbing method. Accepts non-worsening solutions.
 */
public class FirstImprovementHC extends IterableHeuristic {
    @Override
    public HeuristicClasses getHeuristicClass() {
        return HeuristicClasses.Hill_Climbing;
    }

    public FirstImprovementHC(MinSetProblem problem, Random rng) {
        super(problem,rng);
        iterations = getProblem().getDepthOfSearch();
    }
    public void applyHeuristicSingle(int sol_index) {
        int bestEval = problem.getObjectiveValue(sol_index), tmpEval;
        for(int j = 0; j < problem.getNumberOfSubsets(); j++){
            problem.getOperations().bitFlip(j,sol_index);

            tmpEval = problem.getObjectiveValue(sol_index);

            if(tmpEval <= bestEval){
                bestEval = tmpEval;
            } else {
                problem.getOperations().bitFlip(j,sol_index); // Revert
            }
        }
    }

}
