package com.ai.problems.min_set.records;

/**
 * @param Time Time it took to run the heuristic, relative. In Milliseconds.
 * @param ObjectiveValue An objective value for the heuristic. Can be a change from the previous, or an absolute measure.
 */
public record TimeObjectiveValue(long Time, int ObjectiveValue) {
}
