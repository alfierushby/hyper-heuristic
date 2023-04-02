package com.ai.problems.min_set;

import com.ai.Problem;
import com.ai.problems.min_set.enums.instance_reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.ai.problems.min_set.enums.instance_reader.*;


public class MinSetProblem implements Problem {



    Map<Integer, Boolean> language = new HashMap<>();
    ArrayList< ArrayList<Integer> > subsets = new ArrayList<>();

    private int insertSubsetValues(int index, String[] values){
        // First get the arraylist to insert the values to.
        ArrayList<Integer> subset;
        int inserted =0;
        if(index >= subsets.size()){
            // No index. Create.
            subset = new ArrayList<>();
            subsets.add(subset);
        } else {
            subset = subsets.get(index);
        }

        // Now loop through all the array values.
        for(String s : values){
            if (!Objects.equals(s, "")) {
                int i = Integer.parseInt(s);
               // System.out.println(i);
                subset.add(i);
                inserted++;
            }

        }
        //System.out.println( "size " + subset.size() + " " + subset.toString());
        // Return number of elements inserted.
        return inserted;
    }


    @Override
    public void loadInstance(String Path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Path))) {
            String line = reader.readLine();
            // Stack to keep track of execution
            Map<Enum<instance_reader>,Integer> data = new HashMap<>();
            String[] arr;
            int current_subset_i=0, insert_limit=1;

            while(line != null){
                // Split into parts.
                arr = line.split(" ");


                // Do simple Parsing. First case is m n form.
                if( data.isEmpty()){
                    data.put(NumSubsets,Integer.valueOf(arr[1]));
                    data.put(NumElements,Integer.valueOf(arr[2]));
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
