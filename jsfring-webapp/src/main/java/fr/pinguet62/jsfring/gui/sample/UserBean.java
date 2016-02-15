package fr.pinguet62.jsfring.gui.sample;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;

import fr.pinguet62.jsfring.gui.config.UserDetailsImpl;

@Named
@SessionScoped
public final class UserBean implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * Get the {@link UserDetails} in {@link SecurityContext}.
     *
     * @return The {@link UserDetails}.
     * @see UserDetailsImpl
     */
    public static UserDetails get() {
        Authentication authentication = getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken)
            return null;
        else
            return (UserDetails) authentication.getPrincipal();
    }

}