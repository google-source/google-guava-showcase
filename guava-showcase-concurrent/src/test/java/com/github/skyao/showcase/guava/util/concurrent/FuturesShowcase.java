package com.github.skyao.showcase.guava.util.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.testng.annotations.Test;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * show case for class util.concurrent.Futures.
 * 
 * @author Sky Ao
 *
 */
public class FuturesShowcase {

    @Test
    public void addCallback_beforeTaskCompleted() throws Exception, ExecutionException {
        final Thread[] involvedThreads = new Thread[3];
        // record main thread
        involvedThreads[0] = Thread.currentThread();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() {
                // record work thread
                involvedThreads[1] = Thread.currentThread();

                // wait, so that the addCallback() will be called before this task is completed
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                return "a";
            }
        });
        Futures.addCallback(future, new FutureCallback<String>() {
            public void onSuccess(String explosion) {
                // record callbackThread
                involvedThreads[2] = Thread.currentThread();
                System.out.println("onSuccess");
            }

            public void onFailure(Throwable thrown) {

            }
        });
        future.get();

        // wait for the callback finished
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        final Thread mainThread = involvedThreads[0];
        final Thread workThread = involvedThreads[1];
        final Thread callbackThread = involvedThreads[2];
        System.out.println("mainThread=" + mainThread.getName() + ", workThread=" + workThread.getName()
                + ", callbackThread=" + callbackThread.getName());
        assertThat(mainThread).isNotSameAs(workThread);
        assertThat(mainThread).isNotSameAs(callbackThread);
        // in this case, the work thread will do callback, so workThread is also callbackThread
        assertThat(workThread).isSameAs(callbackThread);
    }

    @Test
    public void addCallback_afterTaskCompleted() throws Exception, ExecutionException {
        final Thread[] involvedThreads = new Thread[3];
        // record main thread
        involvedThreads[0] = Thread.currentThread();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() {
                // record work thread
                involvedThreads[1] = Thread.currentThread();
                return "a";
            }
        });
        // wait, so that the addCallback() will be called after this task is already completed
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        Futures.addCallback(future, new FutureCallback<String>() {
            public void onSuccess(String explosion) {
                // record callbackThread
                involvedThreads[2] = Thread.currentThread();
                System.out.println("onSuccess");
            }

            public void onFailure(Throwable thrown) {

            }
        });
        future.get();
        // wait for the callback finished
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        final Thread mainThread = involvedThreads[0];
        final Thread workThread = involvedThreads[1];
        final Thread callbackThread = involvedThreads[2];
        System.out.println("mainThread=" + mainThread.getName() + ", workThread=" + workThread.getName()
                + ", callbackThread=" + callbackThread.getName());
        assertThat(mainThread).isNotSameAs(workThread);
        // in main thread will do callback, so mainThread == callbackThread
        assertThat(mainThread).isSameAs(callbackThread);
    }

}
