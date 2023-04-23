package com.ai;

import com.ai.problems.min_set.MinSetProblem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static com.ai.problems.min_set.enums.InstanceReader.NumElements;
import static com.ai.problems.min_set.enums.InstanceReader.NumSubsets;

public class FileOutput {
    int[] seed = {123456789,618201371,198273947,971402982,102349730};
    void run(String instance_name) throws IOException {

        FileWriter data = new FileWriter(instance_name + "_trial_data");
        String newLine = System.getProperty("line.separator");

        for(int i = 1; i<= seed.length; i++){
            data.write("Trial #"+i + newLine);
            Random ran = new Random(seed[i-1]);
            MinSetProblem problem = new MinSetProblem(ran);
            problem.loadInstance("src/test_instances",instance_name);
            HyperHeuristicModifiedChoice hyper_heuristic = new HyperHeuristicModifiedChoice(ran);
            hyper_heuristic.applyHyperHeuristic(problem,new int[]{problem.getData().get(NumSubsets),
                    problem.getData().get(NumElements),i});
            boolean[] sol_data = problem.getBestSolution().getSolutionData();

            data.write(hyper_heuristic.getBestObjectiveValue() + newLine +
                    Arrays.toString(sol_data).replace("true", "1").replace("false", "0") + newLine);
        }
        data.close();

    }

}
