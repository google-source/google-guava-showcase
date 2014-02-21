package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.regex.Pattern;

import org.testng.annotations.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * show case for class base.Splitter.
 * 
 * @author Sky Ao
 *
 */
public class SplitterShowcase {

    @Test
    public void split() {
        String content = "a,b,, c ,,d";

        Iterable<String> splitResult = Splitter.on(',').split(content);
        // no change
        assertThat(readableResult(splitResult)).isEqualTo("a,b,, c ,,d");

        splitResult = Splitter.on(',').trimResults().split(content);
        // no whitespace 
        assertThat(readableResult(splitResult)).isEqualTo("a,b,,c,,d");

        splitResult = Splitter.on(',').trimResults().omitEmptyStrings().split(content);
        // no whitespace and empty string
        assertThat(readableResult(splitResult)).isEqualTo("a,b,c,d");
    }

    @Test
    public void split_charMatcher() {
        String content = "a,b;, c :,d";

        Iterable<String> splitResult = Splitter.on(CharMatcher.anyOf(",;:")).trimResults().omitEmptyStrings()
                .split(content);
        assertThat(readableResult(splitResult)).isEqualTo("a,b,c,d");
    }

    @Test
    public void split_string() {
        String content = "a&&b&& c &&d";

        Iterable<String> splitResult = Splitter.on("&&").trimResults().omitEmptyStrings().split(content);
        assertThat(readableResult(splitResult)).isEqualTo("a,b,c,d");
    }

    @Test
    public void split_pattern() {
        String content = "a\r\nb\nc\nd";

        Pattern separatorPattern = Pattern.compile("\r?\n");
        Iterable<String> splitResult = Splitter.on(separatorPattern).trimResults().omitEmptyStrings().split(content);
        assertThat(readableResult(splitResult)).isEqualTo("a,b,c,d");
    }

    @Test
    public void split_onPattern() {
        String content = "a\r\nb\nc\nd";

        Iterable<String> splitResult = Splitter.onPattern("\r?\n").trimResults().omitEmptyStrings().split(content);
        assertThat(readableResult(splitResult)).isEqualTo("a,b,c,d");
    }

    @Test
    public void split_fixedLength() {
        assertThat(readableResult(Splitter.fixedLength(3).split("abcdefgh"))).isEqualTo("abc,def,gh");
    }

    @Test
    public void split_limit() {
        String content = " keyword  commandword  message messageconent";
        content = "keyword  message messageconent";

        List<String> resultList = Splitter.on(CharMatcher.WHITESPACE).limit(3).splitToList(content);
        if (resultList.size() == 3) {

        }

        assertThat(readableResult(Splitter.fixedLength(3).split("abcdefgh"))).isEqualTo("abc,def,gh");
    }

    private String readableResult(Iterable<String> splitResult) {
        return Joiner.on(',').useForNull("null").join(splitResult);
    }
}
