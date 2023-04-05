package com.ai;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random ran = new Random(123456789);

        MinSetProblem problem = new MinSetProblem(ran);
        problem.loadInstance("src/test_instances/d1_50_500.txt");
        problem.initialiseSolution(0);
        for(int i =0; i<20; i++){
            System.out.println("///////////////////////////////////");
            problem.getOperations().bitFlip(0,i);
            problem.printInfo(0);
            problem.getEvaluator(0).setObjectiveValue();
            problem.printInfo(0);
            System.out.println("///////////////////////////////////");
        }
    }
}