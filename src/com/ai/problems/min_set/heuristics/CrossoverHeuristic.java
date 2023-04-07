package com.ai.problems.min_set.heuristics;

import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;

import java.util.Random;

public abstract class CrossoverHeuristic extends Heuristic {


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
    public  void applyHeuristic(int sol, int save){
        applyHeuristic(sol,sol,save);
    }

    /**
     * This performs the relevant crossover, and saves the best child to the child index.
     * @param parent1Index First Parent index for crossover
     * @param parent2Index Second Parent index for crossover
     * @param childIndex The child index where the new child is saved.
     */
    public  void applyHeuristic(int parent1Index, int parent2Index, int childIndex){
        Solution[] children = applyHeuristicSingle(parent1Index,parent2Index);
        if(children[0].getEvaluator().getObjectiveValue()<children[1].getEvaluator().getObjectiveValue())
            getProblem().setSolution(children[0],childIndex);
        else
            getProblem().setSolution(children[1],childIndex);
    }

}
