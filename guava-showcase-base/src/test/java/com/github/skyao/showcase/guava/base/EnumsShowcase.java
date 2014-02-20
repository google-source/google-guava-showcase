package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.testng.annotations.Test;

import com.google.common.base.Converter;
import com.google.common.base.Enums;

/**
 * Showcase for class base.Enums.
 * 
 * @author Sky Ao
 *
 */
public class EnumsShowcase {

    @Test
    public void listDefaults() {
        assertThat(Enums.getField(Sports.GOLF).getName()).isEqualTo("GOLF");
        assertThat(Enums.getField(Sports.GOLF).isAnnotationPresent(ExampleAnnotation.class)).isTrue();
        assertThat(Enums.getField(Sports.PINGBANG).isAnnotationPresent(ExampleAnnotation.class)).isFalse();
    }

    @Test
    public void getIfPresent() {
        assertThat(Enums.getIfPresent(Sports.class, "GOLF").isPresent()).isTrue();
        assertThat(Enums.getIfPresent(Sports.class, "PINGBANG").isPresent()).isTrue();
        assertThat(Enums.getIfPresent(Sports.class, "abc").isPresent()).isFalse();
    }

    @Test
    public void stringConverter() {
        Converter<String, Sports> converter = Enums.stringConverter(Sports.class);

        assertThat(converter.convert("GOLF")).isEqualTo(Sports.GOLF);
        assertThat(converter.convert("PINGBANG")).isEqualTo(Sports.PINGBANG);
        try {
            converter.convert("abc");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private static enum Sports {
        @ExampleAnnotation
        GOLF, PINGBANG
    }

    @Retention(RetentionPolicy.RUNTIME)
    private static @interface ExampleAnnotation {
    }
}
