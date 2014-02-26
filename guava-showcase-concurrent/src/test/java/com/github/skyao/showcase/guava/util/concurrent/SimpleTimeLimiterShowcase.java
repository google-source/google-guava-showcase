package com.github.skyao.showcase.guava.util.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.testng.annotations.Test;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;

/**
 * show case for class util.concurrent.SimpleTimeLimiter.
 * 
 * @author Sky Ao
 *
 */
public class SimpleTimeLimiterShowcase {

    @Test
    public void newProxy() throws Exception {
        SimpleTimeLimiter stl = new SimpleTimeLimiter();
        final AtomicBoolean isInterrupted = new AtomicBoolean(false);
        Task task = new Task() {
            @Override
            public void run() throws Exception {
                isInterrupted.set(true);
                Thread.sleep(200);
                // if the thread is not interrupted, this line will be executed and marked to false
                System.out.println("the work thread is not interrupted");
                isInterrupted.set(false);
            }
        };
        Task proxy = stl.newProxy(task, Task.class, 50, TimeUnit.MILLISECONDS);

        try {
            proxy.run();
        } catch (Exception e) {
            e.printStackTrace();
            // wait for work thread finishing sleep
            Thread.sleep(200);
            assertThat(isInterrupted.get()).isTrue();

            assertThat(e).isInstanceOf(UncheckedTimeoutException.class);
            assertThat(e.getCause()).isInstanceOf(TimeoutException.class);
        }
    }

    public static interface Task {
        public void run() throws Exception;
    }

    @Test
    public void callWithTimeout() throws Exception {
        SimpleTimeLimiter stl = new SimpleTimeLimiter();
        final AtomicBoolean isInterrupted = new AtomicBoolean(false);

        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                isInterrupted.set(true);
                Thread.sleep(200);
                // if the thread is not interrupted, this line will be executed and marked to false
                isInterrupted.set(false);

                return "";
            }
        };

        try {
            stl.callWithTimeout(task, 50, TimeUnit.MILLISECONDS, true);
        } catch (Exception e) {
            e.printStackTrace();
            // wait for work thread finishing sleep
            Thread.sleep(200);
            assertThat(isInterrupted.get()).isTrue();
            assertThat(e).isInstanceOf(UncheckedTimeoutException.class);
            assertThat(e.getCause()).isInstanceOf(TimeoutException.class);
        }
    }
}
