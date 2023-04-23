package com.ai;

import com.ai.problems.min_set.heuristics.Heuristic;

import java.util.ArrayList;

// A standard interface for any problem domain. Acted upon by the
// hyper heuristic.
public interface Problem {

    /**
     * Loads a problem instance
     * @param path the path to the instance name, emitting the instance name!
     * @param file the name of the file in this path. Do not include a .txt, as it is inferred!
     */
    void loadInstance(String path, String file);

    /**
     * Intialise the solution via some method
     * @param current_solution_index Current solution to intialise
     * @param backup_solution_index A backup solution
     */
    void initialiseSolution(int current_solution_index, int backup_solution_index);

    /**
     * Gets the objective value of the solution.
     * @param index Index of the solution in memory
     * @return
     */
    int getObjectiveValue(int index);
    Heuristic[] getHeuristics(HeuristicClasses h_class);
    void printInfo(int index);
    void copySolution(int from, int to);

    /**
     * @return -1 or 1, with 1 entailing a minimisation problem, and -1 entailing a maximisation problem.
     */
    int getObjectiveValueGoal();
    String getInstanceName();

    /**
     * This is called when a best solution is detected and the solution memory needs to be saved for usage purposes.
     * @param sol_index Index of the solution to be saved in memory
     */
    void setBestSolution(int sol_index);

}
