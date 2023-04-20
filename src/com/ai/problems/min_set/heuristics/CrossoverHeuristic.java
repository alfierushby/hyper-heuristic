package com.ai.problems.min_set.heuristics;

import com.ai.HeuristicClasses;
import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;

import java.util.Arrays;
import java.util.Random;

public abstract class CrossoverHeuristic extends Heuristic {

    @Override
    public HeuristicClasses getHeuristicClass() {
        return HeuristicClasses.Crossover;
    }

    public CrossoverHeuristic(MinSetProblem problem, Random rng) {
        super(problem, rng);
    }

    /**
     * This is returns both children, and is used as an intermediary to then have the best child selected and set.
     * @param parent1Index First Parent index for crossover
     * @param parent2Index Second parent index for crossover
     * @return A size 2 array of both children created.
     */
    public abstract Solution[] applyHeuristicSingle(int parent1Index, int parent2Index);
    @Override
    public  void applyHeuristic(int sol_index, int save_index){
        applyHeuristic(sol_index,sol_index,save_index);
    }

    /**
     * This performs the relevant crossover, and saves the best child to the child index.
     * @param parent1_index First Parent index for crossover
     * @param parent2_index Second Parent index for crossover
     * @param child_index The child index where the new child is saved.
     */
    public  void applyHeuristic(int parent1_index, int parent2_index, int child_index){
        Solution[] children = applyHeuristicSingle(parent1_index,parent2_index);
        if(children[0].getEvaluator().getObjectiveValue()<children[1].getEvaluator().getObjectiveValue()){
           // System.out.println("Chose child 0");
            getProblem().setSolution(children[0],child_index);
        }
        else{
          //  System.out.println("Chose child1");
            getProblem().setSolution(children[1],child_index);
        }
    }

}
