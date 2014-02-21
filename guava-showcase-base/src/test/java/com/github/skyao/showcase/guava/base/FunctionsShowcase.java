package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

/**
 * show case for class base.Functions.
 * 
 * @author Sky Ao
 *
 */
public class FunctionsShowcase {

    @Test
    public void toStringFunction() {
        Function<Object, String> stringFunction = Functions.toStringFunction();
        assertThat(stringFunction.apply(new Integer(123))).isEqualTo("123");
    }

    @Test
    public void identity() {
        Function<Object, Object> identifyFunction = Functions.identity();
        Integer input = new Integer(123);
        assertThat(identifyFunction.apply(input)).isSameAs(input);
    }

    @Test
    public void forMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "123");
        Function<String, String> mapFunction = Functions.forMap(map);
        assertThat(mapFunction.apply("a")).isEqualTo("123");

        try {
            assertThat(mapFunction.apply("b")).isNull();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    public void forMap_withDefault() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "123");
        Function<String, String> mapFunction = Functions.forMap(map, "defaultValue");
        assertThat(mapFunction.apply("a")).isEqualTo("123");
        assertThat(mapFunction.apply("b")).isEqualTo("defaultValue");
    }

    @Test
    public void compose() {
        Function<String, Integer> string2intFunction = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.parseInt(input);
            }

        };
        Function<Integer, Integer> doubleFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input.intValue() * 2;
            }
        };

        Function<String, Integer> composeFunction = Functions.compose(doubleFunction, string2intFunction);
        assertThat(composeFunction.apply("100")).isEqualTo(200);
    }

    @Test
    public void forPredicate() {
        Predicate<Integer> predicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        Function<Integer, Boolean> predicateFunction = Functions.forPredicate(predicate);
        assertThat(predicateFunction.apply(1)).isTrue();
        assertThat(predicateFunction.apply(-1)).isFalse();
    }

    @Test
    public void constant() {
        String constant = "100";
        Function<Object, String> constantFunction = Functions.constant(constant);
        assertThat(constantFunction.apply(new Integer(123))).isSameAs(constant);
    }

    @Test
    public void forSupplier() {
        Supplier<Integer> predicate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 123;
            }
        };

        Function<Object, Integer> supplierFunction = Functions.forSupplier(predicate);
        assertThat(supplierFunction.apply(1)).isEqualTo(123);
        assertThat(supplierFunction.apply(-1)).isEqualTo(123);
    }
}
