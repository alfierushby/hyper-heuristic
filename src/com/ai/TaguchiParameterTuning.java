package com.ai;

import com.ai.problems.min_set.MinSetProblem;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Performs a L25 taguchi array test. Does 30 trials on each instance for 20 seconds, averaging the resulting best objective
 * value. Variables to modify are:
 * <br><br>
 * <b>Phi Weight</b>: {0.2,0.4,0.6,0.8,0.99}
 * <br>
 * <b>Function 1 Weight</b>: {0.2,0.4,0.6,0.8,1}
 * <br>
 * <b>Function 2 Weight</b>:  {0.2,0.4,0.6,0.8,1}
 * <br>
 * <b>Function 3 Weight</b>: {0.2,0.4,0.6,0.8,1}
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

    // Expected use of taguchi array, shuffled to improve experiment reliability.
    ArrayList<int[]> taguchi_shuffled;

    // Follows form PHI_WEIGHT, Function_1_weight, Function_2_weight, Function_3_weight, depth_of_search & intensity_of_mutation.
    double[][] weights = {{0.2,0.4,0.6,0.8,0.99}, {0.2,0.4,0.6,0.8,1}, {0.2,0.4,0.6,0.8,1}, {0.2,0.4,0.6,0.8,1},
            {0,0.2,0.4,0.6,0.8},{0,0.2,0.4,0.6,0.8}};

    int[] formula_1 = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    String[][] instances = {{"src/test_instances","d1_50_500"},{"src/test_instances" ,"d2_50_500"}};

    public int[][][] getResult() {
        return result;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public String[][] getInstances() {
        return instances;
    }

    public void setResult(int[][][] result) {
        this.result = result;
    }

    public int[][] getTaguchiArray() {
        return taguchi_array;
    }

    /**
     * This is the expected use case of the taguchi array. Do not use the default version!
     * @return Shuffled taguchi array, should be set before test starts.
     */
    public ArrayList<int[]> getTaguchiShuffled() {
        return taguchi_shuffled;
    }

    public void setTaguchiShuffled(ArrayList<int[]> taguchi_shuffled) {
        this.taguchi_shuffled = taguchi_shuffled;
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


    /**
     * From the taguchi iteration, we get what weight we should pick specified by the weight_function index. From this,
     * we then go to the weights array and pick the actual value.
     * So, if the taguchi array stated '3' for weight index 0 (phi_weight), then it will return 5 with it being an index
     * of 2 in the weight array.
     * @param weight_function_index This specifies what function to choose a weight for
     * @return The actual weight for that specific function.
     */
    private double getWeight(int weight_function_index){
        return getWeights()[weight_function_index][getTaguchiShuffled().get(getTaguchiIteration())[weight_function_index]-1];
    }

    private ArrayList<int[]> createRandomPermutation(int[][] array){
        Random shuffle_ran = new Random(System.currentTimeMillis());
        ArrayList<int[]> to_shuffle = new ArrayList<>(Arrays.asList(array));
        int len = to_shuffle.size();
        for(int i = 0; i<len; i++){
            int ran = shuffle_ran.nextInt(0,len);
            int[] temp_v = to_shuffle.get(i);
            to_shuffle.set(i,to_shuffle.get(ran));
            to_shuffle.set(ran, temp_v);
        }
        return to_shuffle;
    }


    /**
     * Starts the test on the specified problem domain.
     */
    void startTest() {
        // Creates the result array for use.
        setResult(new int[ITERATIONS][getInstances().length][getTaguchiArray().length]);

        Integer[] intermediary_result = new Integer[getTaguchiArray().length];
        long seed;

        // Make a scores array, where scores[i][j][k] correspond for the ith weight being modified, and j
        // represents the scores of said weight when set to the index in weights.
        // So, for i=0, pick {1,2,5,10,0.5}, where j=1 corresponds to all the scores claimed
        // when variable 1 had a value of 2.
        // k=0 corresponds to the total score, and k=1 corresponds to the number of scores added.
        int scores[][][] = new int[getWeights().length][getWeights()[0].length][2];


        // Go through each iteration
        while(getIteration() < ITERATIONS) {

            //Get shuffled taguchi array
            setTaguchiShuffled(createRandomPermutation(getTaguchiArray()));

            // Seed is consistent for each iteration.
            seed = System.currentTimeMillis();

            // For each iteration go through each instance
            for(int instance=0; instance<getInstances().length; instance++){

                //Set taguchi iteration to 0, so we start on the first configuration for this instance and iteration.
                setTaguchIteration(0);

                System.out.println("/////////////////////////////////////////////////////////");
                while(getTaguchiIteration() < getTaguchiShuffled().size()){
                    int[] taguchi_configuration = getTaguchiShuffled().get(getTaguchiIteration());


                    // Create a hyper heuristic and problem domain
                    Random rng= new Random(seed);
                    Problem problem = new MinSetProblem(rng);
                    problem.loadInstance(getInstances()[instance][0],getInstances()[instance][1]);

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
                Integer[] index_array = new Integer[getTaguchiShuffled().size()];
                // First create an index array
                for(int i = 0; i< getTaguchiShuffled().size(); i++){
                    index_array[i]=i;
                }
                // Ascending order, where the smallest value is the best.
                Arrays.sort(index_array, Comparator.comparing(a -> intermediary_result[a]));

                //Get the array to set.
                int[] array = getResult()[getIteration()][instance];

                // Assign the top 10 with a score in the data array. The rest will be 0.
                // Recall that each iteration, for an instance, has an array where each index corresponds to a taguchi
                // configuration.
                int formula_1_index = 0;
                for(int i = 0; i< array.length; i++){
                    array[index_array[i]] = formula_1[formula_1_index];
                    if(i+1< array.length && !Objects.equals(intermediary_result[index_array[i]], intermediary_result[index_array[i+1]]))
                        formula_1_index++;
                }
                System.out.println("Formula 1 Scores for " + getIteration() + " iteration, instance " + instance + ", "
                        + Arrays.toString(array));

                // Add scores. Ie, go through each taguchi configuration, and index the scores array to add the scores
                // for each variable setting.
                for(int i = 0; i<getTaguchiShuffled().size(); i++){
                    // Get the taguchi configuration (index version)
                    int[] taguchi_config = getTaguchiShuffled().get(i); // Note the index should be offset by -1.
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
        // For each weight, 0 corresponds to the best variable config, and 1 is the average score it had.
        double[][] best_variables = new double[getWeights().length][2];
        // Now go through each weight and find the one with the highest score.
        for(int i = 0; i<scores.length; i++) {
          // 0 corresponds to the variable config, and 1 corresponds to the average score.
          double[] best_average_pair = new double[2];
          best_average_pair[0]= (double) scores[i][0][0] /scores[i][0][1];
          for(int j = 0; j<scores[i].length; j++){
              // Where j is the weight configuration. Ie, i=0,j=0 states that from weight 0, {1,2,5,10,0.5}, what was
              // its score when it was equal to '1'.
              int[] score = scores[i][j];
              double avg = (double) score[0] /score[1];
              System.out.println("Score " + score[0] + " num " + score[1] + "avg " +avg);
              if(avg>best_average_pair[0]){
                  best_average_pair[0]=avg;
                  best_average_pair[1]=j;

              }
          }
          // Set the best average pair to the weight i.
          best_variables[i] = best_average_pair;
        }
        int var = 0;
        for(double[] pair : best_variables){
            System.out.println("Weight: " + var + ", Best Setting: " + getWeights()[var][(int) pair[1]] + " With Average: "
            + pair[0]);
            var++;
        }

    }




}
