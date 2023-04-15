package com.ai;

public interface HyperHeuristic {
    /**
     * Applies the hyper heuristic to some implement problem domain following {@link Problem}'s interface.
     * @param problem_domain The problem domain to apply the hyper heuristic to.
     */
    public void applyHyperHeuristic(Problem problem_domain);
}
