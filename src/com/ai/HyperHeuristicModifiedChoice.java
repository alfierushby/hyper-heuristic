package com.ai;

import com.ai.problems.min_set.records.HeuristicData;
import com.ai.problems.min_set.records.HeuristicPair;
import com.ai.problems.min_set.records.TimeObjectiveValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Hyper Heuristic that implements the Modified Choice Function, includes Crossover
 * from a list of the top 5 solutions as its secondary parent solution.
 */
public class HyperHeuristicModifiedChoice implements HyperHeuristic{
    int maxNumberOf=100;
    int bestObjectiveValue, CurrentObjectiveValue;
    HeuristicData prev_heuristic;
    /**
     * Each entry represents a heuristic ID, and for each ID is a list of executions done for that specific heuristic.
     * To be used in the f1 function.
     */
    ArrayList<TimeObjectiveValue[]> heuristicSingleData = new ArrayList<>();

    /**
     * This maps a size 2 pair of hk to hj to a TimeObjectiveValue pair that determines the change in objective value
     * from hk to hj, and the time taken to execute hj.
     * In Array form to represent that this can occur on multiple occasions.
     */
    Map<HeuristicPair, TimeObjectiveValue[]> followHeuristicTimes= new HashMap<>();

    @Override
    public void applyHyperHeuristic(Problem problem_domain) {

    }
}
