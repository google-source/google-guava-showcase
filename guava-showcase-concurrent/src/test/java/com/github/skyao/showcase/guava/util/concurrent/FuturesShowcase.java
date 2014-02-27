package com.github.skyao.showcase.guava.util.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.FutureFallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.UncheckedExecutionException;

/**
 * show case for class util.concurrent.Futures.
 * 
 * @author Sky Ao
 *
 */
public class FuturesShowcase {

    @SuppressWarnings("serial")
    public static class FutrueGetException extends Exception {

        public FutrueGetException(String message) {
            super(message);
        }

        public FutrueGetException(String message, Throwable t) {
            super(message, t);
        }
    }

    @Test
    public void makeChecked() throws Exception, ExecutionException {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
        final RuntimeException exception = new RuntimeException("runtime exception");
        ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() {
                throw exception;
            }
        });

        CheckedFuture<String, FutrueGetException> checkedFuture = Futures.makeChecked(future,
                new Function<Exception, FutrueGetException>() {
                    @Override
                    public FutrueGetException apply(Exception input) {
                        return new FutrueGetException("wrap", input);
                    }
                });

        try {
            checkedFuture.checkedGet();
        } catch (Throwable t) {
            //t.printStackTrace();
            // IOException will be wrapped to FutrueGetException
            assertThat(t).isInstanceOf(FutrueGetException.class);
            assertThat(t.getCause()).isInstanceOf(ExecutionException.class);
            assertThat(t.getCause().getCause()).isSameAs(exception);
        }
    }

    @Test
    public void immediateFuture() throws Exception, ExecutionException {
        String value = "abc";
        assertThat(Futures.immediateFuture(value).get()).isSameAs(value);
    }

    @Test
    public void immediateCheckedFuture() throws Exception, ExecutionException {
        String value = "abc";
        assertThat(Futures.immediateCheckedFuture(value).get()).isSameAs(value);
    }

    public void immediateFailedFuture() throws Exception {

        IOException throwable = new IOException();
        try {
            Futures.immediateFailedFuture(throwable).get();
        } catch (Throwable t) {
            // Calling get() will immediately throw the provided Throwable wrapped in an ExecutionException.
            assertThat(t).isInstanceOf(ExecutionException.class);
            assertThat(t.getCause()).isSameAs(throwable);
        }
    }

    @Test
    public void immediateCancelledFuture() throws Exception, ExecutionException {
        assertThat(Futures.immediateCancelledFuture().isCancelled()).isTrue();
    }

    @Test
    public void immediateFailedCheckedFuture() throws Exception, ExecutionException {
        FutrueGetException throwable = new FutrueGetException("");
        try {
            Futures.immediateFailedCheckedFuture(throwable).checkedGet();
        } catch (Throwable t) {
            // calling checkedGet() will throw the provided exception itself.
            assertThat(t).isSameAs(throwable);
        }
    }

    @Test
    public void withFallback() throws Exception, ExecutionException {
        ListenableFuture<Integer> fetchCounterFuture = Futures.immediateFailedFuture(new RuntimeException());
        // Falling back to a zero counter in case an exception happens when
        // processing the RPC to fetch counters.
        ListenableFuture<Integer> faultTolerantFuture = Futures.withFallback(fetchCounterFuture,
                new FutureFallback<Integer>() {
                    public ListenableFuture<Integer> create(Throwable t) {
                        // Returning "0" as the default for the counter when the
                        // exception happens.
                        return Futures.immediateFuture(0);
                    }
                });

        assertThat(faultTolerantFuture.get()).isEqualTo(0);
    }

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

        // Note!
        // This is dangerous if the callback operation is not so fast and it will hold the main thread
        // see new show case to avoid it
    }

    @Test
    public void addCallback_withAloneCallbackExecutor() throws Exception, ExecutionException {
        final Thread[] involvedThreads = new Thread[3];
        // record main thread
        involvedThreads[0] = Thread.currentThread();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListeningExecutorService callbackService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
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
        }, callbackService);
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
        // the callback thread will not be main thread or work thread
        assertThat(mainThread).isNotSameAs(callbackThread);
        assertThat(callbackThread).isNotSameAs(workThread);
    }

    @Test
    public void get_success() throws Exception, ExecutionException {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
        ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() {
                return "abc";
            }
        });

        Futures.get(future, FutrueGetException.class);
    }

    @Test
    public void get_throwExpectedException() throws Exception, ExecutionException {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
        final IOException exception = new IOException("some message");
        ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() throws IOException {
                throw exception;
            }
        });

        try {
            Futures.get(future, FutrueGetException.class);
        } catch (Throwable t) {
            // IOException will be wrapped to FutrueGetException
            assertThat(t).isInstanceOf(FutrueGetException.class);
            assertThat(t.getCause()).isSameAs(exception);
        }
    }

    @Test
    public void get_throwRuntimeException() throws Exception, ExecutionException {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
        final RuntimeException exception = new RuntimeException();
        ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() {
                throw exception;
            }
        });

        try {
            Futures.get(future, FutrueGetException.class);
        } catch (Throwable t) {
            // RuntimeException will be wrapped to UncheckedExecutionException
            assertThat(t).isInstanceOf(UncheckedExecutionException.class);
            assertThat(t.getCause()).isSameAs(exception);
        }
    }

    @Test
    public void get_throwError() throws Exception, ExecutionException {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
        final Error error = new Error();
        ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() {
                throw error;
            }
        });

        try {
            Futures.get(future, FutrueGetException.class);
        } catch (Throwable t) {
            // Error will be wrapped to ExecutionError
            assertThat(t).isInstanceOf(ExecutionError.class);
            assertThat(t.getCause()).isSameAs(error);
        }
    }

    @Test
    public void getUnchecked_success() throws Exception, ExecutionException {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
        ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() {
                return "abc";
            }
        });

        Futures.getUnchecked(future);
    }
}
