package com.ai.problems.min_set.records;


/**
 * @param heuristicID ID of the heuristic pertaining to an ArrayList index.
 * @param data The time and objective value data.
 */
public record HeuristicData(int heuristicID, TimeObjectiveValue data) {
}
