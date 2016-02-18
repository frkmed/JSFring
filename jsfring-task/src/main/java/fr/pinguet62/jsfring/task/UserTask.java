package fr.pinguet62.jsfring.task;

import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fr.pinguet62.jsfring.dao.sql.UserDao;
import fr.pinguet62.jsfring.model.sql.User;

/** The service for {@link User}. */
@Component
public class UserTask {

    private static final Logger LOGGER = getLogger(UserTask.class);

    @Inject
    private UserDao dao;

    // TODO Config param for number of days.
    /**
     * Disable all users who have not connected since a delay.
     * <p>
     * Scheduled method as batch.
     */
    @Scheduled(fixedRate = 1_000/* ms */ * 60/* sec */ * 60/* min */ * 1/* h */)
    @Transactional
    public void disableInactiveUsers() {
        LOGGER.info("Scheduling...");
        dao.disableInactiveUsers(7);
    }

}