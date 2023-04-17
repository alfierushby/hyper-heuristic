package com.ai;

import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;
import com.ai.problems.min_set.heuristics.CrossoverHeuristic;
import com.ai.problems.min_set.heuristics.Heuristic;
import com.ai.problems.min_set.heuristics.IterableHeuristic;
import com.ai.problems.min_set.heuristics.RuinRecreateHeuristic;

import java.util.Base64;
import java.util.Random;

import static com.ai.HeuristicClasses.*;
import static com.ai.problems.min_set.Config.*;

public class Main {
    public static void main(String[] args) {
        Random ran = new Random(32423453);
         MinSetProblem problem = new MinSetProblem(ran);
         problem.loadInstance("src/test_instances/d4_2047_495.txt");
         HyperHeuristicModifiedChoice hyper_heuristic = new HyperHeuristicModifiedChoice(ran);
         hyper_heuristic.applyHyperHeuristic(problem);
         problem.getSolution(hyper_heuristic.CURRENT_SOLUTION_INDEX).getEvaluator().setObjectiveValue();
         System.out.println("I am " + problem.getObjectiveValue(hyper_heuristic.CURRENT_SOLUTION_INDEX));

//        IterableHeuristic[] mutation = (IterableHeuristic[]) problem.getHeuristics(Mutational);
//        IterableHeuristic[] hill_climbing = (IterableHeuristic[]) problem.getHeuristics(Hill_Climbing);
//        CrossoverHeuristic[] crossover = (CrossoverHeuristic[]) problem.getHeuristics(Crossover);
//        RuinRecreateHeuristic[] ruins = (RuinRecreateHeuristic[]) problem.getHeuristics(Ruin_and_Recreate);
        //problem.getOperations().bitFlip(0,CURRENT_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
//        hill_climbing[0].applyHeuristic(CURRENT_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
//        problem.printInfo(CURRENT_SOLUTION_INDEX);
//        problem.printInfo(BACKUP_SOLUTION_INDEX);

//        for(int i =0; i<1000; i++){
//            System.out.println("///////////////////////////////////");
//            ruins[1].applyHeuristic(CURRENT_SOLUTION_INDEX,BACKUP_SOLUTION_INDEX);
//            crossover[2].applyHeuristic(CURRENT_SOLUTION_INDEX,BACKUP_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
//            hill_climbing[2].applyHeuristic(BACKUP_SOLUTION_INDEX, BACKUP_SOLUTION_INDEX);
////            problem.getOperations().exchangeBits(problem.getSolution(CURRENT_SOLUTION_INDEX),
////                    problem.getSolution(BACKUP_SOLUTION_INDEX),1);
//            problem.copySolution(BACKUP_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
//             problem.printInfo(CURRENT_SOLUTION_INDEX);
//            System.out.println("///////////////////////////////////");
//        }
//        Solution test = new Solution(problem.getSolution(BACKUP_SOLUTION_INDEX).getSolutionData(),
//                problem.getSolutionMap(BACKUP_SOLUTION_INDEX));
//        problem.insertSolution(test,2);
//        problem.printInfo(2);

    }
}