package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.google.common.base.Defaults;

/**
 * Showcase for class base.Defaults.
 * 
 * @author Sky Ao
 *
 */
public class DefaultsShowcase {

    @Test
    public void listDefaults() {
        assertThat(Defaults.defaultValue(int.class).intValue()).isEqualTo(0);
        assertThat(Defaults.defaultValue(short.class).intValue()).isEqualTo(0);
        assertThat(Defaults.defaultValue(long.class).intValue()).isEqualTo(0);
        assertThat(Defaults.defaultValue(byte.class).intValue()).isEqualTo(0);

        assertThat(Defaults.defaultValue(boolean.class)).isFalse();
        assertThat(Defaults.defaultValue(char.class)).isEqualTo((char) 0);

        assertThat(Defaults.defaultValue(float.class).doubleValue()).isEqualTo(0);
        assertThat(Defaults.defaultValue(double.class).doubleValue()).isEqualTo(0);
        
        // when should we use this class??
    }
}
