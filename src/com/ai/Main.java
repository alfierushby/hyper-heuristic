package com.ai;

import com.ai.problems.min_set.MinSetProblem;

public class Main {
    public static void main(String[] args) {
        MinSetProblem problem = new MinSetProblem();
        problem.loadInstance("src/test_instances/d1_50_500.txt");
        System.out.println("Hello world!");
    }
}