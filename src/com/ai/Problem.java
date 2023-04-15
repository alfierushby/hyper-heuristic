package com.ai;

import com.ai.problems.min_set.heuristics.Heuristic;

import java.util.ArrayList;

// A standard interface for any problem domain. Acted upon by the
// hyper heuristic.
public interface Problem {

    // Loads a problem instance specified by a path string.
    void loadInstance(String Path);
    void initialiseSolution(int current_solution_index, int backup_solution_index);
    int getObjectiveValue(int index);
    Heuristic[] getHeuristics(HeuristicClasses h_class);
    void printInfo(int index);
    void copySolution(int from, int to);

    /**
     * @return -1 or 1, with 1 entailing a minimisation problem, and -1 entailing a maximisation problem.
     */
    int getObjectiveValueGoal();

}
