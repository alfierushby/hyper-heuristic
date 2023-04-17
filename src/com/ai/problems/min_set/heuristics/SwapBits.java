package com.ai.problems.min_set.heuristics;

import com.ai.HeuristicClasses;
import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;

import java.util.Random;

public class SwapBits extends IterableHeuristic{

    @Override
    public HeuristicClasses getHeuristicClass() {
        return HeuristicClasses.Mutational;
    }

    public SwapBits(MinSetProblem problem, Random rng) {
        super(problem,rng);
        iterations = getProblem().getIntensityOfMutation();
        skippable= false;
    }

    @Override
    public void applyHeuristicSingle(int sol_index) {
        // select a random bit in the solution
        int bit_1 = getRng().nextInt(problem.getNumberOfSubsets());
        int bit_2 = getRng().nextInt(problem.getNumberOfSubsets());
        boolean[] solution =  getProblem().getSolution(sol_index).getSolutionData();

        boolean bit1 = solution[bit_1];
        boolean bit2 = solution[bit_2];
        if(bit2 != bit1){
            getProblem().getOperations().bitFlip(bit_1,sol_index);
            getProblem().getOperations().bitFlip(bit_2,sol_index);
        }
    }
}
