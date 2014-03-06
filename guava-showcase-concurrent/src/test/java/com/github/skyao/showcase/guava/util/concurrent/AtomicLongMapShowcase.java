package com.github.skyao.showcase.guava.util.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.google.common.util.concurrent.AtomicLongMap;

/**
 * show case for class util.concurrent.AtomicLongMap.
 * 
 * @author Sky Ao
 *
 */
public class AtomicLongMapShowcase {

    @Test
    public void showNormalUsage() {
        AtomicLongMap<String> map = AtomicLongMap.create();
        final String key1 = "key1";
        // if no exist value, return 0
        assertThat(map.getAndAdd(key1, 1000)).isEqualTo(0);
        // the value will be added to 1000
        assertThat(map.get(key1)).isEqualTo(1000);

        // add and get the added result
        assertThat(map.addAndGet(key1, 1000)).isEqualTo(2000);

        assertThat(map.decrementAndGet(key1)).isEqualTo(1999);
    }
}
