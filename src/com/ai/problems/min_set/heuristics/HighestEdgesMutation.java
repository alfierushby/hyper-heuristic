package com.ai.problems.min_set.heuristics;

import com.ai.HeuristicClasses;
import com.ai.problems.min_set.MinSetProblem;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.Random;

/**
 * Gets the highest number of edges for a node in the base elements set and finds a subset that contributes to that,
 * setting it to false.
 */
public class HighestEdgesMutation extends IterableHeuristic{
    @Override
    public HeuristicClasses getHeuristicClass() {
        return HeuristicClasses.Mutational;
    }

    public HighestEdgesMutation(MinSetProblem problem, Random rng) {
        super(problem,rng);
        iterations = getProblem().getIntensityOfMutation();
        skippable= false;
    }

    @Override
    public void applyHeuristicSingle(int sol_index) {
        // select a random bit in the solution
        int[] solution_map = getProblem().getSolution(sol_index).getSolutionMap();
        boolean[] solution_data = getProblem().getSolution(sol_index).getSolutionData();

        // Get max element
        int max = 0;
        int max_index = 0;
        for(int i=0; i<solution_map.length; i++){
            if(solution_map[i] > max){
                max = solution_map[i];
                max_index = i;
            }
        }

        // Find the first subset in the solution data that contributes to this.
        for(int i=0; i < solution_data.length; i++){
            if(solution_data[i] && getProblem().getSubsets().get(i)[max_index]==1){
                // It contributes, so we can remove this subset.
                System.out.println("I am contributing :OOOO");
                bitFalse(i,sol_index);
                break;
            }
        }

    }
}
