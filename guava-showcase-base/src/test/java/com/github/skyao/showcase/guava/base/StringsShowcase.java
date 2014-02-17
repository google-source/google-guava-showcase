package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.google.common.base.Strings;

/**
 * show case for class base.StringsShowcase.
 * 
 * @author Sky Ao
 *
 */
public class StringsShowcase {

    @Test
    public void nullToEmpty() {
        // normal code
        String content = testString();
        if (content == null) {
            content = "";
        }

        //guava style code
        content = Strings.nullToEmpty(content);

        // test case
        assertThat(Strings.nullToEmpty("abc")).isEqualTo("abc");
        assertThat(Strings.nullToEmpty(null)).isEqualTo("");
    }

    @Test
    public void emptyToNull() {
        // normal code
        String content = testString();
        if ("".equals(content)) {
            content = null;
        }

        //guava style code
        content = Strings.emptyToNull(content);

        // test case
        assertThat(Strings.emptyToNull("abc")).isEqualTo("abc");
        assertThat(Strings.emptyToNull("")).isNull();
        // Attention, no trim for input string
        assertThat(Strings.emptyToNull(" ")).isNotNull();
    }

    @Test
    public void isNullOrEmpty() {
        // normal code
        String content = testString();
        if (content == null || content.length() == 0) {

        }

        //guava style code
        if (Strings.isNullOrEmpty(content)) {

        }

        // test case
        assertThat(Strings.isNullOrEmpty("abc")).isFalse();
        assertThat(Strings.isNullOrEmpty("")).isTrue();
        // Attention, no trim for input string
        assertThat(Strings.isNullOrEmpty(" ")).isFalse();
    }

    @Test
    public void padStart() {
        assertThat(Strings.padStart("1", 5, '0')).isEqualTo("00001");
        assertThat(Strings.padStart("12", 5, '0')).isEqualTo("00012");
        assertThat(Strings.padStart("123", 5, '0')).isEqualTo("00123");
        assertThat(Strings.padStart("1234", 5, '0')).isEqualTo("01234");
        assertThat(Strings.padStart("12345", 5, '0')).isEqualTo("12345");
        assertThat(Strings.padStart("123456", 5, '0')).isEqualTo("123456");
    }

    @Test
    public void padEnd() {
        assertThat(Strings.padEnd("1.", 6, '0')).isEqualTo("1.0000");
        assertThat(Strings.padEnd("1.2", 6, '0')).isEqualTo("1.2000");
        assertThat(Strings.padEnd("1.23", 6, '0')).isEqualTo("1.2300");
        assertThat(Strings.padEnd("1.234", 6, '0')).isEqualTo("1.2340");
        assertThat(Strings.padEnd("1.2345", 6, '0')).isEqualTo("1.2345");
        assertThat(Strings.padEnd("1.23456", 6, '0')).isEqualTo("1.23456");
    }

    @Test
    public void repeat() {
        assertThat(Strings.repeat("Hello", 0)).isEqualTo("");
        assertThat(Strings.repeat("Hello", 1)).isEqualTo("Hello");
        assertThat(Strings.repeat("Hello", 2)).isEqualTo("HelloHello");
        assertThat(Strings.repeat("Hello", 3)).isEqualTo("HelloHelloHello");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*invalid count.*")
    public void repeatWithInvalidCount() {
        assertThat(Strings.repeat("Hello", -1)).isEqualTo("");
    }

    @Test
    public void commonPrefix() {
        assertThat(Strings.commonPrefix("HelloWorld", "HelloKitty")).isEqualTo("Hello");
        // case sensitive
        assertThat(Strings.commonPrefix("HelloWorld", "helloKitty")).isEqualTo("");

        assertThat(Strings.commonPrefix("HelloWorld", "abc")).isEqualTo("");
    }

    @Test
    public void commonSuffix() {
        assertThat(Strings.commonSuffix("LifeIsGood", "WorkIsGood")).isEqualTo("IsGood");
        // case sensitive
        assertThat(Strings.commonSuffix("LifeIsGood", "WorkIsGooD")).isEqualTo("");

        assertThat(Strings.commonSuffix("Life is Good", "abc")).isEqualTo("");
    }

    private String testString() {
        return "some content";
    }
}
