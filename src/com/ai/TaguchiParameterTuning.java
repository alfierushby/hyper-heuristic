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
 * <b>Intensity of Mutation</b>: {0,0.2,0.4,0.6,0.8
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


}
