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
        int tmp = getProblem().getIntensityOfMutation()*2;
        if(tmp > getProblem().getNumberOfSubsets()){
            tmp = getProblem().getNumberOfSubsets();
        }
        this.intensity = tmp;
        System.out.println(intensity);
    }

    /**
     * The heuristic single iteration to be executed.
     * @param save_index Solution index to apply to
     */
    public abstract void applyHeuristicSingle(int save_index);


    @Override
    public void applyHeuristic(int sol_index, int save_index) {
        getProblem().copySolution(sol_index,save_index);
        applyHeuristicSingle(save_index);
    }
}
