package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.google.common.base.Ascii;

/**
 * show case for class base.Ascii.
 * 
 * @author Sky Ao
 *
 */
public class AsciiShowcase {

    @Test
    public void toLowerCase() {
        assertThat(Ascii.toLowerCase("ABC")).isEqualTo("abc");

        String content = "abc";
        assertThat(Ascii.toLowerCase(content)).isSameAs(content);

        // why not just use "ABC".toLowerCase();
        // better performance? 
    }

    @Test
    public void truncate() {
        assertThat(Ascii.truncate("foobar", 7, "...")).isEqualTo("foobar");
        assertThat(Ascii.truncate("foobar", 5, "...")).isEqualTo("fo...");
    }
}
