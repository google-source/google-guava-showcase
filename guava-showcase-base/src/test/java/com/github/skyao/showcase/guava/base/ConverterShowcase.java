package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.google.common.base.Converter;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;

/**
 * show case for class base.Converter.
 * 
 * @author Sky Ao
 *
 */
public class ConverterShowcase {

    @Test
    public void shortStringConverter() {
        assertThat(Shorts.stringConverter().convert("111").intValue()).isEqualTo(111);
        assertThat(Shorts.stringConverter().reverse().convert(Short.valueOf("111"))).isEqualTo("111");
    }

    @Test
    public void intStringConverter() {
        assertThat(Ints.stringConverter().convert("111").intValue()).isEqualTo(111);
        assertThat(Ints.stringConverter().reverse().convert(111)).isEqualTo("111");
    }

    @Test
    public void andThen() {
        Converter<Integer, Integer> doubleIntConvert = new Converter<Integer, Integer>() {

            @Override
            protected Integer doForward(Integer a) {
                return Integer.valueOf(a * 2);
            }

            @Override
            protected Integer doBackward(Integer b) {
                return Integer.valueOf(b / 2);
            }

        };
        // "111"  --> 111 ---> 222
        assertThat(Ints.stringConverter().andThen(doubleIntConvert).convert("111").intValue()).isEqualTo(222);
    }

    @Test
    public void identify() {
        Converter<String, String> identifyConverter = Converter.identity();
        String content = "111";
        assertThat(identifyConverter.convert(content)).isSameAs(content);
    }
}
