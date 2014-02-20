package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Stopwatch;

/**
 * Showcase for class base.CharMatcher.
 * <pre>
 * Reference to wiki page:
 *    http://code.google.com/p/guava-libraries/wiki/StringsExplained#CharMatcher
 * </pre>
 * @author Sky Ao
 *
 */
public class CharMatcherShowcase {

    @Test
    public void replaceFrom() {
        assertThat(CharMatcher.WHITESPACE.replaceFrom("a b c d", '-')).isEqualTo("a-b-c-d");
        assertThat(CharMatcher.JAVA_DIGIT.replaceFrom("11-11-abc", '*')).isEqualTo("**-**-abc");
        assertThat(CharMatcher.JAVA_LETTER.replaceFrom("aaa-bbb-ccc-1234", '*')).isEqualTo("***-***-***-1234");
        assertThat(CharMatcher.JAVA_LETTER_OR_DIGIT.replaceFrom("aaa-111-ccc", '*')).isEqualTo("***-***-***");
    }

    @Test
    public void collapseFrom() {
        assertThat(CharMatcher.WHITESPACE.collapseFrom("a   b  c    d", ' ')).isEqualTo("a b c d");
        assertThat(CharMatcher.JAVA_DIGIT.collapseFrom("11-11-abc", '*')).isEqualTo("*-*-abc");
        assertThat(CharMatcher.JAVA_LETTER.collapseFrom("aaa-bbb-ccc-1234", '*')).isEqualTo("*-*-*-1234");
        assertThat(CharMatcher.JAVA_LETTER_OR_DIGIT.collapseFrom("aaa-111-ccc", '*')).isEqualTo("*-*-*");
    }

    @Test
    public void matchesAllOf() {
        assertThat(CharMatcher.WHITESPACE.matchesAllOf(" \r\n\t")).isTrue();
        assertThat(CharMatcher.JAVA_DIGIT.matchesAllOf("1234567890")).isTrue();
        assertThat(CharMatcher.JAVA_LETTER.matchesAllOf("abcABC")).isTrue();
        assertThat(CharMatcher.JAVA_LETTER_OR_DIGIT.matchesAllOf("1234567890abcABC")).isTrue();
    }

    @Test
    public void removeFrom() {
        assertThat(CharMatcher.WHITESPACE.removeFrom("a   b  c    d\r\n")).isEqualTo("abcd");
        assertThat(CharMatcher.WHITESPACE.removeFrom("we are good man, you are not.\r\n")).isEqualTo(
                "wearegoodman,youarenot.");
        assertThat(CharMatcher.BREAKING_WHITESPACE.removeFrom("we are good man, you are not.\r\n")).isEqualTo(
                "wearegoodman,youarenot.");

        assertThat(CharMatcher.JAVA_DIGIT.removeFrom("1a2b3c4d")).isEqualTo("abcd");
        assertThat(CharMatcher.JAVA_LETTER.removeFrom("1a2b3c4d")).isEqualTo("1234");
        assertThat(CharMatcher.JAVA_LETTER_OR_DIGIT.removeFrom("1a2b3c4d")).isEqualTo("");
    }

    @Test
    public void retainFrom() {
        assertThat(CharMatcher.JAVA_DIGIT.retainFrom("tel:+8613900000000")).isEqualTo("8613900000000");
        assertThat(CharMatcher.JAVA_LETTER.retainFrom("1a2b3c4d")).isEqualTo("abcd");
        assertThat(CharMatcher.JAVA_LETTER_OR_DIGIT.retainFrom("abss-1245-af3d")).isEqualTo("abss1245af3d");
    }

    @Test
    public void trimFrom() {
        assertThat(CharMatcher.JAVA_DIGIT.trimFrom("111abc222")).isEqualTo("abc");
        assertThat(CharMatcher.JAVA_LETTER.trimFrom("abc12345efg")).isEqualTo("12345");
        assertThat(CharMatcher.JAVA_LETTER_OR_DIGIT.trimFrom("abss---af3d")).isEqualTo("---");
    }

    @Test
    public void is() {
        assertThat(CharMatcher.is(' ').replaceFrom("a b c", '*')).isEqualTo("a*b*c");
        assertThat(CharMatcher.is('1').replaceFrom("123", '*')).isEqualTo("*23");
        assertThat(CharMatcher.is('+').removeFrom("+8613900000000")).isEqualTo("8613900000000");
    }

    @Test
    public void isNot() {
        assertThat(CharMatcher.isNot('a').replaceFrom("abca", '*')).isEqualTo("a**a");
        assertThat(CharMatcher.isNot('1').retainFrom("12311456")).isEqualTo("23456");
        assertThat(CharMatcher.isNot('+').retainFrom("+8613900000000")).isEqualTo("8613900000000");
    }

    @Test
    public void anyOf() {
        assertThat(CharMatcher.anyOf("abc").replaceFrom("abcdefg1234", '*')).isEqualTo("***defg1234");
        assertThat(CharMatcher.anyOf("1234").retainFrom("12311456")).isEqualTo("123114");
    }

    @Test
    public void inRange() {
        assertThat(CharMatcher.inRange('a', 'c').replaceFrom("abcdefg1234", '*')).isEqualTo("***defg1234");
        assertThat(CharMatcher.inRange('1', '4').retainFrom("12311456")).isEqualTo("123114");
    }

    @Test
    public void matches() {
        assertThat(CharMatcher.JAVA_DIGIT.matches('1')).isTrue();
        assertThat(CharMatcher.JAVA_LETTER.matches('A')).isTrue();
    }

    @Test
    public void negate() {
        assertThat(CharMatcher.JAVA_LETTER.retainFrom("1a2b3c4d")).isEqualTo("abcd");
        assertThat(CharMatcher.JAVA_LETTER.negate().retainFrom("1a2b3c4d")).isEqualTo("1234");
    }

    @Test
    public void and() {
        assertThat(CharMatcher.JAVA_LETTER.and(CharMatcher.JAVA_LETTER_OR_DIGIT).retainFrom("1a2b3c4d")).isEqualTo(
                "abcd");
        assertThat(CharMatcher.JAVA_LETTER.and(CharMatcher.ASCII).retainFrom("1a2b3c4d")).isEqualTo("abcd");
    }

    @Test
    public void or() {
        assertThat(CharMatcher.is('a').or(CharMatcher.is('b')).retainFrom("1a2b3c4d")).isEqualTo("ab");

        assertThat(CharMatcher.JAVA_DIGIT.or(CharMatcher.is('+')).retainFrom("tel: +8613900000000")).isEqualTo(
                "+8613900000000");
    }

    @Test
    public void precomputed() {
        CharMatcher notpreComputedMatch = CharMatcher.inRange('1', '3').or(CharMatcher.inRange('6', '8'))
                .or(CharMatcher.inRange('a', 'c')).or(CharMatcher.inRange('e', 'f')).or(CharMatcher.is('+'));

        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 10000; i++) {
            notpreComputedMatch.replaceFrom("aaa-111-ccc", '*');
        }
        long timeNotPreComputed = stopwatch.elapsed(TimeUnit.NANOSECONDS);

        CharMatcher preComputedMatch = notpreComputedMatch.precomputed();
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        for (int i = 0; i < 10000; i++) {
            preComputedMatch.replaceFrom("aaa-111-ccc", '*');
        }
        long timePreComputed = stopwatch2.elapsed(TimeUnit.NANOSECONDS);

        System.out.println("timeNotPreComputed = " + timeNotPreComputed + ", timePreComputed = " + timePreComputed);
        assertThat(timeNotPreComputed).isGreaterThan(timePreComputed);
    }
}
