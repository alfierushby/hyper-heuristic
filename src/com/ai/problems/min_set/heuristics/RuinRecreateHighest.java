package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;

import java.lang.constant.Constable;
import java.util.Random;

public class RuinRecreateHighest extends RuinRecreateHeuristic{

    public RuinRecreateHighest(MinSetProblem problem, Random rng) {
        super(problem, rng);
    }


    /**
     * Picks a random point in the solution and sets intensity nodes to false.
     * @param save_index Solution index where it saves
     */
    @Override
    public void applyHeuristicSingle(int save_index) {
        int len = getProblem().getNumberOfSubsets();
        int ran = getRng().nextInt(len);
        for(int i=ran;i<ran+getIntensity();i++){
            if(i<len){
                bitFalse(i,save_index);
            } else{
                bitFalse(i-len,save_index);
            }
        }
    }
}