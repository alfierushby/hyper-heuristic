package com.ai.problems.min_set.heuristics;

import com.ai.HeuristicClasses;
import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;

import java.util.Random;


/**
 * A general ruin-recreate heuristic. Will rebuild the solution after applying the ruin part.
 */
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
    public abstract void applyRuin(int save_index);


    @Override
    public void applyHeuristic(int sol_index, int save_index) {
        getProblem().copySolution(sol_index,save_index);
        // Ruin solution
        applyRuin(save_index);
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
