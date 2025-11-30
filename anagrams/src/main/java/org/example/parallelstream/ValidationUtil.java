package org.example.parallelstream;

//import lombok.extern.java.Log;
//import lombok.extern.log4j.Log4j;
//import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Processor class for generating permutations with comprehensive input validation.
 * Validates input according to requirements: non-empty, only letters, distinct characters.
 *
 * @Component Indicates that this class is a Spring component and should be auto-detected
 * @Slf4j Provides logging capabilities (Lombok annotation)
 */
@Component
@Slf4j
public class ValidationUtil {
    private static final Logger log = LoggerFactory.getLogger(ValidationUtil.class);

    private static final Pattern LETTERS_ONLY_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final int MAX_INPUT_LENGTH = 10; // Prevent excessive computation

    /**
     * Processes input string to generate all possible permutations with validation.
     *
     * @param input the input string to process
     * @return list of all permutations if validation passes
     * @throws ValidationException if input fails validation checks
     * @throws IllegalArgumentException if input is too long for practical computation
     */
    public String validations(String input) throws Exception {

        log.debug("Processing input: '{}'", input);

        // Step 1: Basic null and empty validation
        validateInputNotNullOrEmpty(input);

        // Step 2: Trim and clean input
        String cleanedInput = cleanInput(input);

        // Step 3: Validate contains only letters
        validateLettersOnly(cleanedInput);

        // Step 4: Validate distinct characters
        validateDistinctCharacters(cleanedInput);

        // Step 5: Validate reasonable length (prevent DoS and memory issues)
        validateInputLength(cleanedInput);

        // Step 6: Generate permutations
        log.info("Generating permutations for valid input: '{}'", cleanedInput);
//        return generateAllPermutations(cleanedInput);
        return cleanedInput;
    }

    /**
     * Validates that input is not null or empty
     *
     * @param input the input string to validate
     * @throws ValidationException if input is null or empty
     */
    public void validateInputNotNullOrEmpty(String input) throws ValidationException {
        if (input == null) {
            log.warn("Input cannot be ValidationExceptionnull");
            throw new ValidationException("Input cannot be null");
        }

        if (input.trim().isEmpty()) {
            log.warn("Input cannot be empty or whitespace only");
            throw new ValidationException("Input cannot be empty");
        }
    }

    /**
     * Cleans input by trimming and converting to consistent case
     *
     * @param input the raw input string
     * @return cleaned and normalized input
     */
    public String cleanInput(String input) {
        return input.trim();
    }

    /**
     * Validates that input contains only alphabetic characters
     *
     * @param input the input string to validate
     * @throws ValidationException if input contains non-letter characters
     */
    public void validateLettersOnly(String input) {
        if (!LETTERS_ONLY_PATTERN.matcher(input).matches()) {
            log.warn("Input contains non-letter characters: '{}'", input);
            throw new ValidationException("Input must contain only letters: " + input);
        }
    }

    /**
     * Validates that all characters in input are distinct
     *
     * @param input the input string to validate
     * @throws ValidationException if input contains duplicate characters
     */
    public void validateDistinctCharacters(String input) {
        Set<Character> characterSet = new HashSet<>();
        for (char c : input.toCharArray()) {
            if (!characterSet.add(c)) {
                log.warn("Input contains duplicate characters: '{}'", input);
                throw new ValidationException("Input must contain distinct letters only. Duplicate found: '" + c + "' in: " + input);
            }
        }
    }

    /**
     * Validates that input length is reasonable for permutation generation
     *
     * @param input the input string to validate
     * @throws IllegalArgumentException if input is too long
     */
    public void validateInputLength(String input) {
        if (input.length() > MAX_INPUT_LENGTH) {
            String errorMessage = String.format(
                    "Input too long: %d characters. Maximum allowed: %d. This would generate %d permutations.",
                    input.length(), MAX_INPUT_LENGTH, FactorialUtil.factorial(input.length())
            );
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
