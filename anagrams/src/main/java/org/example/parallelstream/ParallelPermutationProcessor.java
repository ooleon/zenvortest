package org.example.parallelstream;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class ParallelPermutationProcessor implements ItemProcessor<String, List<String>> {

    @Value("${parallelProcessor.elements:1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40}")
    String string;

    @Autowired
    private PermutationItemReader reader;

    @Override

    /*
    * Process that invoke the permutation in parallel.
     * */
    public List<String> process(String item) throws Exception {
        List<String> elements = reader.getInputElements();
        //        List<String> elements = Arrays.asList(string.split(","));
        int totalPermutations = FactorialUtil.factorial(elements.size());

        return IntStream.range(0, totalPermutations)
                .parallel() //this parallelizes the stream.
                .mapToObj(index -> generateNthPermutation(elements, index))
                .map(permutation -> String.join(",", permutation))
                .peek((s -> {
                    System.out.print(s + " ");
                }))
                .peek((s -> {
                    System.out.println();
                }))
                .toList();
    }

    /**
     * Generates the n-th lexicographical permutation of a given list of elements
     * using the factorial number system (factoradic) approach.
     *
     * This method implements an efficient algorithm to directly compute any specific
     * permutation by index without generating all previous permutations.
     *
     * Time Complexity: O(n²) where n is the number of elements
     * Space Complexity: O(n) for storing the result and mutable copy
     *
     * Algorithm Overview:
     * 1. The factorial number system allows representing any integer as a sum of factorial terms
     * 2. Each digit in the factoradic representation indicates the position to select from remaining elements
     * 3. The method sequentially selects elements based on the factoradic decomposition of the index
     *
     * Mathematical Basis:
     * For n elements, there are n! possible permutations
     * The index (0 ≤ index < n!) uniquely identifies a specific permutation
     * The algorithm uses: position = index / (n-1)! then index = index % (n-1)!
     *
     * @param elements The list of distinct elements to permute (should not contain duplicates)
     * @param index The zero-based index of the desired permutation (0 ≤ index < n!)
     * @return The n-th lexicographical permutation as a List<String>
     * @throws IllegalArgumentException if index is out of bounds (index < 0 or index ≥ n!)
     * @throws NullPointerException if elements is null or contains null values
     */
    private List<String> generateNthPermutation(List<String> elements, int index) {
        List<String> mutableElements = new ArrayList<>(elements);
        List<String> result = new ArrayList<>();
        int n = elements.size();
        int currentIndex = index;

        for (int i = n; i > 0; i--) {
            int fact = FactorialUtil.factorial(i - 1);
            int pos = currentIndex / fact;

            /**
             * Here is where magic append. The remove method shifts any subsequent elements to the left, return it
             * and finaly the add method put it in the last position.
            */
             result.add(mutableElements.remove(pos));

            currentIndex %= fact;
        }

        return result;
    }

}