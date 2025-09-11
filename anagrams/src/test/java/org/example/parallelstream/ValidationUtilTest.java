package org.example.parallelstream;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidationUtilTest {

//    @Autowired
//    ValidationUtil vu;

    @Test
    void validateInputNotNullOrEmpty() {

        /*
        ValidationException thrown;
        thrown = assertThrows(ValidationException.class, () -> {
                    vu.validateInputNotNullOrEmpty("");
                }
        );
        IO.println("thrown.getMessage()");
        IO.println(thrown.getMessage());

        assertTrue(thrown != null);

        thrown = assertThrows(ValidationException.class, () -> {
                    vu.validateInputNotNullOrEmpty(null);
                }
        );
        assertTrue(thrown != null);
        */

    }

    @Test
    void cleanInput() {
    }

    @Test
    void validateLettersOnly() {
    }

    @Test
    void validateDistinctCharacters() {
    }

    @Test
    void validateInputLength() {
    }

    @Test
    void validations() {
    }

}