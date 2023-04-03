package com.ai.problems.min_set;

/**
 * Contains fundamental operations that are used by heuristics.
 */
public class Operations {
    private final MinSetProblem problem;

    public Operations(MinSetProblem problem) {
        this.problem = problem;
    }

    public void bitFlip(int bit_index){
        int bit = problem.getSolution()[bit_index];
        if(bit==1)
            bit=0;
        else
            bit=1;
        problem.getSolution()[bit_index] = bit;
        // Perform Delta evaluation on problem.
        problem.getEvaluator().deltaObjectiveEvaluation(bit_index);
    }

}
