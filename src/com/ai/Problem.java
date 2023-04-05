package com.ai;

import com.ai.problems.min_set.heuristics.Heuristic;

import java.util.ArrayList;

// A standard interface for any problem domain. Acted upon by the
// hyper heuristic.
public interface Problem {

    // Loads a problem instance specified by a path string.
    void loadInstance(String Path);
    void initialiseSolution();
    int getObjectiveValue(int index);
    Heuristic[] getHeuristics(HeuristicClasses h_class);
    void printInfo(int index);
    void copySolution(int from, int to);

}
