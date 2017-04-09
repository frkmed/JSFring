package fr.pinguet62.jsfring.util;

import java.util.Base64;
import java.util.Objects;

public class UrlUtils {

    /**
     * Format the values for HTTP {@code Authorization} header parameter. <br>
     * Username and password are combined into a string separated by a colon {@code ":"}, and result is Base64 encoded.
     *
     * @return The Base64 encoded value.<br>
     *         End with {@code "=="}.
     */
    public static String formatAuthorization(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        return new String(Base64.getEncoder().encode((username + ":" + password).getBytes()));
    }

}