package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.google.common.base.Objects;

/**
 * Showcase for class base.Objects.
 * <pre>
 * Reference to wiki page:
 *    http://code.google.com/p/guava-libraries/wiki/CommonObjectUtilitiesExplained
 * </pre>
 * @author Sky Ao
 *
 */
public class ObjectsShowcase {

    @Test
    public void equal() {
        assertThat(Objects.equal("a", "a")).isTrue();
        assertThat(Objects.equal(null, "a")).isFalse();
        assertThat(Objects.equal("a", null)).isFalse();
        assertThat(Objects.equal(null, null)).isTrue();
    }

    @Test
    public void hashCode_toString() {
        Object obj = new Object() {
            private int     a = 100;

            private String  b = "abc";

            private long    c = 1000000L;

            private boolean d = true;

            @Override
            public int hashCode() {
                return Objects.hashCode(a, b, c, d);
            }

            @Override
            public String toString() {
                return Objects.toStringHelper("ThisObject").add("a", a).add("b", b).add("c", c).add("d", d)
                        .omitNullValues().toString();
            }
        };

        assertThat(obj.hashCode()).isEqualTo(127500046);
        assertThat(obj.toString()).isEqualTo("ThisObject{a=100, b=abc, c=1000000, d=true}");
    }

    @Test
    public void firstNonNull() {
        assertThat(Objects.firstNonNull("a", "b")).isEqualTo("a");
        assertThat(Objects.firstNonNull("a", null)).isEqualTo("a");
        assertThat(Objects.firstNonNull(null, "b")).isEqualTo("b");

        try {
            assertThat(Objects.firstNonNull(null, null));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }
}
