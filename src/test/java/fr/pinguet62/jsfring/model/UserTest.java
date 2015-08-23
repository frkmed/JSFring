package fr.pinguet62.jsfring.model;

import static fr.pinguet62.jsfring.model.User.PASSWORD_REGEX;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

import fr.pinguet62.util.Combinator;

/** @see User */
public final class UserTest {

    /**
     * Generate all combinator of character to generate the possible passwords.<br>
     * Check that all password {@link String#matches(String) match} to the
     * {@link Predicate}.
     *
     * @param passwordChecker The {@link Predicate} to check.
     * @param characters The {@link Character}s of the password.
     * @see Combinator
     */
    private void checkPassword(Predicate<String> passwordChecker,
            List<String> characters) {
        assertTrue(new Combinator<String>(characters).get().stream()
                .map(list -> list.stream().collect(joining("")))
                .allMatch(passwordChecker));
    }

    /** @see User#PASSWORD_REGEX */
    @Test
    public void test_password_regex() {
        Predicate<String> invalidPasswordChecker = password -> !password
                .matches(PASSWORD_REGEX);
        Predicate<String> validPasswordChecker = invalidPasswordChecker
                .negate();

        // 6 character + 1 letter + 1 special
        checkPassword(validPasswordChecker,
                Arrays.asList("5", "$", "a", "b", "c", "d", "e", "f"));
        // < 6 character
        checkPassword(invalidPasswordChecker,
                Arrays.asList("a", "b", "c", "1", "$"));
        // 0 letter
        checkPassword(invalidPasswordChecker,
                Arrays.asList("a", "b", "c", "d", "e", "f", "$", "#", "~"));
        // 0 special
        checkPassword(invalidPasswordChecker,
                Arrays.asList("1", "2", "3", "a", "b", "c", "d", "e", "f"));
    }

}