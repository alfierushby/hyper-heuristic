package com.ai.problems.min_set;

import java.util.Arrays;
import java.util.function.IntBinaryOperator;

import static com.ai.problems.min_set.Config.BACKUP_SOLUTION_INDEX;
import static com.ai.problems.min_set.Config.CURRENT_SOLUTION_INDEX;

/**
 * Contains fundamental operations that are used by heuristics.
 */
public class Operations {
    private final MinSetProblem problem;

    public Operations(MinSetProblem problem) {
        this.problem = problem;
    }


    /**
     * @param bit_index Bit index to perform the bit flip in sol_index
     * @param sol_index The solution to perform the bit flip
     * @param save_index Where the solution is saved.
     */
    public void bitFlip(int bit_index, int sol_index, int save_index){
        // Perform a copy.
        if(sol_index!=save_index){
            problem.copySolution(sol_index,save_index);
        }

        boolean bit = !problem.getSolution(save_index).getSolutionData()[bit_index];
        Solution solution = problem.getSolution(save_index);

        solution.getSolutionData()[bit_index]=bit;

        if(bit){
            // Add to solution map.
            insertNode(problem.getSubsets().get(bit_index), solution);
        }else {
            // Bit is 0 so remove it from solution map.
            removeNode(problem.getSubsets().get(bit_index), solution);
        }

        // Perform Delta evaluation on solution.
        solution.getEvaluator().deltaObjectiveEvaluation(bit_index);
    }


    /**
     * @param bit_index Bit index to perform the bit flip in sol_index
     * @param sol_index The solution to perform the bit flip, saved there
     */
    public void bitFlip(int bit_index, int sol_index){
        // Perform a copy.
        bitFlip(bit_index,sol_index,sol_index);
    }

    /**
     * Swaps the specified bit of two solutions. Implements Delta Evaluation.
     * @param left Left solution
     * @param right Right solution
     * @param bit_index The bit to swap
     */
    public void exchangeBits(Solution left, Solution right, int bit_index){
        boolean left_prev = left.getSolutionData()[bit_index];
        boolean right_prev = right.getSolutionData()[bit_index];

        if(left_prev != right_prev){
            // Different, so equivalent to a bit flip on both.
            left.getSolutionData()[bit_index] = right_prev;
            right.getSolutionData()[bit_index] = left_prev;

            left.getEvaluator().deltaObjectiveEvaluation(bit_index);
            right.getEvaluator().deltaObjectiveEvaluation(bit_index);
        }

    }


    ////////////////////////////
    // SOLUTION MAP FUNCTIONS //
    ////////////////////////////

    /**
     * Generalised function to be used by insert and removal of nodes for public use. Internal function.
     * @param left_subset_map left array map, loops through to do an operation to the right array
     * @param right left array acts upon right array
     * @param operator operation that is executed on each '1' element in the left array to the right array
     */
    private void operatorNode(int[] left_subset_map, Solution right, IntBinaryOperator operator){
        int count =0;
        int[] map_right = right.getSolutionMap();
        SolutionEvaluator eval = right.getEvaluator();

        for(int i : left_subset_map){
            if(i == 1){
                int prev = map_right[count];
                map_right[count] = operator.applyAsInt(map_right[count],1);
                // Evaluate unaccounted elements. Add 1 if node made from >0 to 0.
                if(prev>0&&map_right[count]==0){
                  //  System.out.println("hi?" + eval.getUnaccountedElements());
                    eval.setUnaccountedElements(eval.getUnaccountedElements()+1);
                } else if(prev==0&&map_right[count]>0){
                    //System.out.println("lol?" + eval.getUnaccountedElements());
                    eval.setUnaccountedElements(eval.getUnaccountedElements()-1);
                }
            }
            count++;
        }
    }

    /**
     * Combines two arrays, returning result. Acts as an insertion of one subset to another, culminating edges.
     * Formally left + right.
     * @param left_subset_map subset that will be inserted
     * @param right Solution that will be inserted to
     */
    public void insertNode(int[] left_subset_map, Solution right){
        operatorNode(left_subset_map,right,  (a, b) -> a + b);
    }

    /**
     * Combines two arrays, returning result. Acts as a removal of one subset to another, removing edges.
     * formally right - left
     * @param toRemove subset that will be removed
     * @param source Solution that will have contents of toRemove removed
     */
    public void removeNode(int[] toRemove, Solution source){
        operatorNode(toRemove,source,  (a, b) -> a - b);
    }


}
