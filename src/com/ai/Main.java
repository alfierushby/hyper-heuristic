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

//        Random ran = new Random(123456789);
//        MinSetProblem problem = new MinSetProblem(ran);
//        problem.loadInstance("src/test_instances","d4_2047_495");
//        HyperHeuristicModifiedChoice hyper_heuristic = new HyperHeuristicModifiedChoice(ran);
//        hyper_heuristic.applyHyperHeuristic(problem);

//        TaguchiParameterTuning test = new TaguchiParameterTuning();
//        test.startTest();



    }
}