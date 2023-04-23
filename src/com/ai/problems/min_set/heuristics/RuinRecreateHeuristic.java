package com.ai.problems.min_set.heuristics;

import com.ai.HeuristicClasses;
import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;

import java.util.Random;

import static com.ai.Config.INTENSITY_OF_MUTATION;


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
        this.intensity = (int) getProblem().getIntensityOfMutation()*2;
        if(intensity>getProblem().getNumberOfSubsets())
            this.intensity = getProblem().getNumberOfSubsets();
    }

    /**
     * The heuristic single iteration to be executed.
     * @param save_index Solution index to apply to
     */
    public abstract void applyHeuristicSingle(int save_index);


    @Override
    public void applyHeuristic(int sol_index, int save_index) {
        getProblem().copySolution(sol_index,save_index);
        // Ruin solution
        applyHeuristicSingle(save_index);
        // Recreate solution
        Solution sol = getProblem().getSolution(save_index);
        boolean[] sol_data = sol.getSolutionData();
        while(sol.getUnaccountedElements()>0){
            int ran_index = getRng().nextInt(getProblem().getNumberOfSubsets());
            if(!sol_data[ran_index])
                getProblem().getOperations().bitFlip(ran_index,save_index);
        }
    }
}
