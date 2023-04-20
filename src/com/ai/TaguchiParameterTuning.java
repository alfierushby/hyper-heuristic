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
    private int taguchi_iteration = 0, iteration = 0;

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

    int[] formula_1 = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    String[] instances = {"src/test_instances/d1_50_500.txt","src/test_instances/d2_50_500.txt",
            "src/test_instances/d3_511_210.txt","src/test_instances/d4_2047_495.txt"};

    public int[][][] getResult() {
        return result;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public String[] getInstances() {
        return instances;
    }

    public void setResult(int[][][] result) {
        this.result = result;
    }

    public int[][] getTaguchiArray() {
        return taguchi_array;
    }

    public int getTaguchiIteration() {
        return taguchi_iteration;
    }
    public void iterateTaguchiIteration(){
        this.taguchi_iteration++;
    }

    public void setTaguchIteration(int taguchi_iteration) {
        this.taguchi_iteration = taguchi_iteration;
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
    void startTest(String file_name) {
        // Creates the result array for use.
        setResult(new int[ITERATIONS][getInstances().length][taguchi_array.length]);

        Integer[] intermediary_result = new Integer[taguchi_array.length];
        long seed;

        // Make a scores array, where scores[i][j][k] correspond for the ith variable weight being modified, and j
        // represents the scores of said weight when set to the index in weights.
        // So, for i=0, pick {1,2,5,10,0.5}, where j=1 corresponds to all the scores claimed
        // when variable 1 had a value of 2.
        // k=0 corresponds to the total score, and k=1 corresponds to the number of scores added.
        int scores[][][] = new int[getWeights().length][getWeights()[0].length][2];

        // Go through each iteration
        while(getIteration() < ITERATIONS) {

            // Seed is consistent for each iteration.
            seed = System.currentTimeMillis();

            // For each iteration go through each instance
            for(int instance=0; instance<getInstances().length; instance++){

                //Set taguchi iteration to 0, so we start on the first configuration for this instance and iteration.
                setTaguchIteration(0);

                System.out.println("/////////////////////////////////////////////////////////");
                while(getTaguchiIteration() < taguchi_array.length){
                    int[] taguchi_configuration = getTaguchiArray()[getTaguchiIteration()];


                    // Create a hyper heuristic and problem domain
                    Random rng= new Random(seed);
                    Problem problem = new MinSetProblem(rng);
                    problem.loadInstance(getInstances()[instance]);

                    // Create hyper heuristic.
                    HyperHeuristic hyper_heuristic = new HyperHeuristicModifiedChoice(rng,
                            getWeight(0),getWeight(1)
                            ,getWeight(2),getWeight(3),
                            getWeight(4),getWeight(5));

                    System.out.println("Running hyper heuristic on setting {" + getWeight(0) + ","
                            + getWeight(1) + "," + getWeight(2) + ","
                            + getWeight(3) + "," + getWeight(4) + ","
                            + getWeight(5) + "}");

                    hyper_heuristic.applyHyperHeuristic(problem);
                    int best = hyper_heuristic.getBestObjectiveValue();
                    intermediary_result[getTaguchiIteration()] = best;
                    System.out.println("Taguchi:  " + getTaguchiIteration() + ": " + best);
                    iterateTaguchiIteration();
                }

                // Now sort the index array, ie get an array of form {9,14,...} where intermediary_result[9]
                // is the highest value in the array.
                Integer[] index_array = new Integer[taguchi_array.length];
                // First create an index array
                for(int i = 0; i< taguchi_array.length; i++){
                    index_array[i]=i;
                }
                Arrays.sort(index_array, (a,b) ->{
                    return  intermediary_result[b].compareTo(intermediary_result[a]); // Descending order, b vs a.
                });

                //Get the array to set.
                int[] array = getResult()[getIteration()][instance];

                // Assign the top 10 with a score in the data array. The rest will be 0.
                // Recall that each iteration, for an instance, has an array where each index corresponds to a taguchi
                // configuration.
                for(int i = 0; i< formula_1.length; i++){
                    array[index_array[i]] = formula_1[i];
                }
                System.out.println("Formula 1 Scores for " + getIteration() + " iteration, instance " + instance + ", "
                        + Arrays.toString(array));

                // Add scores. Ie, go through each taguchi configuration, and index the scores array to add the scores
                // for each variable setting.
                for(int i = 0; i<getTaguchiArray().length; i++){
                    // Get the taguchi configuration (index version)
                    int[] taguchi_config = getTaguchiArray()[i]; // Note the index should be offset by -1.
                    int score = array[i]; // Score of this taguchi configuration.
                    // Add score to the corresponding variables.
                    for(int index = 0; index<taguchi_config.length; index++){
                        // Get the setting, 0-4.
                        int taguchi_setting = taguchi_config[index]-1;
                        // index corresponds to the weight, and taguchi setting describes what the weight was set to
                        // to get that score.
                        scores[index][taguchi_setting][0]+=score;
                        // Increment the counter for number of scores summed.
                        scores[index][taguchi_setting][1]++;
                    }

                }

                System.out.println("/////////////////////////////////////////////////////////");

            }
            setIteration(getIteration()+1);
        }

        int num_weights = getWeights()[0].length;
        // For each weight, 0 corresponds to the best variable config, and 1 is the average score it had.
        double[][] best_variables = new double[num_weights][2];
        // Now go through each variable and find the one with the highest score.
        for(int i = 0; i<getWeights().length; i++) {
          // Where i is the variable name, go through each weight.
          double[] weights = getWeights()[i];
          // 0 corresponds to the variable config, and 1 corresponds to the average score.
          double[] best_average_pair = new double[2];
          for(int j = 0; j<num_weights; j++){
              // Where j is the weight configuration. Ie, i=0,j=0 states that from weight 0, {1,2,5,10,0.5}, what was
              // its score when it was equal to '1'.
              int[] score = scores[i][j];
              double avg = (double) score[0] /score[1];
              if(avg>best_average_pair[0]){
                  best_average_pair[0]=j;
                  best_average_pair[1]=avg;

              }
          }
          // Set the best average pair to the weight i.
          best_variables[i] = best_average_pair;
        }
        int var = 0;
        for(double[] pair : best_variables){
            System.out.println("Weight: " + var + ", Best Setting: " + getWeights()[var][(int) pair[0]] + " With Average: "
            + pair[1]);
        }

    }




}
