package fr.pinguet62.jsfring.dao.sql;

import static fr.pinguet62.jsfring.test.DbUnitConfig.DATASET;
import static fr.pinguet62.jsfring.util.MatcherUtils.mappedTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.GeneratedValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Predicate;

import fr.pinguet62.jsfring.SpringBootConfig;
import fr.pinguet62.jsfring.dao.sql.common.CommonRepository;
import fr.pinguet62.jsfring.model.sql.Profile;
import fr.pinguet62.jsfring.model.sql.QRight;
import fr.pinguet62.jsfring.model.sql.Right;
import fr.pinguet62.jsfring.model.sql.User;
import fr.pinguet62.jsfring.util.PasswordGenerator;

/** @see CommonRepository */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SpringBootConfig.class)
@DatabaseSetup(DATASET)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@Transactional
public class CommonRepositoryTest {

    @Inject
    private ProfileDao profileDao;

    @Inject
    private RightDao rightDao;

    @Inject
    private UserDao userDao;

    /** @see CrudRepository#count() */
    @Test
    public void test_count() {
        assertThat(rightDao.count(), is(equalTo(5L)));
        assertThat(profileDao.count(), is(equalTo(2L)));
        assertThat(userDao.count(), is(equalTo(3L)));
    }

    /** @see CrudRepository#delete(Object) */
    @Test
    public void test_delete() {
        assertThat(profileDao.count(), is(equalTo(2L)));
        profileDao.delete(profileDao.getOne(1));
        assertThat(profileDao.count(), is(equalTo(1L)));
        profileDao.delete(profileDao.getOne(2));
        assertThat(profileDao.count(), is(equalTo(0L)));
    }

    /** @see CrudRepository#deleteAll() */
    @Test
    public void test_deleteAll() {
        assertThat(profileDao.count(), is(equalTo(2L)));
        // not empty
        profileDao.deleteAll();
        assertThat(profileDao.count(), is(equalTo(0L)));
        // already empty
        profileDao.deleteAll();
    }

    /** @see CommonRepository#find(JPAQuery) */
    @Test
    public void test_find_JPAQuery() {
        final String keyword = "PROFILE";

        QRight r = QRight.right_;
        JPAQuery query = new JPAQuery().from(r).where(r.code.contains(keyword));
        List<Right> rights = rightDao.find(query);

        assertThat(rights, everyItem(mappedTo(Right::getCode, containsString(keyword))));
    }

    /** @see JpaRepository#findAll() */
    @Test
    public void test_findAll() {
        assertThat(rightDao.findAll(), hasSize(5));
        assertThat(profileDao.findAll(), hasSize(2));
        assertThat(userDao.findAll(), hasSize(3));
    }

    /** @see QueryDslPredicateExecutor#findAll(Predicate) */
    @Test
    public void test_findAll_Predicate() {
        final String keyword = "PROFILE";

        Predicate predicate = QRight.right_.code.contains(keyword);
        List<Right> rights = rightDao.findAll(predicate);

        assertThat(rights, everyItem(mappedTo(Right::getCode, containsString(keyword))));
    }

    /** @see QueryDslPredicateExecutor#findAll(Predicate) */
    @Test
    public void test_findAll_Predicate_notFound() {
        List<Right> rights = rightDao.findAll(QRight.right_.code.contains("#$!@"));
        assertThat(rights, is(empty()));
    }

    /**
     * Get an entity from its key.
     *
     * @see JpaRepository#getOne(Serializable)
     */
    @Test
    public void test_getOne() {
        {
            assertThat(rightDao.getOne("RIGHT_RO").getTitle(), is(equalTo("Affichage des droits")));
            assertThat(rightDao.getOne("PROFILE_RO").getTitle(), is(equalTo("Affichage des profils")));
        }
        {
            assertThat(profileDao.getOne(1).getTitle(), is(equalTo("Profile admin")));
            assertThat(profileDao.getOne(2).getTitle(), is(equalTo("User admin")));
        }
        {
            assertThat(userDao.getOne("super admin").getPassword(), is(equalTo("Azerty1!")));
            assertThat(userDao.getOne("admin profile").getEmail(), is(equalTo("admin_profile@domain.fr")));
        }
    }

    /**
     * If the entity doesn't exists, an {@link EntityNotFoundException} must be
     * thrown.
     *
     * @see JpaRepository#getOne(Serializable)
     */
    @Test
    public void test_getOne_notExisting() {
        final int id = -1;
        assertThat(profileDao.exists(id), is(false));

        try {
            profileDao.getOne(id).getTitle(); // fail
            fail();
        } catch (EntityNotFoundException e) {}
    }

    /** @see CrudRepository#save(Object) */
    @Test
    public void test_save_create() {
        {
            long count = profileDao.count();
            profileDao.save(new Profile("new profile"));
            assertThat(profileDao.count(), is(equalTo(count + 1)));
        }
        {
            long count = userDao.count();
            userDao.save(new User("new login", new PasswordGenerator().get(), "foo@hostname.domain"));
            assertThat(userDao.count(), is(equalTo(count + 1)));
        }
    }

    /**
     * The auto-incremented ID must be automatically set during creation.
     *
     * @see CrudRepository#save(Object)
     * @see GeneratedValue
     */
    @Test
    public void test_save_create_autoIncrement() {
        profileDao.save(new Profile("new profile"));
    }

    /**
     * Update a <i>simple</i> (not an <i>association</i>) field.
     *
     * @see JpaRepository#save(Object)
     */
    @Test
    public void test_save_update() {
        final int id = profileDao.findAll().get(0).getId();
        final String newTitle = "new title";

        // Initial state
        Profile profile = profileDao.getOne(id);
        assertThat(profile.getTitle(), is(not(equalTo(newTitle))));

        // Update
        profile.setTitle(newTitle);
        profileDao.save(profile);

        // Check
        profileDao.flush();
        assertThat(profileDao.getOne(1).getTitle(), is(equalTo(newTitle)));
    }

}