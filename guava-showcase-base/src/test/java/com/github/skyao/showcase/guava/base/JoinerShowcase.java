package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.google.common.base.Joiner;

/**
 * Showcase for class base.Joiner.
 * 
 * @author Sky Ao
 *
 */
public class JoinerShowcase {

    @Test
    public void join() {
        Joiner joiner = Joiner.on(",");
        assertThat(joiner.join(new String[] { "a", "b", "c" })).isEqualTo("a,b,c");

        assertThat(joiner.skipNulls().join(new String[] { "a", "b", null, "c" })).isEqualTo("a,b,c");
        assertThat(joiner.useForNull("*").join(new String[] { "a", "b", null, "c" })).isEqualTo("a,b,*,c");
    }

    @Test
    public void appendTo() throws IOException {
        StringBuilder buiilder = new StringBuilder();
        buiilder.append("content=");

        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.appendTo(buiilder, new String[] { "a", "b", "c" });

        assertThat(buiilder.toString()).isEqualTo("content=a,b,c");
    }

    @Test
    public void mapJoiner() {
        Joiner.MapJoiner joiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, Integer> contents = new HashMap<String, Integer>();
        contents.put("a", 1);
        contents.put("b", 2);
        contents.put("c", 3);
        // how to control the order of key-value pairs
        assertThat(joiner.join(contents)).isEqualTo("b=2,c=3,a=1");
    }
}
