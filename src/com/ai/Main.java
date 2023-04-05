package com.ai;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

import static com.ai.problems.min_set.Config.*;

public class Main {
    public static void main(String[] args) {
        Random ran = new Random(123456789);

        MinSetProblem problem = new MinSetProblem(ran);
        problem.loadInstance("src/test_instances/d1_50_500.txt");
        problem.initialiseSolution();
        for(int i =0; i<20; i++){
            System.out.println("///////////////////////////////////");
            problem.getOperations().bitFlip(i);
            problem.copySolution(BACKUP_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
            problem.printInfo(CURRENT_SOLUTION_INDEX);
            problem.getEvaluator(CURRENT_SOLUTION_INDEX).setObjectiveValue();
            problem.printInfo(CURRENT_SOLUTION_INDEX);
            System.out.println("///////////////////////////////////");
        }
    }
}