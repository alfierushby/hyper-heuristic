package com.ai;

import com.ai.problems.min_set.MinSetProblem;
import com.ai.problems.min_set.Solution;
import com.ai.problems.min_set.heuristics.CrossoverHeuristic;
import com.ai.problems.min_set.heuristics.Heuristic;
import com.ai.problems.min_set.heuristics.IterableHeuristic;
import com.ai.problems.min_set.heuristics.RuinRecreateHeuristic;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

import static com.ai.HeuristicClasses.*;
import static com.ai.problems.min_set.enums.InstanceReader.NumElements;
import static com.ai.problems.min_set.enums.InstanceReader.NumSubsets;

public class Main {
    private static final int CURRENT_SOLUTION_INDEX = 0;
    private static final int BACKUP_SOLUTION_INDEX = 1;
    public static void main(String[] args) throws IOException {

        FileOutput runner = new FileOutput();
        runner.run("d2_50_500");

//         Random ran = new Random(123456789);
//         MinSetProblem problem = new MinSetProblem(ran);
//         problem.loadInstance("src/test_instances","d4_2047_495");
//        HyperHeuristicModifiedChoice hyper_heuristic = new HyperHeuristicModifiedChoice(ran);
         //   problem.initialiseSolution(0, 1);
//        hyper_heuristic.applyHyperHeuristic(problem,new int[]{problem.getData().get(NumSubsets),
//        problem.getData().get(NumElements),0});

//        TaguchiParameterTuning test = new TaguchiParameterTuning();
//        test.startTest();

         //problem.getSolution(hyper_heuristic.CURRENT_SOLUTION_INDEX).getEvaluator().setObjectiveValue();
        // System.out.println("I am " + problem.getObjectiveValue(hyper_heuristic.CURRENT_SOLUTION_INDEX));
//
//        IterableHeuristic[] mutation = (IterableHeuristic[]) problem.getHeuristics(Mutational);
//        IterableHeuristic[] hill_climbing = (IterableHeuristic[]) problem.getHeuristics(Hill_Climbing);
//         CrossoverHeuristic[] crossover = (CrossoverHeuristic[]) problem.getHeuristics(Crossover);
//       RuinRecreateHeuristic[] ruins = (RuinRecreateHeuristic[]) problem.getHeuristics(Ruin_and_Recreate);
//        problem.getOperations().bitFlip(0,CURRENT_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
//        hill_climbing[0].applyHeuristic(CURRENT_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
//        problem.printInfo(BACKUP_SOLUTION_INDEX);
//        hill_climbing[0].applyHeuristic(CURRENT_SOLUTION_INDEX,BACKUP_SOLUTION_INDEX);
//        problem.printInfo(BACKUP_SOLUTION_INDEX);
//        problem.printInfo(CURRENT_SOLUTION_INDEX);
//        crossover[2].applyHeuristic(CURRENT_SOLUTION_INDEX,BACKUP_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
//        problem.printInfo(BACKUP_SOLUTION_INDEX);
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