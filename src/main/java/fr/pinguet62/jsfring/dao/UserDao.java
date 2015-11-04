package fr.pinguet62.jsfring.dao;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.Expression;

import fr.pinguet62.jsfring.model.QUser;
import fr.pinguet62.jsfring.model.User;

/** The DAO for {@link User}. */
@Repository
public final class UserDao extends AbstractDao<User, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

    /**
     * Disable all users who have not connected since {@code numberOfDays} days.
     * <br>
     * Ignore {@link User}s who never be connected.
     *
     * @param numberOfDays Number of days.
     * @throws IllegalArgumentException Zero or negative number of days.
     */
    public void disableInactiveUsers(int numberOfDays) {
        if (numberOfDays <= 0)
            throw new IllegalArgumentException("The number of days must be a positive value.");

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, numberOfDays);
        Date lastAccepted = c.getTime();

        QUser u = QUser.user;
        long nb = new JPAUpdateClause(em, u).where(u.active.eq(true).and(u.lastConnection.before(lastAccepted)))
                .set(u.active, false).execute();
        LOGGER.info("Number of users disabled: {}", nb);
    }

    @Override
    protected Expression<User> getBaseExpression() {
        return QUser.user;
    }

    /**
     * Get the {@link User} by email.
     *
     * @param email The {@link User#email user's email}.
     * @return The {@link User}, {@code null} if not found.
     */
    public User getByEmail(String email) {
        QUser user = QUser.user;
        return new JPAQuery(em).from(user).where(user.email.eq(email)).singleResult(user);
    }

    /**
     * Reset the {@link User#lastConnection last connection date} to the current
     * day.
     *
     * @param user The {@link User} to update.
     */
    public void resetLastConnectionDate(User user) {
        LOGGER.debug("Last connection date reset for user: {}", user.getLogin());
        user.setLastConnection(new Date());
        update(user);
    }

    /**
     * Update the {@link User#password password}.
     *
     * @param user The {@link User} to update.
     * @param password The new {@link User#password user's password}.
     */
    public void updatePassword(User user, String password) {
        LOGGER.debug("Password updated for user: {}", user.getLogin());
        user.setPassword(password);
        update(user);
    }

}