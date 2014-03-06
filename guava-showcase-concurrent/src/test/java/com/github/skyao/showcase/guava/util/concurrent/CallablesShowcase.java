package com.github.skyao.showcase.guava.util.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.Callable;

import org.testng.annotations.Test;

import com.google.common.util.concurrent.Callables;

/**
 * show case for class util.concurrent.Callables.
 * 
 * @author Sky Ao
 *
 */
public class CallablesShowcase {

    @Test
    public void showNormalUsage() throws Exception {
        final String value = "abc";
        Callable<String> callable = Callables.returning(value);
        assertThat(callable.call()).isSameAs(value);
    }
}
