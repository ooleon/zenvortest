package org.example.parallelstream;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class ParallelPermutationProcessor implements ItemProcessor<String, List<String>> {

    @Autowired
    private PermutationItemReader reader;

    @Override
    public List<String> process(String item) throws Exception {
        List<String> elements = reader.getInputElements();
        int totalPermutations = factorial(elements.size());

        return IntStream.range(0, totalPermutations)
                .parallel()
                .mapToObj(index -> generateNthPermutation(elements, index))
                .map(permutation -> String.join(",", permutation))
                .toList();
    }

    private List<String> generateNthPermutation(List<String> elements, int index) {
        List<String> mutableElements = new ArrayList<>(elements);
        List<String> result = new ArrayList<>();
        int n = elements.size();
        int currentIndex = index;

        for (int i = n; i > 0; i--) {
            int fact = factorial(i - 1);
            int pos = currentIndex / fact;
            result.add(mutableElements.remove(pos));
            currentIndex %= fact;
        }

        return result;
    }

    private int factorial(int n) {
        if (n <= 1) return 1;
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}