package com.github.skyao.showcase.guava.util.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Runnables;

/**
 * show case for class util.concurrent.MoreExecutors.
 * 
 * @author Sky Ao
 *
 */
public class MoreExecutorsShowcase {

    @Test
    public void addDelayedShutdownHook() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        MoreExecutors.addDelayedShutdownHook(service, 100, TimeUnit.MILLISECONDS);

        // how to see the affect?
    }

    @Test
    public void platformThreadFactory() {
        ThreadFactory threadFactory = MoreExecutors.platformThreadFactory();
        Thread newThread = threadFactory.newThread(Runnables.doNothing());
        // pool-3-thread-1
        assertThat(newThread.getName()).startsWith("pool-").contains("-thread-");
    }

    @Test
    public void sameThreadExecutor() throws Exception {
        ListeningExecutorService threadExecutor = MoreExecutors.sameThreadExecutor();
        final Thread mainThread = Thread.currentThread();

        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                assertThat(Thread.currentThread()).isSameAs(mainThread);
            }
        });
        threadExecutor.shutdown();
        threadExecutor.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Test
    public void listeningDecorator() throws Exception {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors
                .newFixedThreadPool(2));
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "abc";
            }

        };

        ListenableFuture<String> listenableFuture = listeningExecutorService.submit(task);
        assertThat(listenableFuture.get()).isEqualTo("abc");
    }
}
