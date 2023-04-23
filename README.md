# Using the hyper-heuristic.
The hyper-heuristic can be run via 3 different methods. You can directly run it by creating it, run it via the 'FileOutput' class, or perform parameter tuning with the 'TaguchiParameterTuning' class.

## DEBUG Mode
If you require the hyper heuristic to output to the console with its implementation specific information (function values, etc) set
DEBUG to true in the Config class.

## Directly Running
To directly run the hyper heuristic, the following code will work:
             
    Random ran = new Random(123456789);
    MinSetProblem problem = new MinSetProblem(ran);
    problem.loadInstance("src/test_instances","d4_2047_495");
    HyperHeuristicModifiedChoice hyper_heuristic = new HyperHeuristicModifiedChoice(ran);
    hyper_heuristic.applyHyperHeuristic(problem);

This will load the 4th instance, and the results/running values will be displayed in the console. 

## Running via the 'FileOutput' class
To do this, simple run:

    FileOutput runner = new FileOutput();
    runner.run("d2_50_500");

This will output the results of the hyper heuristic in 6 files (5 for the 5 trials, and 1 outlining the best solutions conjured), in the root folder. It is set to do 5 trials on 5 seeds.

## Running via the 'TaguchiParameterTuning' class
To do this, run:

    TaguchiParameterTuning test = new TaguchiParameterTuning();
    test.startTest();

It is recommended to set DEBUG to false to see the results of the tuning. Modify the hyper heuristic termination conditions in its class for differing run lengths.
