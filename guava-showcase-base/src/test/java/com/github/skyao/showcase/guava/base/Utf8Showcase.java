package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Utf8;

/**
 * show case for class base.Utf8.
 * 
 * @author Sky Ao
 *
 */
public class Utf8Showcase {

    @Test
    public void isWellFormed() {
        byte[] content = "abc".getBytes();

        // normal code to check
        boolean isWellFormed = Arrays.equals(content, new String(content, Charsets.UTF_8).getBytes(Charsets.UTF_8));
        assertThat(isWellFormed).isTrue();

        // guava style code
        isWellFormed = Utf8.isWellFormed(content);
        assertThat(isWellFormed).isTrue();
    }

    @Test
    public void encodedLength() {
        String content = "abc";

        // normal code
        int encodedLength = content.getBytes(Charsets.UTF_8).length;
        assertThat(encodedLength).isEqualTo(3);

        // guava style code
        encodedLength = Utf8.encodedLength(content);
        assertThat(encodedLength).isEqualTo(3);
    }
}
