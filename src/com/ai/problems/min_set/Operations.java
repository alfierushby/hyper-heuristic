package com.ai.problems.min_set;

/**
 * Contains fundamental operations that are used by heuristics.
 */
public class Operations {
    private final MinSetProblem problem;

    public Operations(MinSetProblem problem) {
        this.problem = problem;
    }

    public void bitFlip(int sol_index,int bit_index){
        int bit = problem.getSolution(sol_index)[bit_index];
        if(bit==1)
            bit=0;
        else
            bit=1;
        problem.getSolution(sol_index)[bit_index] = bit;
        // Perform Delta evaluation on problem.
        problem.getEvaluator(sol_index).deltaObjectiveEvaluation(bit_index);
    }

}
