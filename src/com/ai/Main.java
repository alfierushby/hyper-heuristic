package com.ai;

import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.heuristics.Heuristic;

import java.util.Random;

import static com.ai.HeuristicClasses.*;
import static com.ai.problems.min_set.Config.*;

public class Main {
    public static void main(String[] args) {
        Random ran = new Random(123456789);

        Problem problem = new MinSetProblem(ran);
        problem.loadInstance("src/test_instances/d1_50_500.txt");
        problem.initialiseSolution();
        Heuristic[] mutation = problem.getHeuristics(Mutational);
        for(int i =0; i<1000; i++){
            System.out.println("///////////////////////////////////");
            mutation[0].applyHeuristic(CURRENT_SOLUTION_INDEX,BACKUP_SOLUTION_INDEX);
            problem.copySolution(BACKUP_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
            problem.printInfo(CURRENT_SOLUTION_INDEX);
            System.out.println("///////////////////////////////////");
        }
    }
}