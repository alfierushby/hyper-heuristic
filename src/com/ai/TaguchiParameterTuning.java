package com.ai;

import java.util.ArrayList;

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

    /**
     * Of form data[i][j][k], where i represents the trial, j represents the instance, and k represents the (formula 1)
     * score for each configuration.
     * Ie, we run every configuration once on a specific seed, for some instance, and then rank them from best to worst,
     * formula 1 style, and do these for every instance.
     * This will be done 30 times, with 4 instances each having 25 configurations.
     */
    int[][][] result = new int[30][4][25];

    int[][] taguchi_array = new int[][] {{1,1,1,1,1,1},
            {1,	2,	2,	2,	2,	2 },
            {1,	3,	3,	3,	3,	3 },
            {1,	4,	4,	4,	4,	4 },
            {1,	5,	5,	5,	5,	5 },
            {2,	1,	2,	3,	4,	5 },
            {2,	2,	3,	4,	5,	1 },
            {2,	3,	4,	5,	1,	2 },
            {2,	4,	5,	1,	2,	3 },
            {2,	5,	1,	2,	3,	4 },
            {3,	1,	3,	5,	2,	4 },
            {3,	2,	4,	1,	3,	5 },
            {3,	3,	5,	2,	4,	1 },
            {3,	4,	1,	3,	5,	2 },
            {3,	5,	2,	4,	1,	3 },
            {4,	1,	4,	2,	5,	3 },
            {4,	2,	5,	3,	1,	4 },
            {4,	3,	1,	4,	2,	5 },
            {4,	4,	2,	5,	3,	1 },
            {4,	5,	3,	1,	4,	2 },
            {5,	1,	5,	4,	3,	2 },
            {5,	2,	1,	5,	4,	3 },
            {5,	3,	2,	1,	5,	4 },
            {5,	4,	3,	2,	1,	5 },
            {5,	5,	4,	3,	2,	1 }};

    // Follows form function1, function2, function3, random_initialisation, depth_of_search & intensity_of_mutation.
    double[][] weights = {{1,2,5,10,0.5},{1,2,5,10,0.5},{1,2,5,10,0.5},{0.01,0.05,0.1,0.25,0.5},
            {0,0.2,0.4,0.6,0.8},{0,0.2,0.4,0.6,0.8}};
}
