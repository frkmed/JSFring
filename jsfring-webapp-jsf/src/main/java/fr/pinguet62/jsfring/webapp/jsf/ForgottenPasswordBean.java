package fr.pinguet62.jsfring.webapp.jsf;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.MessageSource;

import fr.pinguet62.jsfring.model.sql.User;
import fr.pinguet62.jsfring.service.UserService;
import fr.pinguet62.jsfring.webapp.jsf.config.scope.SpringViewScoped;
import lombok.Getter;
import lombok.Setter;

/** Page to reset its {@link User#password} after an oversight. */
@Named
@SpringViewScoped
public final class ForgottenPasswordBean implements Serializable {

    private static final long serialVersionUID = 1;

    /** The {@link User#email user's email}. */
    @Getter
    @Setter
    private String email;

    @Inject
    private transient MessageSource messageSource;

    @Inject
    private transient UserService userService;

    /**
     * Reset the password.
     *
     * @see UserService#forgottenPassword(String)
     */
    public void reset() {
        Locale locale = getCurrentInstance().getViewRoot().getLocale();

        try {
            userService.forgottenPassword(email);
            String message = messageSource.getMessage("forgottenPassword.messages.informationsSent", null, locale);
            getCurrentInstance().addMessage(null, new FacesMessage(SEVERITY_INFO, message, null));
        } catch (IllegalArgumentException exception) {
            String message = messageSource.getMessage("forgottenPassword.messages.emailUnknown", null, locale);
            getCurrentInstance().addMessage(null, new FacesMessage(SEVERITY_ERROR, message, null));
        }
    }

}