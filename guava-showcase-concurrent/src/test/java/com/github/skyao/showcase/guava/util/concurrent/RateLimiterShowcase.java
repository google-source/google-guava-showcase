package com.github.skyao.showcase.guava.util.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.RateLimiter;

/**
 * show case for class util.concurrent.RateLimiter.
 * 
 * @author Sky Ao
 *
 */
public class RateLimiterShowcase {

    @Test
    public void normalLimiter() {
        final double permitsPerSecond = 10; // so for each invoke, the wait time will be 100 ms
        RateLimiter limiter = RateLimiter.create(permitsPerSecond);
        final int expectedIntevalInMs = (int) (1 * 1000 / permitsPerSecond);

        for (int i = 0; i < 20; i++) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            int timeSpentInMs = (int) (limiter.acquire() * 1000);
            long elapsedTimeInMs = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            System.out.println("i=" + i + ", elapsedTimeInMs=" + elapsedTimeInMs + ", timeSpentInMs=" + timeSpentInMs);

            if (i == 0) {
                // first time, timeSpentInMs = 0, elapsedTimeInMs = 0
                assertThat(timeSpentInMs).isBetween(0, 0 + 10);
                assertThat(elapsedTimeInMs).isBetween(0L, 0 + 10L);
            } else if (i == 1) {
                // for the second time, the timeSpentInMs and elapsedTimeInMs are always be 50+ or 60+
                // why?
                assertThat(timeSpentInMs).isBetween(expectedIntevalInMs / 2 - 20, expectedIntevalInMs / 2 + 20);
                assertThat(elapsedTimeInMs).isBetween(expectedIntevalInMs / 2 - 20L, expectedIntevalInMs / 2 + 20L);
            } else {
                // later, timeSpentInMs and elapsedTimeInMs should be stable to near expectedIntevalInMs
                assertThat(timeSpentInMs).isBetween(expectedIntevalInMs - 20, expectedIntevalInMs + 20);
                assertThat(elapsedTimeInMs).isBetween(expectedIntevalInMs - 20L, expectedIntevalInMs + 20L);
            }
        }
    }

    @Test
    public void limiterWithWarnuptime() {
        final double permitsPerSecond = 10; // so for each invoke, the wait time will be 100 ms
        final int warmupTimeInMs = 1000;
        RateLimiter limiter = RateLimiter.create(permitsPerSecond, warmupTimeInMs, TimeUnit.MILLISECONDS);
        final int expectedIntevalInMs = (int) (1 * 1000 / permitsPerSecond);

        int totalPassedTimeInMs = 0;
        for (int i = 0; i < 20; i++) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            int timeSpentInMs = (int) (limiter.acquire() * 1000);
            totalPassedTimeInMs += timeSpentInMs;
            long elapsedTimeInMs = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            System.out.println(totalPassedTimeInMs);
            System.out.println("i=" + i + ", elapsedTimeInMs=" + elapsedTimeInMs + ", timeSpentInMs=" + timeSpentInMs);

            if (i == 0) {
                // first time, timeSpentInMs = 0, elapsedTimeInMs = 0
                //assertThat(timeSpentInMs).isBetween(0, 0 + 10);
                //assertThat(elapsedTimeInMs).isBetween(0L, 0 + 10L);
            } else if (timeSpentInMs <= warmupTimeInMs) {
                // in warm up time, the timeSpentInMs and elapsedTimeInMs should greater than expectedIntevalInMs
                assertThat(timeSpentInMs).isGreaterThanOrEqualTo(expectedIntevalInMs - 20);
                assertThat(elapsedTimeInMs).isGreaterThanOrEqualTo(expectedIntevalInMs - 20);
            } else {
                // after warm up, timeSpentInMs and elapsedTimeInMs should be stable to near expectedIntevalInMs
                assertThat(timeSpentInMs).isBetween(expectedIntevalInMs - 20, expectedIntevalInMs + 20);
                assertThat(elapsedTimeInMs).isBetween(expectedIntevalInMs - 20L, expectedIntevalInMs + 20L);
            }
        }
    }

    @Test
    public void dont_block_work_thread() throws Exception {
        // we have a thread pool
        final ExecutorService executorService = Executors.newFixedThreadPool(1);

        // we want to execute some task which should use RateLimier
        final RateLimiter limiter = RateLimiter.create(10);

        final int taskCount = 10;
        final CountDownLatch latch = new CountDownLatch(taskCount);
        for (int i = 0; i < taskCount; i++) {
            final int id = i + 1;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    Stopwatch stopwatch = Stopwatch.createStarted();

                    // if we use limiter.acquire() here, the work thread will be blocked
                    // if the permitsPerSecond is very small, for example, only one permit in 30 seconds
                    // then all of our work thread will be blocked here
                    //limiter.acquire();

                    // we call tryAcquire()
                    boolean acquireResult = limiter.tryAcquire();

                    if (acquireResult) {
                        // allow to execute
                        // do something in this task ......
                        System.out.println("task " + id + " is done!");
                        latch.countDown();
                    } else {
                        // limited by rate
                        System.out.println("task " + id + " should be executed later!");

                        // add to executorService again
                        // in production, here we should use a queue to save the task
                        // and the task should marked with a timestamp that when this task start to run
                        // and a background thread to check this queue and get the ready task out and submit to executor
                        // one question: how can I know when should next task is acceptable by the rate limiter? 
                        executorService.submit(this);
                    }

                    // work thread is not blocked
                    assertThat(stopwatch.elapsed(TimeUnit.MILLISECONDS)).isEqualTo(0);
                }
            };
            executorService.submit(task);
        }
        latch.await();
    }
}
