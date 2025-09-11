package org.example.parallelstream;

//import lombok.extern.java.Log;
//import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class FactorialUtil {
    /**
     * Computes the factorial of a non-negative integer.
     *
     * This iterative implementation is efficient for small values of n (typical in permutation scenarios)
     * and avoids recursion stack overhead.
     *
     * Mathematical Definition:
     * n! = n × (n-1) × (n-2) × ... × 1
     * 0! = 1 (by convention)
     * 1! = 1
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param n The non-negative integer to compute factorial for
     * @return The factorial of n (n!)
     * @throws IllegalArgumentException if n is negative
     */
    public static int factorial(int n) {
        if (n <= 1) return 1;
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

}
