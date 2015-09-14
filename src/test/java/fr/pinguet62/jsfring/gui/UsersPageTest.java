package fr.pinguet62.jsfring.gui;

import static fr.pinguet62.jsfring.gui.htmlunit.DateUtils.getDatetime;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import fr.pinguet62.Config;
import fr.pinguet62.jsfring.dao.UserDao;
import fr.pinguet62.jsfring.gui.htmlunit.AbstractPage;
import fr.pinguet62.jsfring.gui.htmlunit.datatable.AbstractDatatablePage;
import fr.pinguet62.jsfring.gui.htmlunit.datatable.AbstractRow;
import fr.pinguet62.jsfring.gui.htmlunit.user.UserRow;
import fr.pinguet62.jsfring.gui.htmlunit.user.UsersPage;
import fr.pinguet62.jsfring.gui.htmlunit.user.popup.UserShowPopup;
import fr.pinguet62.jsfring.gui.htmlunit.user.popup.UserUpdatePopup;
import fr.pinguet62.jsfring.model.User;

/** @see UsersPage */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = Config.SPRING)
@DatabaseSetup(Config.DATASET)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class UsersPageTest {

    @Inject
    private UserDao userDao;

    @Test
    public void test_dataTable_action_rendered() {
        UsersPage usersPage = AbstractPage.get().gotoUsersPage();

        assertTrue(usersPage.isCreateButtonVisible());
        for (UserRow row : usersPage.getRows()) {
            assertTrue(row.isActionButtonShowVisible());
            assertTrue(row.isActionButtonUpdateVisible());
            assertTrue(row.isActionButtonDeleteVisible());
        }
    }

    /**
     * <ul>
     * <li>Each row must be different</li>
     * <li>Each page must be different</li>
     * </ul>
     *
     * @see AbstractRow#actionShow()
     */
    @Test
    public void test_dataTable_action_show() {
        UsersPage page = AbstractPage.get().gotoUsersPage();
        {
            // Page 1
            List<UserRow> rows = page.getRows();
            {
                UserShowPopup popup = rows.get(0).actionShow();

                assertEquals("super admin", popup.getLogin().getValue());
                assertTrue(popup.getLogin().isReadonly());

                assertEquals("admin@domain.fr", popup.getEmail().getValue());
                assertTrue(popup.getEmail().isReadonly());

                assertEquals(true, popup.isActive().getValue());
                assertTrue(popup.isActive().isReadonly());

                assertEquals(getDatetime(2015, 6, 14, 13, 45, 41), popup.getLastConnection().getValue());
                assertTrue(popup.getLastConnection().isReadonly());

                assertEquals(Arrays.asList("Profile admin", "User admin"), popup.getProfiles().getValue());
                assertTrue(popup.getProfiles().isReadonly());

                popup.close();
            }

            {
                UserShowPopup popup = rows.get(1).actionShow();

                assertEquals("admin profile", popup.getLogin().getValue());
                assertTrue(popup.getLogin().isReadonly());

                assertEquals("admin_profile@domain.fr", popup.getEmail().getValue());
                assertTrue(popup.getEmail().isReadonly());

                assertEquals(true, popup.isActive().getValue());
                assertTrue(popup.isActive().isReadonly());

                assertNull(popup.getLastConnection().getValue());
                assertTrue(popup.getLastConnection().isReadonly());

                assertEquals(Arrays.asList("Profile admin"), popup.getProfiles().getValue());
                assertTrue(popup.getProfiles().isReadonly());

                popup.close();
            }
        }

        page.gotoNextPage();
        {
            // Page 2
            List<UserRow> rows = page.getRows();
            {
                UserShowPopup popup = rows.get(0).actionShow();

                assertEquals("admin user", popup.getLogin().getValue());
                assertTrue(popup.getLogin().isReadonly());

                assertEquals("admin_user@domain.fr", popup.getEmail().getValue());
                assertTrue(popup.getEmail().isReadonly());

                assertEquals(true, popup.isActive().getValue());
                assertTrue(popup.isActive().isReadonly());

                assertNull(popup.getLastConnection().getValue());
                assertTrue(popup.getLastConnection().isReadonly());

                assertEquals(Arrays.asList("User admin"), popup.getProfiles().getValue());
                assertTrue(popup.getProfiles().isReadonly());

                popup.close();
            }
        }
    }

    /** @see AbstractRow#actionUpdate() */
    @Test
    public void test_dataTable_action_update() {
        UsersPage page = AbstractPage.get().gotoUsersPage();
        {
            // Page 1
            List<UserRow> rows = page.getRows();
            {
                UserUpdatePopup popup = rows.get(0).actionUpdate();

                assertEquals("super admin", popup.getLogin().getValue());
                assertTrue(popup.getLogin().isReadonly());

                assertEquals("admin@domain.fr", popup.getEmail().getValue());
                assertFalse(popup.getEmail().isReadonly());

                assertEquals(true, popup.isActive().getValue());
                assertFalse(popup.isActive().isReadonly());

                assertEquals(getDatetime(2015, 6, 14, 13, 45, 41), popup.getLastConnection().getValue());
                assertTrue(popup.getLastConnection().isReadonly());

                assertEquals(Arrays.asList("Profile admin", "User admin"), popup.getProfiles().getValue());
                assertFalse(popup.getProfiles().isReadonly());

                popup.close();
            }
            {
                UserUpdatePopup popup = rows.get(1).actionUpdate();

                assertEquals("admin profile", popup.getLogin().getValue());
                assertTrue(popup.getLogin().isReadonly());

                assertEquals("admin_profile@domain.fr", popup.getEmail().getValue());
                assertFalse(popup.getEmail().isReadonly());

                assertEquals(true, popup.isActive().getValue());
                assertFalse(popup.isActive().isReadonly());

                assertNull(popup.getLastConnection().getValue());
                assertTrue(popup.getLastConnection().isReadonly());

                assertEquals(Arrays.asList("Profile admin"), popup.getProfiles().getValue());
                assertFalse(popup.getProfiles().isReadonly());

                popup.close();
            }
        }
        page.gotoNextPage();
        {
            // Page 2
            List<UserRow> rows = page.getRows();
            {
                UserUpdatePopup popup = rows.get(0).actionUpdate();

                assertEquals("admin user", popup.getLogin().getValue());
                assertTrue(popup.getLogin().isReadonly());

                assertEquals("admin_user@domain.fr", popup.getEmail().getValue());
                assertFalse(popup.getEmail().isReadonly());

                assertEquals(true, popup.isActive().getValue());
                assertFalse(popup.isActive().isReadonly());

                assertNull(popup.getLastConnection().getValue());
                assertTrue(popup.getLastConnection().isReadonly());

                assertEquals(Arrays.asList("User admin"), popup.getProfiles().getValue());
                assertFalse(popup.getProfiles().isReadonly());

                popup.close();
            }
        }
    }

    /** @see AbstractDatatablePage#getRows() */
    @Test
    public void test_dataTable_getRows() {
        List<User> users = userDao.getAll();
        List<UserRow> rows = AbstractPage.get().gotoUsersPage().getRows();

        for (int i = 0; i < 2; i++) {
            User user = users.get(i);
            UserRow row = rows.get(i);

            assertEquals(user.getLogin(), row.getLogin());
            assertEquals(user.getEmail(), row.getEmail());
            assertEquals(user.isActive(), row.getActive());
            assertEquals(user.getLastConnection(), row.getLastConnection());
        }
    }

    /** @see UsersPage#sortByEmail() */
    @Test
    public void test_dataTable_sort_email() {
        List<String> emails = userDao.getAll().stream().map(User::getEmail).sorted().collect(toList());

        UsersPage usersPage = AbstractPage.get().gotoUsersPage();
        usersPage.sortByEmail();
        List<UserRow> rows = usersPage.getRows();

        for (int i = 0; i < 2; i++) {
            assertEquals(emails.get(i), rows.get(i).getEmail());
        }
    }

    /** @see UsersPage#sortByLogin() */
    @Test
    public void test_dataTable_sort_login() {
        List<String> logins = userDao.getAll().stream().map(User::getLogin).sorted().collect(toList());

        UsersPage usersPage = AbstractPage.get().gotoUsersPage();
        usersPage.sortByLogin();
        List<UserRow> rows = usersPage.getRows();

        for (int i = 0; i < 2; i++) {
            assertEquals(logins.get(i), rows.get(i).getLogin());
        }
    }

}