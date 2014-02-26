package com.github.skyao.showcase.guava.util.concurrent;

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
 * show case for class util.concurrent.ListenableFuture.
 * 
 * @author Sky Ao
 *
 */
public class ListenableFutureShowcase {

    @Test
    public void callback_onSuccess() throws Exception, ExecutionException {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() {
                return "a";
            }
        });
        Futures.addCallback(future, new FutureCallback<String>() {
            public void onSuccess(String explosion) {
                System.out.println("onSuccess");
            }

            public void onFailure(Throwable thrown) {

            }
        });

        future.get();
    }

    @Test(expectedExceptions = ExecutionException.class)
    public void callback_onFailure() throws Exception, ExecutionException {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() {
                throw new RuntimeException();
            }
        });
        Futures.addCallback(future, new FutureCallback<String>() {
            public void onSuccess(String explosion) {

            }

            public void onFailure(Throwable thrown) {
                System.out.println("onFailure");
            }
        });

        future.get();
    }
}
