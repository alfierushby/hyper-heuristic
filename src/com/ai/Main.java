package com.ai;

import com.ai.problems.min_set.MinSetProblem;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random ran = new Random(123456789);

        MinSetProblem problem = new MinSetProblem(ran);
        problem.loadInstance("src/test_instances/d1_50_500.txt");
        problem.initialiseSolution();
        System.out.println("Hello world!");
    }
}