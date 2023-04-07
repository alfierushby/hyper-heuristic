package com.ai;

import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.heuristics.CrossoverHeuristic;
import com.ai.problems.min_set.heuristics.Heuristic;
import com.ai.problems.min_set.heuristics.IterableHeuristic;

import java.util.Random;

import static com.ai.HeuristicClasses.*;
import static com.ai.problems.min_set.Config.*;

public class Main {
    public static void main(String[] args) {
        Random ran = new Random(123456789);

        MinSetProblem problem = new MinSetProblem(ran);
        problem.loadInstance("src/test_instances/d1_50_500.txt");
        problem.initialiseSolution();
        IterableHeuristic[] mutation = (IterableHeuristic[]) problem.getHeuristics(Mutational);
        IterableHeuristic[] hill_climbing = (IterableHeuristic[]) problem.getHeuristics(Hill_Climbing);
        CrossoverHeuristic[] crossover = (CrossoverHeuristic[]) problem.getHeuristics(Crossover);
        problem.getOperations().bitFlip(0,CURRENT_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
        hill_climbing[0].applyHeuristic(CURRENT_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
        problem.printInfo(CURRENT_SOLUTION_INDEX);
        problem.printInfo(BACKUP_SOLUTION_INDEX);

        for(int i =0; i<1; i++){
            System.out.println("///////////////////////////////////");
          //  mutation[0].applyHeuristic(CURRENT_SOLUTION_INDEX,BACKUP_SOLUTION_INDEX);
            crossover[0].applyHeuristic(CURRENT_SOLUTION_INDEX,BACKUP_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
            problem.printInfo(CURRENT_SOLUTION_INDEX);
            problem.printInfo(BACKUP_SOLUTION_INDEX);
            System.out.println("///////////////////////////////////");
        }
    }
}