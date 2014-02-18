package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

/**
 * Showcase for class base.Optional.
 * <pre>
 * Reference to wiki page:
 *    http://code.google.com/p/guava-libraries/wiki/UsingAndAvoidingNullExplained#Optional
 * </pre>
 * @author Sky Ao
 *
 */
public class OptionalShowcase {

    @Test
    public void of() {
        Optional<Integer> t = Optional.of(1);
        assertThat(t.get()).isEqualTo(1);
        assertThat(t.isPresent()).isTrue();
    }

    @Test
    public void absent() {
        Optional<Integer> t = Optional.absent();
        assertThat(t.isPresent()).isFalse();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void absent_ExceptionWhenGet() {
        Optional<Integer> t = Optional.absent();
        t.get();
    }

    @Test
    public void fromNullable() {
        Optional<Integer> t = Optional.fromNullable(1);
        assertThat(t.isPresent()).isTrue();
        assertThat(t.get()).isEqualTo(1);

        t = Optional.fromNullable(null);
        assertThat(t.isPresent()).isFalse();
    }

    @Test
    public void or() {
        Optional<Integer> t = Optional.absent();
        assertThat(t.or(1)).isEqualTo(1);
        assertThat(t.or(Optional.of(2)).get()).isEqualTo(2);

        t = Optional.of(3);
        assertThat(t.or(1)).isEqualTo(3);
    }

    @Test
    public void orNull() {
        Optional<Integer> t = Optional.absent();
        assertThat(t.orNull()).isNull();

        t = Optional.of(3);
        assertThat(t.orNull()).isEqualTo(3);
    }

    @Test
    public void asSet() {
        Optional<Integer> t = Optional.absent();
        assertThat(t.asSet()).isEmpty();

        t = Optional.of(3);
        assertThat(t.asSet()).contains(3).hasSize(1);
    }

    @Test
    public void transform() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input * 2);
            }
        };
        Optional<Integer> t = Optional.absent();
        t.transform(function);
        assertThat(t.transform(function).isPresent()).isFalse();

        t = Optional.of(3);
        assertThat(t.transform(function).get()).isEqualTo("6");
    }

    @Test
    public void presentInstances() {
        Optional<Integer> t = Optional.absent();
        @SuppressWarnings("unchecked")
        ArrayList<Optional<Integer>> values = Lists.newArrayList(Optional.of(1), t, Optional.of(2), t, Optional.of(3));
        Iterable<Integer> instances = Optional.presentInstances(values);
        assertThat(instances).hasSize(3).contains(1, 2, 3);
    }
}
