package org.example;


import java.util.*;
        import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Anagram Generator Utility
 *
 * This utility class provides functionality to generate all possible anagrams
 * from a given set of distinct letters using modern Java features.
 *
 * @author Expert Java Developer
 * @version Java 24
 */
public class AnagramGeneratorRecursivePermutation {

    /**
     * Generates all possible anagrams from the given input string.
     * Uses recursion with backtracking to generate permutations.
     *
     * @param input The input string containing distinct letters
     * @return A List of all possible anagrams
     * @throws IllegalArgumentException if input is invalid
     */
    public static List<String> generateAnagrams(String input) {
        // Validate input using pattern matching (Java 17+)
        var validationResult = validateInput(input);

        return switch (validationResult) {
            case ValidationResult.Success(var cleanedInput) -> {
                List<String> result = new ArrayList<>();
                generatePermutations(cleanedInput.toCharArray(), 0, result);
                yield result;
            }
            case ValidationResult.Empty() -> List.of();
            case ValidationResult.Invalid(var message) ->
                    throw new IllegalArgumentException(message);
        };
    }

    /**
     * Recursive helper method to generate permutations using backtracking.
     * This method swaps characters to create different arrangements.
     *
     * @param chars Character array to permute
     * @param index Current position in the recursion
     * @param result List to store generated anagrams
     */
    private static void generatePermutations(char[] chars, int index, List<String> result) {
        // Base case: when we've fixed all positions, add the permutation
        if (index == chars.length - 1) {
            result.add(new String(chars));
            return;
        }

        // Recursive case: try each character at the current position
        for (int i = index; i < chars.length; i++) {
            // Swap current character with character at index
            swap(chars, index, i);

            // Recursively generate permutations for remaining positions
            generatePermutations(chars, index + 1, result);

            // Backtrack: restore original order
            swap(chars, index, i);
        }
    }

    /**
     * Helper method to swap two characters in an array.
     *
     * @param chars Character array
     * @param i First index
     * @param j Second index
     */
    private static void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }

    /**
     * Validates the input string and returns a ValidationResult.
     * Using sealed classes pattern (Java 17+) for type-safe validation results.
     *
     * @param input The input string to validate
     * @return ValidationResult indicating success or failure
     */
    private static ValidationResult validateInput(String input) {
        // Check for null or empty input
        if (input == null || input.isEmpty()) {
            return new ValidationResult.Empty();
        }

        // Check if all characters are letters and remove duplicates
        var cleanedInput = input.chars()
                .filter(Character::isLetter)
                .mapToObj(c -> String.valueOf((char) Character.toLowerCase(c)))
                .distinct()
                .collect(Collectors.joining());

        // If no valid letters found
        if (cleanedInput.isEmpty()) {
            return new ValidationResult.Invalid("Input must contain at least one letter");
        }

        // Check if original input had non-letter characters
        if (cleanedInput.length() != input.replaceAll("\\s", "").length()) {
            System.out.println("Warning: Non-letter characters were filtered out");
        }

        return new ValidationResult.Success(cleanedInput);
    }

    /**
     * Sealed interface for validation results (Java 17+)
     * Provides type-safe way to handle different validation outcomes.
     */
    private sealed interface ValidationResult {
        record Success(String cleanedInput) implements ValidationResult {}
        record Empty() implements ValidationResult {}
        record Invalid(String message) implements ValidationResult {}
    }

    /**
     * Alternative implementation using Stream API for functional approach.
     * This method demonstrates a more functional programming style.
     *
     * @param input The input string
     * @return Stream of anagrams
     */
    public static Stream<String> generateAnagramsStream(String input) {
        var validationResult = validateInput(input);

        return switch (validationResult) {
            case ValidationResult.Success(var cleanedInput) ->
                    permutationsStream(cleanedInput);
            case ValidationResult.Empty() -> Stream.empty();
            case ValidationResult.Invalid(var message) ->
                    throw new IllegalArgumentException(message);
        };
    }

    /**
     * Helper method to generate permutations as a Stream.
     *
     * @param str Input string
     * @return Stream of all permutations
     */
    private static Stream<String> permutationsStream(String str) {
        if (str.length() <= 1) {
            return Stream.of(str);
        }

        return str.chars()
                .mapToObj(c -> (char) c)
                .flatMap(c -> {
                    String remaining = str.replaceFirst(String.valueOf(c), "");
                    return permutationsStream(remaining)
                            .map(perm -> c + perm);
                })
                .distinct();
    }

    /**
     * Main method with comprehensive testing
     */
    public static void main(String[] args) {
        System.out.println("=== Anagram Generator Test Suite ===\n");

        // Test Case 1: Standard input with 3 letters
        testCase("abc", "Standard 3-letter input");

        // Test Case 2: Single letter (edge case)
        testCase("a", "Single letter edge case");

        // Test Case 3: Empty input (edge case)
        testCase("", "Empty input edge case");

        // Test Case 4: Input with duplicates (should remove duplicates)
        testCase("aab", "Input with duplicate letters");

        // Test Case 5: Input with non-letters (should filter)
        testCase("a1b2c", "Input with non-letter characters");

        // Test Case 6: Larger input
        testCase("abcd", "4-letter input");

        // Performance comparison for functional vs imperative
        System.out.println("\n=== Performance Comparison ===");
        performanceComparison("abcde");
    }

    /**
     * Helper method to run and display test cases.
     *
     * @param input Test input
     * @param description Test description
     */
    private static void testCase(String input, String description) {
        System.out.println("Test: " + description);
        System.out.println("Input: \"" + input + "\"");

        try {
            List<String> anagrams = generateAnagrams(input);
            System.out.println("Output: " + anagrams);
            System.out.println("Count: " + anagrams.size());

            // Verify using Stream implementation
            var streamResult = generateAnagramsStream(input).toList();
            boolean matching = new HashSet<>(anagrams).equals(new HashSet<>(streamResult));
            System.out.println("Stream implementation matches: " + matching);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("---\n");
    }

    /**
     * Compares performance between imperative and functional implementations.
     *
     * @param input Test input for performance comparison
     */
    private static void performanceComparison(String input) {
        System.out.println("Comparing performance for input: \"" + input + "\"");

        // Warm-up runs
        for (int i = 0; i < 100; i++) {
            generateAnagrams(input);
            generateAnagramsStream(input).toList();
        }

        // Imperative approach timing
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            generateAnagrams(input);
        }
        long imperativeTime = System.nanoTime() - startTime;

        // Functional approach timing
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            generateAnagramsStream(input).toList();
        }
        long functionalTime = System.nanoTime() - startTime;

        System.out.printf("Imperative approach: %.2f ms%n", imperativeTime / 1_000_000.0);
        System.out.printf("Functional approach: %.2f ms%n", functionalTime / 1_000_000.0);
        System.out.printf("Ratio (functional/imperative): %.2fx%n",
                (double) functionalTime / imperativeTime);
    }
}

