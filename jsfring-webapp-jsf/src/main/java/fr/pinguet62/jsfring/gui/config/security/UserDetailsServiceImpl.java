package fr.pinguet62.jsfring.gui.config.security;

import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.pinguet62.jsfring.dao.sql.UserDao;
import fr.pinguet62.jsfring.model.sql.User;

/**
 * Implementation of {@link UserDetailsService}.
 * <p>
 * <ol>
 * <li>User fill <i>username</i> and <i>password</i></li>
 * <li>{@link DaoAuthenticationProvider} request {@link UserDetails} to
 * {@link UserDetailsService}</li>
 * <li>{@link UserDetailsService} retrieve user's informations in database, from
 * its <i>username</i></li>
 * <li>{@link DaoAuthenticationProvider} check user's <i>password</i> from the
 * {@link UserDetails#getPassword() database password} and other informations
 * </li>
 * </ol>
 *
 * @see AuthenticationProvider
 * @see UserDetailsService
 */
@Service("userDetailsService")
public final class UserDetailsServiceImpl implements UserDetailsService {

    @Inject
    private UserDao userDao;

    /**
     * The login process.<br>
     * Reset {@link User#lastConnection} after connection.
     *
     * @param login The {@link User#login user's login}.
     * @return The {@link UserDetails}.
     * @throws UsernameNotFoundException If {@link User#getLogin()} is not
     *             found.
     * @see UserDetailsImpl
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userDao.findOne(login);

        if (user == null)
            throw new UsernameNotFoundException(null);

        userDao.resetLastConnectionDate(user);

        return new UserDetailsImpl(user);
    }

}