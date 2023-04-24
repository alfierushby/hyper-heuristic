package com.ai.problems.min_set.heuristics;

import com.ai.HeuristicClasses;
import com.ai.problems.min_set.MinSetProblem;

import java.util.ArrayList;
import java.util.Random;

/**
 * Implements the Davis Bit Hill Climbing method. Accepts non-worsening solutions.
 */
public class DavisBitHC extends IterableHeuristic {

    @Override
    public HeuristicClasses getHeuristicClass() {
        return HeuristicClasses.Hill_Climbing;
    }

    public DavisBitHC(MinSetProblem problem, Random rng) {
        super(problem,rng);
        iterations = getProblem().getDepthOfSearch();
    }

    private ArrayList<Integer> createRandomPermutation(){
        ArrayList<Integer> to_shuffle = new ArrayList<>();
        for(int i = 0; i < getProblem().getNumberOfSubsets(); i++){
            to_shuffle.add(i);
        }
        int len = to_shuffle.size();
        for(int i = 0; i<len; i++){
            int ran = getRng().nextInt(0,len);
            int temp_v = to_shuffle.get(i);
            to_shuffle.set(i,to_shuffle.get(ran));
            to_shuffle.set(ran, temp_v);
        }

        return to_shuffle;
    }

    public void applyHeuristicSingle(int sol_index) {
        int bestEval = problem.getObjectiveValue(sol_index), tmpEval;
        ArrayList<Integer> perm = createRandomPermutation();
            for(int j = 0; j < problem.getNumberOfSubsets(); j++){
                problem.getOperations().bitFlip(perm.get(j),sol_index);

                tmpEval = problem.getObjectiveValue(sol_index);

                if(tmpEval <= bestEval){
                    bestEval = tmpEval;
                } else {
                    problem.getOperations().bitFlip(perm.get(j),sol_index); // Revert
                }
            }
    }

}
