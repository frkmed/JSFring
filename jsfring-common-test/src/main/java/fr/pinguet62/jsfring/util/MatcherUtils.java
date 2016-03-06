package fr.pinguet62.jsfring.util;

import static org.apache.commons.collections.CollectionUtils.isEqualCollection;
import static org.apache.commons.lang3.time.DateUtils.truncate;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.collect.Lists;

/** Utility class with simple {@link Matcher}s. */
public final class MatcherUtils {

    /**
     * Check that the {@link Date} is equals to another with a delta.
     *
     * @param expected The {@link Date}.
     * @param field The field from the field {@code Calendar}.
     * @return The {@link Matcher}.
     * @see DateUtils#truncate(Date, int)
     * @see Date#equals(Object)
     */
    public static Matcher<Date> equalToTruncated(Date expected, int field) {
        return new TypeSafeMatcher<Date>() {

            @Override
            public void describeTo(Description description) {
            }

            /**
             * @param actual The {@link Date} to compare.
             * @see Date#truncate(Date, int)
             * @see Date#equals(Object)
             */
            @Override
            protected boolean matchesSafely(Date actual) {
                if (expected == null && actual == null)
                    return true;
                else if (expected == null || actual == null)
                    return false;
                else
                    return Objects.equals(truncate(actual, field), truncate(expected, field));
            }
        };
    };

    /**
     * Check That 2 {@link Iterable}s are {@link Object#equals(Object) equals}, without considering the order.
     *
     * @param expected The expected {@link Iterable}.
     */
    public static <T> Matcher<Iterable<T>> equalWithoutOrderTo(Iterable<T> expected) {
        return new TypeSafeMatcher<Iterable<T>>() {

            @Override
            public void describeTo(Description description) {
            }

            /**
             * @param actual The {@link Iterable} to check.
             * @see ListUtils#intersection(List, List)
             */
            @Override
            protected boolean matchesSafely(Iterable<T> actual) {
                List<T> actualList = Lists.newArrayList(actual);
                List<T> expectedList = Lists.newArrayList(expected);
                if (expectedList.size() != actualList.size())
                    return false;
                return isEqualCollection(actualList, expectedList);
            }
        };
    };

    /**
     * {@link Matcher} used to {@link Function map} object to another type and apply another {@link Mapper} on converted
     * value.
     *
     * @param <S> The source type.
     * @param <M> The mapped type.
     * @param mapper The {@link Function mapper}.
     * @param matcher The {@link Matcher} to apply on converted value.
     * @return The {@link Matcher}.
     */
    public static <S, M> Matcher<S> mappedTo(Function<S, M> mapper, Matcher<M> matcher) {
        return new TypeSafeMatcher<S>() {

            @Override
            public void describeTo(Description description) {
            }

            /**
             * Convert source object and apply {@link Matcher}.
             *
             * @param actual The source object.
             * @see Matcher#matches(Object)
             */
            @Override
            protected boolean matchesSafely(S actual) {
                M mapped = mapper.apply(actual);
                return matcher.matches(mapped);
            }
        };
    }

    /**
     * Check that value {@link String#matches(String) matches to regex}.
     *
     * @param regex The regex.
     * @return The {@link Matcher}.
     * @see String#matches(String)
     */
    public static Matcher<String> matches(String regex) {
        return new TypeSafeMatcher<String>() {

            @Override
            public void describeTo(Description description) {
            }

            /**
             * @param actual The {@link String} to test.
             * @see String#matches(String)
             */
            @Override
            protected boolean matchesSafely(String actual) {
                return actual.matches(regex);
            }
        };
    }

    /**
     * Check that {@link List} is sorted.
     *
     * @param <T> The type of elements.
     * @param comparator The {@link Comparator} used by algorithm.
     * @return The {@link Matcher}.
     */
    public static <T> Matcher<List<T>> sorted(Comparator<T> comparator) {
        return new TypeSafeMatcher<List<T>>() {

            @Override
            public void describeTo(Description description) {
            }

            /**
             * @param values The {@link List} to check.
             * @throws ClassCastException If the list contains elements that are not {@link Comparable}.
             */
            @Override
            protected boolean matchesSafely(List<T> values) {
                for (int i = 0, j = 1; j < values.size(); i++, j++)
                    if (0 < comparator.compare(values.get(i), values.get(j)))
                        return false;
                return true;
            }
        };
    }

    // Utils class
    private MatcherUtils() {
    }

}