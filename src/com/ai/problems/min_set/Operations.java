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


    /**
     * @param bit_index Bit index to perform the bit flip in sol_index
     * @param sol_index The solution to perform the bit flip
     * @param save_index Where the solution is saved.
     */
    public void bitFlip(int bit_index, int sol_index, int save_index){
        // Perform a copy.
        if(sol_index!=save_index){
            problem.copySolution(sol_index,save_index);
        }

        boolean bit = problem.getSolution(save_index).getSolutionData()[bit_index];

        problem.getSolution(save_index).getSolutionData()[bit_index]=!bit;
        // Perform Delta evaluation on solution.
        problem.getEvaluator(save_index).deltaObjectiveEvaluation(bit_index);
    }


    /**
     * @param bit_index Bit index to perform the bit flip in sol_index
     * @param sol_index The solution to perform the bit flip, saved there
     */
    public void bitFlip(int bit_index, int sol_index){
        // Perform a copy.
        bitFlip(bit_index,sol_index,sol_index);
    }

}
