package fr.pinguet62.jsfring.util.reflection;

import static fr.pinguet62.jsfring.util.reflection.ReflectionUtil.getDefaultMetaObject;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.Entity;

import org.junit.Test;

import fr.pinguet62.jsfring.model.Profile;
import fr.pinguet62.jsfring.model.QProfile;
import fr.pinguet62.jsfring.model.QRight;
import fr.pinguet62.jsfring.model.QUser;
import fr.pinguet62.jsfring.model.Right;
import fr.pinguet62.jsfring.model.User;

/** @see ReflectionUtil */
public final class ReflectionUtilTest {

    /** @see ReflectionUtil#getDefaultMetaObject(Class) */
    @Test
    public void test_getDefaultMetaObject() {
        assertEquals(QUser.user, getDefaultMetaObject(User.class));
        assertEquals(QRight.right_, getDefaultMetaObject(Right.class));
        assertEquals(QProfile.profile, getDefaultMetaObject(Profile.class));
    }

    /**
     * Parameter not valid: {@code null}, not an {@link Entity}.
     *
     * @see ReflectionUtil#getDefaultMetaObject(Class)
     */
    @Test
    public void test_getDefaultMetaObject_badEntityType() {
        for (Class<?> type : asList(null, String.class))
            try {
                getDefaultMetaObject(type);
                fail();
            } catch (RuntimeException exception) {}
    }

}