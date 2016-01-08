package fr.pinguet62.jsfring.service;

import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.pinguet62.jsfring.dao.UserDao;
import fr.pinguet62.jsfring.dao.util.PasswordGenerator;
import fr.pinguet62.jsfring.model.User;

/** The service for {@link User}. */
@Service
public class UserService extends AbstractService<User, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    /**
     * Generate random {@link User#password}.
     * <p>
     * The password will match {@link User#PASSWORD_REGEX}.
     *
     * @return A new {@link User#password}.
     */
    public static String randomPassword() {
        return new PasswordGenerator().get();
    }

    private final UserDao dao;

    @Inject
    private SimpleMailMessage forgottenPasswordMessage;

    @Inject
    private MailSender mailSender;

    @Inject
    protected UserService(UserDao dao) {
        super(dao);
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Initialize the {@link User#password} before insert.
     */
    @Override
    @Transactional
    public User create(User object) {
        // Generate password
        object.setPassword(randomPassword());
        object.setLastConnection(new Date());

        return super.create(object);
    }

    /**
     * Reset the password of {@code User} and sent these informations to email.
     *
     * @param email The user's email.
     * @throws IllegalArgumentException Email unknown.
     */
    @Transactional
    public void forgottenPassword(String email) {
        // Get user
        User user = dao.getByEmail(email);
        if (user == null)
            throw new IllegalArgumentException("Email unknown: " + email);

        // Reset password
        user.setPassword(randomPassword());
        dao.update(user);

        // Send email
        SimpleMailMessage message = new SimpleMailMessage(forgottenPasswordMessage);
        message.setTo(user.getEmail());
        message.setText(String.format(forgottenPasswordMessage.getText(), user.getLogin(), user.getPassword()));
        mailSender.send(message);
        LOGGER.info("New password sent to " + user.getLogin() + " user's email");
    }

    /**
     * Update the {@link User#password user's password}.
     *
     * @param login The {@link User#login user's login}.
     * @param password The new {@link User#password user's password}.
     * @throws IllegalArgumentException User not found.
     */
    @Transactional
    public void updatePassword(String login, String password) {
        User user = dao.get(login);
        if (user == null)
            throw new IllegalArgumentException("User not found");

        dao.updatePassword(user, password);
    }

}