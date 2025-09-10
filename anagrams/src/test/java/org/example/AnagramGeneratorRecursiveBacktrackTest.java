package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

/**
 * Unit tests for AnagramGenerator class.
 * Uses JUnit 5 with modern testing features.
 */
class AnagramGeneratorRecursiveBacktrackTest {

    @Test
    @DisplayName("Generate anagrams from 'a' to ... should return all permutations")
    void testGenerateAnagramsAToD() {
        List<String> result = AnagramGeneratorRecursiveBacktrack.generateAnagrams("abcdefg");

        System.out.println("result: " + result);
//        Set<String> expected = Set.of("abc", "acb", "bac", "bca", "cab", "cba");
//        assertEquals(6, result.size());
//        assertEquals(expected, Set.copyOf(result));
    }

    @Test
    @DisplayName("Generate anagrams for 'abc' should return all permutations")
    void testGenerateAnagramsForABC() {
        List<String> result = AnagramGeneratorRecursiveBacktrack.generateAnagrams("abc");

        Set<String> expected = Set.of("abc", "acb", "bac", "bca", "cab", "cba");
        assertEquals(6, result.size());
        assertEquals(expected, Set.copyOf(result));
    }

    @Test
    @DisplayName("Generate anagrams for 'ab' should return both permutations")
    void testGenerateAnagramsForAB() {
        List<String> result = AnagramGeneratorRecursiveBacktrack.generateAnagrams("ab");

        Set<String> expected = Set.of("ab", "ba");
        assertEquals(2, result.size());
        assertEquals(expected, Set.copyOf(result));
    }

    @Test
    @DisplayName("Generate anagrams for single letter should return the letter itself")
    void testGenerateAnagramsForSingleLetter() {
        List<String> result = AnagramGeneratorRecursiveBacktrack.generateAnagrams("a");

        assertEquals(1, result.size());
        assertEquals("a", result.getFirst());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception for null or empty input")
    void testInvalidInputNullOrEmpty(String input) {
        assertThrows(IllegalArgumentException.class, () -> {
            AnagramGeneratorRecursiveBacktrack.generateAnagrams(input);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"a1", "ab!", "123", "a b"})
    @DisplayName("Should throw exception for input containing non-letters")
    void testInvalidInputNonLetters(String input) {
        assertThrows(IllegalArgumentException.class, () -> {
            AnagramGeneratorRecursiveBacktrack.generateAnagrams(input);
        });
    }

    @Test
    @DisplayName("Should throw exception for input with duplicate letters")
    void testInvalidInputDuplicateLetters() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnagramGeneratorRecursiveBacktrack.generateAnagrams("aab");
        });
    }



    @Test
    @DisplayName("Modern implementation should produce same results as classic")
    void testModernImplementation() {
        String input = "xyz";
        List<String> classicResult = AnagramGeneratorRecursiveBacktrack.generateAnagrams(input);
        List<String> modernResult = AnagramGeneratorRecursiveBacktrack.generateAnagramsModern(input);

        assertEquals(Set.copyOf(classicResult), Set.copyOf(modernResult));
        assertEquals(6, modernResult.size()); // 3! = 6 permutations
    }

    @ParameterizedTest
    @CsvSource({
            "a, 1",
            "ab, 2",
            "abc, 6",
            "abcd, 24",
            "abcde, 120"
    })
    @DisplayName("Should generate correct number of permutations for different inputs")
    void testNumberOfPermutations(String input, int expectedCount) {
        List<String> result = AnagramGeneratorRecursiveBacktrack.generateAnagrams(input);
        assertEquals(expectedCount, result.size());
    }



}