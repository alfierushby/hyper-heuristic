package com.ai.problems.min_set.heuristics;

import com.ai.HeuristicClasses;
import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

public abstract class RuinRecreateHeuristic extends Heuristic{
    @Override
    public HeuristicClasses getHeuristicClass() {
        return HeuristicClasses.Ruin_and_Recreate;
    }

    int intensity;

    public int getIntensity() {
        return intensity;
    }

    public RuinRecreateHeuristic(MinSetProblem problem, Random rng) {
        super(problem, rng);
        this.intensity = problem.getIntensityOfMutation()*2;
    }

    /**
     * The heuristic single iteration to be executed.
     * @param save_index Solution index to apply to
     */
    public abstract void applyHeuristicSingle(int save_index);

    /**
     * Makes specified bit false. Includes delta evaluation.
     * @param bit_index Bit to make false
     * @param sol_index Solution index to work on
     */
    public void bitFalse(int bit_index,int sol_index){
        if(getProblem().getSolution(sol_index).getSolutionData()[bit_index])
            getProblem().getOperations().bitFlip(bit_index,sol_index);
    }

    @Override
    public void applyHeuristic(int sol_index, int save_index) {
        getProblem().copySolution(sol_index,save_index);
        applyHeuristicSingle(save_index);
    }
}
