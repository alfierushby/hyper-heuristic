package com.ai;

/**
 * Contains configuration variables that are used in problem domains. Exclusive to a hyper heuristic, but can be modified
 * for parameter tuning for a hyper heuristic.
 */
public class Config {
    /**
     * Probability for a bit for be true when initialisation a solution. 0-1.
     */
    public static double RANDOM_INTIALISATION = 0.5;
    /**
     * Number of iterations on a depth of search heuristic. 0-1.
     */
    public static double DEPTH_OF_SEARCH = 0;
    /**
     * Number of iterations on a mutation search. 0-1.
     */
    public static double INTENSITY_OF_MUTATION =0;
    /**
     * Set to true when you want the hyper heuristic to print its state on every iteration.
     */
    public static final boolean DEBUG = false;
    /**
     * Length of each run/trial in milliseconds.
     */
    public static final long HYPER_HEURISTIC_TRIAL_TIME = 20*1000;
}
