package org.example;

import org.example.parallelstream.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Utility class for generating all possible anagrams from a group of distinct letters.
 * Uses recursive backtracking algorithm with modern Java features.
 */
public class AnagramGeneratorRecursiveBacktrack {
    private static final Logger logger = LoggerFactory.getLogger(AnagramGeneratorRecursiveBacktrack.class);

    /**
     * Generates all possible anagrams from a set of distinct letters.
     *
     * @param letters the input string containing distinct letters
     * @return a list of all possible anagrams
     * @throws IllegalArgumentException if input is invalid (empty, contains non-letters, or has duplicates)
     */
    public static List<String> generateAnagrams(String letters) {
        validateInput(letters);

        List<String> result = new ArrayList<>();
        generateAnagramsRecursive("", letters, result);
        return result;
    }

    /**
     * Recursive helper method that uses backtracking to generate anagrams.
     *
     * @param current   the current permutation being built
     * @param remaining the remaining letters to be used
     * @param result    the list to store generated anagrams
     */
    private static void generateAnagramsRecursive(String current, String remaining, List<String> result) {
        if (remaining.isEmpty()) {
            result.add(current);
            return;
        }

        for (int i = 0; i < remaining.length(); i++) {
            String newCurrent = current + remaining.charAt(i);
            String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
            generateAnagramsRecursive(newCurrent, newRemaining, result);
        }
    }

    /**
     * Validates the input according to requirements.
     *
     * @param letters the input string to validate
     * @throws IllegalArgumentException if input is invalid
     */
    private static void validateInput(String letters) {
        if (letters == null || letters.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        if (!letters.chars().allMatch(Character::isLetter)) {
            throw new IllegalArgumentException("Input must contain only letters");
        }

        // Check for duplicates using Java 8+ streams
        boolean hasDuplicates = letters.chars()
                .distinct()
                .count() != letters.length();

        if (hasDuplicates) {
            throw new IllegalArgumentException("Input must contain distinct letters only");
        }
    }

    /**
     * Alternative implementation using Java 21+ features with records and pattern matching
     * for better readability (demonstrating modern Java features).
     */
    public static List<String> generateAnagramsModern(String letters) {
        validateInput(letters);

        return switch (letters.length()) {
            case 0 -> List.of();
            case 1 -> List.of(letters);
            default -> {
                var result = new ArrayList<String>();
                generateModern(letters, result);
                yield result;
            }
        };
    }

    private static void generateModern(String str, List<String> result) {
        if (str.length() == 1) {
            result.add(str);
            return;
        }

        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            String remaining = str.substring(0, i) + str.substring(i + 1);
            List<String> tempResult = new ArrayList<String>();
            generateModern(remaining, tempResult);

            // Use Java 21+ string templates for better readability
            for (String permutation : tempResult) {
                String s =currentChar+permutation;
                result.add(s);
            }
        }
    }

    /**
     * Main method for demonstration purposes.
     */
    public static void main(String[] args) {
        try {
            // Demonstrate the functionality
            var examples = List.of("abc", "ab", "a");

            for (String example : examples) {
                logger.info(String.format("Anagrams for '%s':",example));
                var anagrams = generateAnagrams(example);
                anagrams.forEach(System.out::println);
                System.out.println();
            }

        } catch (IllegalArgumentException e) {
            logger.info(String.format("Error: %s", e.getMessage()));
        }
    }
}