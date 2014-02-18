package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.google.common.base.Throwables;

/**
 * Guava's Throwables utility can frequently simplify dealing with exceptions.
 * <pre>
 * Reference to wiki page:
 *    http://code.google.com/p/guava-libraries/wiki/ThrowablesExplained
 * </pre>
 * @author Sky Ao
 *
 */
public class ThrowablesShowcase {
    private static Logger logger = LoggerFactory.getLogger(ThrowablesShowcase.class);

    /**
     * Sometimes, we don't want to handle the exception, but we need to do something 
     * when the exception happens.
     * <pre>
     * So we need to 
     * 1. catch exception
     * 2. do something we want 
     * 3. throw the exception
     * </pre>
     */
    @Test(expectedExceptions = RuntimeException.class)
    public void propagate_doSomethingWhenExceptionHappens() {
        try {
            throw new Exception();
        } catch (Throwable t) {
            // we want to do something, typically write an error log 
            logger.error("here a error o", t);

            // We can't directly throw it like this
            //throw t;

            // Throwables.propagate() will help us
            Throwables.propagate(t);
        }
    }

    @Test
    public void propagate_HandleCheckedException() {
        Exception original = new Exception();
        try {
            Throwables.propagate(original);
        } catch (Throwable t) {
            // for checked exception, Throwables.propagate() will wrap it with a RuntimeException
            // see source code: throw new RuntimeException(throwable);
            assertThat(t).isInstanceOf(RuntimeException.class);
            assertThat(t.getCause()).isSameAs(original);
        }
    }

    @Test
    public void propagate_HandleRuntimeException() {

        Exception original = new IllegalArgumentException();
        try {
            Throwables.propagate(original);
        } catch (Throwable t) {
            // for RuntimeException, Throwables.propagate() will directly throw it
            assertThat(t).isSameAs(original);
        }
    }

    @Test
    public void propagate_HandleError() {
        Error original = new Error();
        try {
            Throwables.propagate(original);
        } catch (Throwable t) {
            // for Error, Throwables.propagate() will directly throw it
            assertThat(t).isSameAs(original);
        }
    }

    @Test
    public void propagateIfInstanceOf_match() {
        Exception original = new IllegalArgumentException();
        try {
            Throwables.propagateIfInstanceOf(original, IllegalArgumentException.class);
        } catch (Throwable t) {
            // throw original throwable if match
            assertThat(t).isSameAs(original);
        }
    }

    @Test
    public void propagateIfInstanceOf_notMatch() {
        Exception original = new IllegalArgumentException();

        // no Throwable is thrown if not match
        Throwables.propagateIfInstanceOf(original, IndexOutOfBoundsException.class);
    }

    @Test
    public void propagateIfPossible_HandleRuntimeException() {
        Exception original = new IllegalArgumentException();
        try {
            Throwables.propagateIfPossible(original);
        } catch (Throwable t) {
            // for RuntimeException, Throwables.propagate() will directly throw it
            assertThat(t).isSameAs(original);
        }
    }

    @Test
    public void propagateIfPossible_HandleError() {
        Error original = new Error();
        try {
            Throwables.propagateIfPossible(original);
        } catch (Throwable t) {
            // for RuntimeException, Throwables.propagate() will directly throw it
            assertThat(t).isSameAs(original);
        }
    }

    @Test
    public void propagateIfPossible_HandleCheckedException() {
        Exception original = new Exception();
        // won't throw for checked exception
        Throwables.propagateIfPossible(original);
    }

    @Test
    public void getRootCause() {
        Exception original = new Exception();
        Throwable rootCause = Throwables.getRootCause(original);
        assertThat(rootCause).isSameAs(original);

        original = new Exception();
        rootCause = Throwables.getRootCause(new RuntimeException(new Exception(original)));
        assertThat(rootCause).isSameAs(original);
    }

    @Test
    public void getCausalChain() {
        Exception original = new Exception();
        List<Throwable> causes = Throwables.getCausalChain(original);
        assertThat(causes.size()).isEqualTo(1);
        assertThat(causes.get(0)).isSameAs(original);

        original = new Exception();
        Exception wrap1 = new Exception(original);
        Exception wrap2 = new RuntimeException(wrap1);
        causes = Throwables.getCausalChain(wrap2);
        assertThat(causes.size()).isEqualTo(3);
        // the result list is [wrap2, wrap1, original]
        assertThat(causes.get(0)).isSameAs(wrap2);
        assertThat(causes.get(1)).isSameAs(wrap1);
        assertThat(causes.get(2)).isSameAs(original);
    }

    @Test
    public void getStackTraceAsString() {
        Exception original = new Exception("original");
        Exception wrap1 = new Exception("wrap1", original);
        Exception wrap2 = new RuntimeException("wrap2", wrap1);

        // see the output in console
        System.out.println("********  getStackTraceAsString output start  *********");
        System.out.println(Throwables.getStackTraceAsString(wrap2));
        System.out.println("********  getStackTraceAsString output end  *********");
    }
}
