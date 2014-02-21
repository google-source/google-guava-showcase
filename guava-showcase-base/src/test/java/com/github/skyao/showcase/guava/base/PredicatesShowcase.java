package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

/**
 * show case for class base.Predicates.
 * 
 * @author Sky Ao
 *
 */
public class PredicatesShowcase {

    @Test
    public void alwaysTrue() {
        assertThat(Predicates.alwaysTrue().apply("any")).isTrue();
    }

    @Test
    public void alwaysFalse() {
        assertThat(Predicates.alwaysFalse().apply("any")).isFalse();
    }

    @Test
    public void isNull() {
        assertThat(Predicates.isNull().apply("any")).isFalse();
        assertThat(Predicates.isNull().apply(null)).isTrue();
    }

    @Test
    public void notNull() {
        assertThat(Predicates.notNull().apply("any")).isTrue();
        assertThat(Predicates.notNull().apply(null)).isFalse();
    }

    @Test
    public void not() {
        assertThat(Predicates.not(Predicates.alwaysTrue()).apply("any")).isFalse();
    }

    @Test
    public void and() {
        Predicate<String> predicate = Predicates.and(Predicates.notNull(), Predicates.equalTo("value"));
        assertThat((predicate).apply("any")).isFalse();
    }

    @Test
    public void or() {
        Predicate<String> predicate = Predicates.or(Predicates.isNull(), Predicates.equalTo("value"));
        assertThat((predicate).apply("any")).isFalse();
    }

    @Test
    public void equalTo() {
        assertThat(Predicates.equalTo("value").apply("any")).isFalse();
    }

    @Test
    public void instanceOf() {
        assertThat(Predicates.instanceOf(String.class).apply("any")).isTrue();
    }

    @Test
    public void assignableFrom() {
        assertThat(Predicates.assignableFrom(String.class).apply(Integer.class)).isFalse();
        assertThat(Predicates.assignableFrom(Object.class).apply(Integer.class)).isTrue();
    }

    @Test
    public void in() {
        assertThat(Predicates.in(Lists.newArrayList(1, 2, 3, 4)).apply(1)).isTrue();
    }

    @Test
    public void compose() {
        Function<String, Integer> string2intFunction = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(input);
            }

        };

        assertThat(Predicates.compose(Predicates.equalTo(123), string2intFunction).apply("123")).isTrue();
    }

    @Test
    public void containsPattern() {
        assertThat(Predicates.containsPattern("^139").apply("13900000000")).isTrue();
    }

    @Test
    public void contains() {
        assertThat(Predicates.contains(Pattern.compile("^139")).apply("13900000000")).isTrue();
    }
}
