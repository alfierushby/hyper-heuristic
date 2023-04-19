package com.ai;

import com.ai.problems.min_set.MinSetProblem;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Random;

/**
 * Performs a L25 taguchi array test. Does 30 trials on each instance for 20 seconds, averaging the resulting best objective
 * value. Variables to modify are:
 * <br><br>
 * <b>Function 1 Weight</b>: {1,2,5,10,0.5}
 * <br>
 * <b>Function 2 Weight</b>: {1,2,5,10,0.5}
 * <br>
 * <b>Function 3 Weight</b>: {1,2,5,10,0.5}
 * <br>
 * <b>Random Initialisation</b>: {0.01,0.05,0.1,0.25,0.5}
 * <br>
 * <b>Depth of Search </b>: {0,0.2,0.4,0.6,0.8}
 * <br
 * <b>Intensity of Mutation</b>: {0,0.2,0.4,0.6,0.8}
 */
public class TaguchiParameterTuning {

    private final int ITERATIONS = 30;
    private int taguchi_iteration = 0, iteration = 0, instance=0;

    /**
     * Of form data[i][j][k], where i represents the trial, j represents the instance, and k represents the (formula 1)
     * score for each configuration.
     * Ie, we run every configuration once on a specific seed, for some instance, and then rank them from best to worst,
     * formula 1 style, and do these for every instance.
     * This will be done 30 times, with 4 instances each having 25 configurations, for the min set problem.
     */
    int[][][] result;

    int[][] taguchi_array = new int[][] {{1,1,1,1,1,1},
            {1, 2, 2, 2, 2, 2 },
            {1, 3, 3, 3, 3, 3 },
            {1, 4, 4, 4, 4, 4 },
            {1, 5, 5, 5, 5, 5 },
            {2, 1, 2, 3, 4, 5 },
            {2, 2, 3, 4, 5, 1 },
            {2, 3, 4, 5, 1, 2 },
            {2, 4, 5, 1, 2, 3 },
            {2, 5, 1, 2, 3, 4 },
            {3, 1, 3, 5, 2, 4 },
            {3, 2, 4, 1, 3, 5 },
            {3, 3, 5, 2, 4, 1 },
            {3, 4, 1, 3, 5, 2 },
            {3, 5, 2, 4, 1, 3 },
            {4, 1, 4, 2, 5, 3 },
            {4, 2, 5, 3, 1, 4 },
            {4, 3, 1, 4, 2, 5 },
            {4, 4, 2, 5, 3, 1 },
            {4, 5, 3, 1, 4, 2 },
            {5, 1, 5, 4, 3, 2 },
            {5, 2, 1, 5, 4, 3 },
            {5, 3, 2, 1, 5, 4 },
            {5, 4, 3, 2, 1, 5 },
            {5, 5, 4, 3, 2, 1 }};

    // Follows form function1, function2, function3, random_initialisation, depth_of_search & intensity_of_mutation.
    double[][] weights = {{1,2,5,10,0.5},{1,2,5,10,0.5},{1,2,5,10,0.5},{0.01,0.05,0.1,0.25,0.5},
            {0,0.2,0.4,0.6,0.8},{0,0.2,0.4,0.6,0.8}};

    int[] formula_1 = {25, 18,	15, 12, 10, 8, 6, 4, 2, 1};

    public int[][][] getResult() {
        return result;
    }

    public int getIteration() {
        return iteration;
    }

    public int getInstance() {
        return instance;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public void setResult(int[][][] result) {
        this.result = result;
    }

    public int[][] getTaguchiArray() {
        return taguchi_array;
    }

    public void setTaguchiArray(int[][] taguchi_array) {
        this.taguchi_array = taguchi_array;
    }

    public int getTaguchiIteration() {
        return taguchi_iteration;
    }
    public void iterateTaguchiIteration(){
        this.taguchi_iteration++;
    }

    public void setTaguchiIteration(int taguchi_iteration) {
        this.taguchi_iteration = taguchi_iteration;
    }

    public double[][] getWeights() {
        return weights;
    }

    public TaguchiParameterTuning() {
       System.out.println("Hi " +  getWeight(2));
    }

    /**
     * From the taguchi iteration, we get what weight we should pick specified by the weight_function index. From this,
     * we then go to the weights array and pick the actual value.
     * So, if the taguchi array stated '3' for weight index 0 (function 1), then it will return 5 with it being an index
     * of 2 in the weight array.
     * @param weight_function_index This specifies what function to choose a weight for
     * @return The actual weight for that specific function.
     */
    private double getWeight(int weight_function_index){
        return getWeights()[weight_function_index][getTaguchiArray()[getTaguchiIteration()][weight_function_index]-1];
    }

    /**
     * Starts the test on the specified problem domain.
     * @param file_name File name for the result to be saved to.
     */
    void startTest(String file_name, String[] instances) {
        // Creates the result array for use.
        setResult(new int[ITERATIONS][instances.length][taguchi_array.length]);

        int[] intermediary_result = new int[ITERATIONS];

        while(getTaguchiIteration() < taguchi_array.length){
            int[] taguchi_configuration = getTaguchiArray()[getTaguchiIteration()];


            // Create a hyper heuristic and problem domain
            Random rng = new Random(System.currentTimeMillis());
            Problem problem = new MinSetProblem(rng);

            // Create hyper heuristic.
            HyperHeuristic hyper_heuristic = new HyperHeuristicModifiedChoice(rng,
                getWeight(0),getWeight(1)
                ,getWeight(2),getWeight(3),
                getWeight(4),getWeight(5));

            System.out.println("Running hyper heuristic on setting {" + getWeight(0) + "," + getWeight(1)
                    + "," + getWeight(2) + "," + getWeight(3) + "," +
                    getWeight(4) + "," +getWeight(5) + "}");

            hyper_heuristic.applyHyperHeuristic(problem);
            int best = hyper_heuristic.getBestObjectiveValue();
            intermediary_result[getTaguchiIteration()] = best;
            System.out.print("Taguchi:  " + getTaguchiIteration() + ": " + best + " ,");
            iterateTaguchiIteration();
        }
    
    }
}
