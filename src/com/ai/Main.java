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

public class Main {
    private static final int CURRENT_SOLUTION_INDEX = 0;
    private static final int BACKUP_SOLUTION_INDEX = 1;
    public static void main(String[] args) {
         Random ran = new Random(123456789);
         MinSetProblem problem = new MinSetProblem(ran);
       problem.loadInstance("src/test_instances/d1_50_500.txt");
       //  HyperHeuristicModifiedChoice hyper_heuristic = new HyperHeuristicModifiedChoice(ran);
        problem.initialiseSolution(0, 1);
      //  hyper_heuristic.applyHyperHeuristic(problem);

        //TaguchiParameterTuning test = new TaguchiParameterTuning();

         //problem.getSolution(hyper_heuristic.CURRENT_SOLUTION_INDEX).getEvaluator().setObjectiveValue();
        // System.out.println("I am " + problem.getObjectiveValue(hyper_heuristic.CURRENT_SOLUTION_INDEX));
//
//        IterableHeuristic[] mutation = (IterableHeuristic[]) problem.getHeuristics(Mutational);
//        IterableHeuristic[] hill_climbing = (IterableHeuristic[]) problem.getHeuristics(Hill_Climbing);
//        CrossoverHeuristic[] crossover = (CrossoverHeuristic[]) problem.getHeuristics(Crossover);
//        RuinRecreateHeuristic[] ruins = (RuinRecreateHeuristic[]) problem.getHeuristics(Ruin_and_Recreate);
//        problem.getOperations().bitFlip(0,CURRENT_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
//        hill_climbing[0].applyHeuristic(CURRENT_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
//        problem.printInfo(CURRENT_SOLUTION_INDEX);
//        problem.getEvaluator(CURRENT_SOLUTION_INDEX).setObjectiveValue();
//        problem.printInfo(CURRENT_SOLUTION_INDEX);

//        for(int i =0; i<10; i++){
//            System.out.println("///////////////////////////////////");
//            hill_climbing[2].applyHeuristic(hyper_heuristic.CURRENT_SOLUTION_INDEX, hyper_heuristic.CURRENT_SOLUTION_INDEX);
////            problem.getOperations().exchangeBits(problem.getSolution(CURRENT_SOLUTION_INDEX),
////                    problem.getSolution(BACKUP_SOLUTION_INDEX),1);
//            problem.printInfo(hyper_heuristic.CURRENT_SOLUTION_INDEX);
//            System.out.println("///////////////////////////////////");
//        }
//        Solution test = new Solution(problem.getSolution(BACKUP_SOLUTION_INDEX).getSolutionData(),
//                problem.getSolutionMap(BACKUP_SOLUTION_INDEX));
//        problem.insertSolution(test,2);
//        problem.printInfo(2);

    }
}