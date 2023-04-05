package com.ai.problems.min_set;

import static com.ai.problems.min_set.Config.BACKUP_SOLUTION_INDEX;
import static com.ai.problems.min_set.Config.CURRENT_SOLUTION_INDEX;

/**
 * Contains fundamental operations that are used by heuristics.
 */
public class Operations {
    private final MinSetProblem problem;

    public Operations(MinSetProblem problem) {
        this.problem = problem;
    }

    public void bitFlip(int bit_index){
        boolean bit = problem.getSolution(CURRENT_SOLUTION_INDEX).getSolutionData()[bit_index];

        bit=!bit;

        problem.getSolution(BACKUP_SOLUTION_INDEX).getSolutionData()[bit_index] = bit;
        // Perform Delta evaluation on problem.
        problem.getEvaluator(BACKUP_SOLUTION_INDEX).deltaObjectiveEvaluation(bit_index);
    }

}
