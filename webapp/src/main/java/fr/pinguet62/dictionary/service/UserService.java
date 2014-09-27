package fr.pinguet62.dictionary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.pinguet62.dictionary.dao.UserDao;
import fr.pinguet62.dictionary.model.User;

/** The service for {@link User}. */
@Service
public class UserService extends AbstractService<User, String> {

    @Autowired
    protected UserService(UserDao dao) {
        super(dao);
    }

    /**
     * Reset the password of {@code User} and sent these informations to email.
     *
     * @param email
     *            The user's email.
     * @throws IllegalArgumentException
     *             Email unknown.
     */
    public void forgottenPassword(String email) {
        System.out.println("Reset " + email);
    }

}
