package com.ai.problems.min_set;

import com.ai.Problem;
import com.ai.problems.min_set.enums.instance_reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.IntBinaryOperator;

import static com.ai.problems.min_set.enums.instance_reader.*;


public class MinSetProblem implements Problem {

    // CONSTANTS
    private final double RANDOM_INTIALISATION = .2;

    // Represented using Arrays as most efficient data structure with known size.
    // Stores number of times a node has a connected edge.
    private ArrayList <int[]> solution_maps = new ArrayList<>();
    // Binary encoding of a solution, each index specifying the subset in subsets, 1 being selected, 0 not.
    private ArrayList <int[]> solutions = new ArrayList<>();
    private final Random rng;
    // List of subsets that map edges.
    private final ArrayList< int[] > subsets = new ArrayList<>();
    // General data of loaded instance.
    // Note that the assumption is that every unique instance counts *up to* the
    // number of elements, ie numbers 1..X inclusive.
    private final Map<Enum<instance_reader>,Integer> data = new HashMap<>();
    private final ArrayList <SolutionEvaluator> evaluators = new ArrayList<>();
    private Operations operations;

    public Operations getOperations() {
        return operations;
    }

    public void setOperations(Operations operations) {
        this.operations = operations;
    }

    /**
     * @param sol_index specifies what solution you want to evaluate
     * @return a solution evaluator for the specific solution index
     */
    public SolutionEvaluator getEvaluator(int sol_index) {
        return evaluators.get(sol_index);
    }

    /**
     * @return number of connected edges per node in array form
     */
    public int[] getSolutionMap(int index) {
        return solution_maps.get(index);
    }

    /**
     * @return solution of problem in binary encoding form
     */
    public int[] getSolution(int index) {
        return solutions.get(index);
    }

    /**
     * @return subsets of problem
     */
    public ArrayList<int[]> getSubsets() {
        return subsets;
    }

    /**
     * @return metadata of problem
     */
    public Map<Enum<instance_reader>, Integer> getData() {
        return data;
    }

    /**
     * This gets the objective value of a solution. Note that it does not update the objective value.
     * @param sol_index Solution index to get objective value
     * @return objective value
     */
    public int getObjectiveValue(int sol_index){
        return evaluators.get(sol_index).getObjectiveValue();
    }

    public MinSetProblem(Random rng) {
        this.rng = rng;
        setOperations(new Operations(this));
    }



      //////////////////////////////
     // INITIALISATION FUNCTIONS //
    //////////////////////////////


    public void printInfo(int index){
        System.out.println( "size " + solutions.get(index).length + " " + Arrays.toString(solutions.get(index)));
        System.out.println( "size " + solution_maps.get(index).length + " " + Arrays.toString(solution_maps.get(index)));
        System.out.println( "Evaluation : " + getObjectiveValue(index) + " Infeasibility : " + getEvaluator(index).getUnaccountedElements());
    }

    /**
     * Initialises the solution randomly.<br>
     * <b>Must be done before using the solution index.</b>
     * @param sol_index solution index to be inititiated
     */
    public void initialiseSolution(int sol_index){
        int index = 0;
        // Initialise solution memory.
        int[] solution = new int[data.get(NumSubsets)];
        int[] solution_map = new int[data.get(NumElements)];
        // Set evaluator for the solution
        evaluators.add(sol_index,new SolutionEvaluator(this,solution,solution_map));

        for (int[] subset : subsets){
            double ran = rng.nextDouble();
            if(ran < RANDOM_INTIALISATION){
                solution[index] = 1;
                getEvaluator(sol_index).insertNode(subset,solution_map);
            }
            index++;
        }
        System.out.println( "size " + solution.length + " " + Arrays.toString(solution));
        System.out.println( "size " + solution_map.length + " " + Arrays.toString(solution_map));


        System.out.println( "Evaluation : " + getEvaluator(sol_index).getObjectiveValue() + " Infeasibility : "
                + getEvaluator(sol_index).getUnaccountedElements());

        // Set solution to arraylist memory
        solutions.add(sol_index,solution);
        solution_maps.add(sol_index,solution_map);
    }


    /**
     * Inserts values into a subset arraylist specified by a string array.
     * @param index subset index in the subsets array
     * @param values values to be inserted into the subset
     * @return number of element edges inserted into subset
     */
    private int insertSubsetValues(int index, String[] values){
        // First get the arraylist to insert the values to.
        int[] subset;
        int inserted =0;
        if(index >= subsets.size()){
            // No index. Create.
            subset = new int[data.get(NumElements)];
            subsets.add(subset);
        } else {
            subset = subsets.get(index);
        }

        // Now loop through all the array values.
        for(String s : values){
            if (!Objects.equals(s, "")) {
                int i = Integer.parseInt(s);
                // System.out.println(i);
                subset[i-1] = 1;
                inserted++;
            }

        }
        // System.out.println( "size " + subset.size() + " " + subset.toString());
        // Return number of elements inserted.
        return inserted;
    }


    /**
     * Loads a problem instance
     * @param Path path of problem instance, txt form.
     */
    @Override
    public void loadInstance(String Path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Path))) {
            String line = reader.readLine();
            // Stack to keep track of execution
            String[] arr;
            int current_subset_i=0, insert_limit=1;

            while(line != null){
                // Split into parts.
                arr = line.split(" ");

                // Do simple Parsing. First case is m n form.
                if( data.isEmpty()){
                    data.put(NumSubsets,Integer.parseInt(arr[1]));
                    data.put(NumElements,Integer.parseInt(arr[2]));
                }

                // Second case, check size 1 and NumSubsets and NumElements are filled.
                else if(data.size() == 2){
                    data.put(SizeOfSubset,Integer.valueOf(arr[1]));
                    insert_limit = data.get(SizeOfSubset);
                }

                // 3rd case, check if possible to insert data.
                else if(arr.length !=0 && data.size() == 3 && insert_limit > 0){
                    int num_inserted = insertSubsetValues(current_subset_i,arr);
                    insert_limit -= num_inserted; // Minus the items to insert.
                }

                // If we're done inserting subsets, clear the data.
                if(insert_limit <=0){
                    data.remove(SizeOfSubset);
                    current_subset_i++;
                }

                // Read next line.
                line = reader.readLine();
            }

        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}

