package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * JUnit 5 Test Class for AnagramGenerator
 *
 * To run these tests, add JUnit 5 to your project dependencies.
 * This class can be placed in a separate test file.
 */

class AnagramGeneratorRecursivePermutationTest {

    @Test
    void testThreeLetters() {
        List<String> result = AnagramGeneratorRecursiveBacktrack.generateAnagrams("abc");
        assertEquals(6, result.size());
        assertTrue(result.containsAll(List.of("abc", "acb", "bac", "bca", "cab", "cba")));
    }

    @Test
    void testSingleLetter() {
        List<String> result = AnagramGeneratorRecursiveBacktrack.generateAnagrams("x");
        assertEquals(List.of("x"), result);
    }

    @Test
    void testEmptyInput() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> AnagramGeneratorRecursiveBacktrack.generateAnagrams(""));
        assertEquals("Input cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testInvalidCharacters() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> AnagramGeneratorRecursiveBacktrack.generateAnagrams("a1b"));
        assertEquals("Input must contain only letters.", exception.getMessage());
    }

    @Test
    void testDuplicateLetters() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> AnagramGeneratorRecursiveBacktrack.generateAnagrams("aab"));
        assertEquals("Input must not contain duplicate letters.", exception.getMessage());
    }
}
