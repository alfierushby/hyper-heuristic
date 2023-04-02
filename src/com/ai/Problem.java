package com.ai;

// A standard interface for any problem domain. Acted upon by the
// hyper heuristic.
public interface Problem {

    // Loads a problem instance specified by a path string.
    void loadInstance(String Path);


}
