package fr.pinguet62.jsfring.gui;

import static fr.pinguet62.jsfring.gui.htmlunit.AbstractPage.get;
import static fr.pinguet62.jsfring.test.DbUnitConfig.DATASET;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.querydsl.jpa.impl.JPAQuery;

import fr.pinguet62.jsfring.SpringBootConfig;
import fr.pinguet62.jsfring.dao.sql.UserDao;
import fr.pinguet62.jsfring.gui.htmlunit.ForgottenPasswordPage;
import fr.pinguet62.jsfring.model.sql.QUser;
import fr.pinguet62.jsfring.model.sql.User;

/** @see ForgottenPasswordPage */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootConfig.class, webEnvironment = DEFINED_PORT)
@DatabaseSetup(DATASET)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class ForgottenPasswordPageITTest {

    @Inject
    private UserDao userDao;

    /** No error: INFO message. */
    @Test
    public void test_forgottenPassword() {
        String email = userDao.findAll().get(0).getEmail();

        ForgottenPasswordPage forgottenPasswordPage = get().gotoLoginPage().gotoForgottenPasswordPage();

        forgottenPasswordPage.getEmail().setValue(email);
        forgottenPasswordPage.submit();

        assertThat(forgottenPasswordPage.getMessageInfo(), is(not(nullValue())));
    }

    /** Unknown email: ERROR message. */
    @Test
    public void test_forgottenPassword_unknown() {
        String email = "unknown";
        assertThat(userDao.find(new JPAQuery<User>().from(QUser.user).where(QUser.user.email.eq(email))), is(empty()));

        ForgottenPasswordPage forgottenPasswordPage = get().gotoLoginPage().gotoForgottenPasswordPage();

        forgottenPasswordPage.getEmail().setValue(email);
        forgottenPasswordPage.submit();

        assertThat(forgottenPasswordPage.getMessageError(), is(not(nullValue())));
    }

}