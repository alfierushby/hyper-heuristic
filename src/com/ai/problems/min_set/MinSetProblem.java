package com.ai.problems.min_set;

import com.ai.HeuristicClasses;
import com.ai.Problem;
import com.ai.problems.min_set.enums.InstanceReader;
import com.ai.problems.min_set.heuristics.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.ai.HeuristicClasses.Hill_Climbing;
import static com.ai.HeuristicClasses.Mutational;
import static com.ai.problems.min_set.Config.BACKUP_SOLUTION_INDEX;
import static com.ai.problems.min_set.Config.CURRENT_SOLUTION_INDEX;
import static com.ai.problems.min_set.enums.InstanceReader.*;


public class MinSetProblem implements Problem {

    // CONSTANTS
    private final double RANDOM_INTIALISATION = .01;
    private double depth_of_search = .1;
    private double intensity_of_mutation = 0.7;

    // Binary encoding of a solution, each index specifying the subset in subsets, 1 being selected, 0 not.
    private final ArrayList <Solution> solutions = new ArrayList<>();
    private final Random rng;
    // List of subsets that map edges.
    private final ArrayList< int[] > subsets = new ArrayList<>();
    // General data of loaded instance.
    // Note that the assumption is that every unique instance counts *up to* the
    // number of elements, ie numbers 1..X inclusive.
    private final Map<Enum<InstanceReader>,Integer> data = new HashMap<>();
    private Operations operations;

    private final Map<Enum<HeuristicClasses>, Heuristic[]> heurstics = new HashMap<>();


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
        return solutions.get(sol_index).getEvaluator();
    }

    /**
     * @return number of connected edges per node in array form
     */
    public int[] getSolutionMap(int index) {
        return getSolution(index).getSolutionMap();
    }

    /**
     * @return solution of problem in binary encoding form
     */
    public Solution getSolution(int index) {
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
    public Map<Enum<InstanceReader>, Integer> getData() {
        return data;
    }

    public int getNumberOfSubsets(){
        return getData().get(NumSubsets);
    }

    public Random getRng() {
        return rng;
    }


    /**
     * This gets the objective value of a solution. Note that it does not update the objective value.
     * @param sol_index Solution index to get objective value
     * @return objective value
     */
    public int getObjectiveValue(int sol_index){
        return getEvaluator(sol_index).getObjectiveValue();
    }
    @Override
    public Heuristic[] getHeuristics(HeuristicClasses h_class) {
        return heurstics.get(h_class);
    }

    private int caseConvertion(double val){
        if(val<0.2)
            return 1;
        else if (val<0.4)
            return 2;
        else if (val<0.6)
            return 3;
        else if (val<0.8)
            return 4;
        else if (val<1)
            return 5;
        else
            return 6;
    }

    /**
     * Converts depth of search value to number of iterations.
     * @return the corresponding iteration count to be done in hill climbing methods.
     */
    public int getDepthOfSearch() {
        return caseConvertion(depth_of_search);
    }

    /**
     * Converts depth of search value to number of mutations.
     * @return the corresponding iteration count to be done in mutation methods.
     */
    public int getIntensityOfMutation() {
        return caseConvertion(intensity_of_mutation);
    }

    public MinSetProblem(Random rng) {
        this.rng = rng;
        setOperations(new Operations(this));

        // Create Heuristic arrays for the Problem Domain
        IterableHeuristic[] mutations = {new BitMutation(this,getRng())};
        IterableHeuristic[] hill_climbing = {new DavisBitHC(this,getRng()),
                                    new SteepestDescentHC(this,getRng()),
                                    new FirstImprovementHC(this,getRng()),
                                    new RandomMutationHC(this,getRng())};

        // Add to Mapping
        heurstics.put(Mutational,mutations);
        heurstics.put(Hill_Climbing,hill_climbing);

    }

      //////////////////////////////
     // INITIALISATION FUNCTIONS //
    //////////////////////////////


    public void printInfo(int index){
        System.out.println( "size " + solutions.get(index).getSolutionData().length + " " + Arrays.toString(solutions.get(index).getSolutionData()));
        System.out.println( "size " + getSolution(index).getSolutionMap().length + " " + Arrays.toString(getSolution(index).getSolutionMap()));
        System.out.println( "Evaluation : " + getObjectiveValue(index) + " Infeasibility : " + getEvaluator(index).getUnaccountedElements());
    }

    private void insertSolution(Solution solution, int sol_index){

        // Set solution to arraylist memory
        solutions.add(sol_index,solution);
    }

    public void copySolution(int from, int to){
        Solution new_sol = getSolution(from).clone();

        if(solutions.size()<=to){
            System.out.println("Trying to copy solution to uninitialized memory!");
            return;
        }
        solutions.set(to,new_sol);
    }

    /**
     * Initialises the solution randomly.<br>
     * <b>Must be done before using the solution index.</b>
     */
    public void initialiseSolution(){
        int index = 0;
        // Initialise solution memory.
        Solution solution = new Solution(data.get(NumSubsets),data.get(NumElements));
        solution.getEvaluator().setObjectiveValue();

        // Create current solution:
        insertSolution(solution,CURRENT_SOLUTION_INDEX);
        boolean[] solution_data = solution.getSolutionData();

        for (int[] subset : subsets){
            double ran = rng.nextDouble();
            if(ran < RANDOM_INTIALISATION){
                solution_data[index] = true;
                getOperations().insertNode(subset,solution);
            }
            index++;
        }

        insertSolution(solution.clone(),BACKUP_SOLUTION_INDEX);

        // Initialise Objective Values
        getEvaluator(CURRENT_SOLUTION_INDEX).setObjectiveValue();

        System.out.println( "Evaluation : " + getEvaluator(CURRENT_SOLUTION_INDEX).getObjectiveValue() + " Infeasibility : "
                + getEvaluator(CURRENT_SOLUTION_INDEX).getUnaccountedElements());

        // Create backup solution:
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

