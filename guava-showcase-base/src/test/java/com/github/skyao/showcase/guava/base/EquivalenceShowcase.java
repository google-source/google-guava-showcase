package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.google.common.base.Equivalence;

/**
 * Showcase for class base.Equivalence.
 * 
 * @author Sky Ao
 * 
 * question: I don't know when and how to use this class.
 * 
 * No idea, no tips, no example....
 * 
 * TBD
 */
public class EquivalenceShowcase {

    private Equivalence<Object> equalsEquivalence   = Equivalence.equals();

    private Equivalence<Object> identityEquivalence = Equivalence.identity();

    @Test
    public void equivalent() {
        assertThat(equalsEquivalence.equivalent(new String("a"), new String("a"))).isTrue();
        assertThat(identityEquivalence.equivalent(new String("a"), new String("a"))).isFalse();
        
        assertThat(equalsEquivalence.equivalent(null, null)).isTrue();
        assertThat(identityEquivalence.equivalent(null, null)).isTrue();
    }

    @Test
    public void hash() {
        assertThat(equalsEquivalence.hash(new String("a"))).isEqualTo(equalsEquivalence.hash(new String("a")));
        assertThat(identityEquivalence.hash(new String("a"))).isNotEqualTo(identityEquivalence.hash(new String("a")));
    }
}
