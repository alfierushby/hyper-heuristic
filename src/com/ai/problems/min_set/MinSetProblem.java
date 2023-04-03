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
    private int[] solution_map;
    // Binary encoding of a solution, each index specifying the subset in subsets, 1 being selected, 0 not.
    private int[] solution;
    private final Random rng;
    // List of subsets that map edges.
    private final ArrayList< int[] > subsets = new ArrayList<>();
    // General data of loaded instance.
    // Note that the assumption is that every unique instance counts *up to* the
    // number of elements, ie numbers 1..X inclusive.
    private final Map<Enum<instance_reader>,Integer> data = new HashMap<>();
    private SolutionEvaluator evaluator;
    private Operations operations;

    public Operations getOperations() {
        return operations;
    }

    public void setOperations(Operations operations) {
        this.operations = operations;
    }

    public SolutionEvaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(SolutionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    /**
     * @return number of connected edges per node in array form
     */
    public int[] getSolutionMap() {
        return solution_map;
    }

    /**
     * @return solution of problem in binary encoding form
     */
    public int[] getSolution() {
        return solution;
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

    public MinSetProblem(Random rng) {
        this.rng = rng;
        setEvaluator(new SolutionEvaluator(this));
        setOperations(new Operations(this));
    }


      //////////////////////////////
     // INITIALISATION FUNCTIONS //
    //////////////////////////////

    /**
     * Generalised function to be used by insert and removal of nodes for public use. Internal function.
     * @param left left array, loops through to do an operation to the right array
     * @param right left array acts upon right array
     * @param operator operation that is executed on each '1' element in the left array to the right array
     */
    private void operatorNode(int[] left, int[] right, IntBinaryOperator operator){
        int count =0;
        for(int i : left){
            if(i == 1)
                right[count] = operator.applyAsInt(right[count],1);
            count++;
        }
    }

    /**
     * Combines two arrays, returning result. Acts as an insertion of one subset to another, culminating edges.
     * Formally left + right.
     * @param left subset that will be inserted
     * @param right subset that will be inserted to
     */
    public void insertNode(int[] left, int[] right){
        operatorNode(left,right,  (a, b) -> a + b);
    }

    /**
     * Combines two arrays, returning result. Acts as a removal of one subset to another, removing edges.
     * formally right - left
     * @param toRemove subset that will be removed
     * @param source subset that will have contents of toRemove removed
     */
    public void removeNode(int[] toRemove, int[] source){
        operatorNode(toRemove,source,  (a, b) -> a - b);
    }


    public void printInfo(){
        System.out.println( "size " + solution.length + " " + Arrays.toString(solution));
        System.out.println( "size " + solution_map.length + " " + Arrays.toString(solution_map));
        System.out.println( "Evaluation : " + getEvaluator().getObjectiveValue() + " Infeasibility : " + getEvaluator().getUnaccountedElements());
    }

    /**
     * Initialises the solution randomly
     */
    public void initialiseSolution(){
        int index = 0;
        for (int[] subset : subsets){
            double ran = rng.nextDouble();
            if(ran < RANDOM_INTIALISATION){
                solution[index] = 1;
                insertNode(subset,solution_map);
            }
            index++;
        }
        System.out.println( "size " + solution.length + " " + Arrays.toString(solution));
        System.out.println( "size " + solution_map.length + " " + Arrays.toString(solution_map));

        getEvaluator().setObjectiveValue();
        System.out.println( "Evaluation : " + getEvaluator().getObjectiveValue() + " Infeasibility : " + getEvaluator().getUnaccountedElements());
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

            // Initialise solution memory.
            solution = new int[data.get(NumSubsets)];
            solution_map = new int[data.get(NumElements)];
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}

