package com.ai.problems.min_set;

import com.ai.Problem;
import com.ai.problems.min_set.enums.instance_reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.ai.problems.min_set.enums.instance_reader.*;


public class MinSetProblem implements Problem {

    private int[] solution_map;
    private int[] solution;
    private Random rng;
    // List of subsets that map edges.
    private final ArrayList< int[] > subsets = new ArrayList<>();
    // General data of loaded instance.
    // Note that the assumption is that every unique instance counts *up to* the
    // number of elements, ie numbers 1..X inclusive.
    private final Map<Enum<instance_reader>,Integer> data = new HashMap<>();

    public MinSetProblem(Random rng) {
        this.rng = rng;
    }

    private int[] combineEdges(int[] edges1, int[] edges2){
        int count =0;
        int[] combined = new int[edges1.length];
        for(int i : edges1){
            if(i == 1 || edges2[count] == 1)
                combined[count] = 1;
            count++;
        }
        return combined;
    }
    public void initialiseSolution(){
        int index = 0;
        for (int[] subset : subsets){
            double ran = rng.nextDouble();
            if(ran < 0.5){
                solution[index] = 1;
                solution_map = combineEdges(solution_map,subset);
            }
            index++;
        }
        System.out.println( "size " + solution.length + " " + Arrays.toString(solution));
        System.out.println( "size " + solution_map.length + " " + Arrays.toString(solution_map));
    }

    // Inserts values into a subset arraylist specified by a string array.
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

    // Loads a problem instance
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
            solution = new int[data.get(NumElements)];
            solution_map = new int[data.get(NumElements)];
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
