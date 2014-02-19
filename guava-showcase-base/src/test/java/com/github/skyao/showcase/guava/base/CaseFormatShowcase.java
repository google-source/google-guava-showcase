package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

/**
 * show case for class base.CaseFormat.
 * @author Sky Ao
 *
 */
public class CaseFormatShowcase {

    @Test
    public void convateCase() {
        Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);
        assertThat(converter.convert("lowerCamel")).isEqualTo("LowerCamel");
        assertThat(converter.reverse().convert("LowerCamel")).isEqualTo("lowerCamel");

        converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN);
        assertThat(converter.convert("lowerCamel")).isEqualTo("lower-camel");
        assertThat(converter.reverse().convert("lower-camel")).isEqualTo("lowerCamel");
    }
}
