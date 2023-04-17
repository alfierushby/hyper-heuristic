package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

public class RuinRecreateLowest extends RuinRecreateHeuristic{

    public RuinRecreateLowest(MinSetProblem problem, Random rng) {
        super(problem, rng);
    }

    /**
     * Picks a random point in the solution and sets intensity nodes to false.
     * @param save_index Solution index where it saves
     */
    @Override
    public void applyHeuristicSingle(int save_index) {
        int len = problem.getNumberOfSubsets();
        int ran = getRng().nextInt(len);
        int amount = ran-getIntensity();
        for(int i=ran;i>ran-getIntensity();i--){
            if(i>0){
                bitFalse(i,save_index);
            } else{
                bitFalse(len-1+i,save_index);
            }
        }
    }
}
