package com.github.skyao.showcase.guava.base;

import java.lang.ref.Reference;
import java.util.concurrent.atomic.AtomicBoolean;

import org.testng.annotations.Test;

import com.google.common.base.FinalizablePhantomReference;
import com.google.common.base.FinalizableReferenceQueue;
import com.google.common.base.FinalizableWeakReference;

/**
 * show case for class base.FinalizableReferenceQueue.
 * 
 * @author Sky Ao
 *
 */
public class FinalizableReferenceQueueShowcase {

    @Test(timeOut = 10 * 1000)
    public void phantomReference() {
        final AtomicBoolean isFinalized = new AtomicBoolean(false);
        String content = new String("");

        FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
        @SuppressWarnings("unused")
        Reference<String> reference = new FinalizablePhantomReference<String>(content, queue) {

            @Override
            public void finalizeReferent() {
                // we can do something here when this content object is destroyed by GC
                System.out.println("calling finalizeReferent");

                isFinalized.set(true);
            }

        };
        // set it to null so that this object will be GC by JVM
        content = null;

        waitUtilObjectIsFinilized(isFinalized);
    }

    @Test(timeOut = 10 * 1000)
    public void weakReference() {
        final AtomicBoolean isFinalized = new AtomicBoolean(false);
        String content = new String("");

        FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
        @SuppressWarnings("unused")
        Reference<String> reference = new FinalizableWeakReference<String>(content, queue) {

            @Override
            public void finalizeReferent() {
                // we can do something here when this content object is destroyed by GC
                System.out.println("calling finalizeReferent");

                isFinalized.set(true);
            }

        };
        // set it to null so that this object will be GC by JVM
        content = null;

        waitUtilObjectIsFinilized(isFinalized);
    }

    @Test(timeOut = 10 * 1000)
    public void softReference() {
        // similar to soft reference

        // Just change this line
        //Reference<String> reference = new FinalizableSoftReference<String>(content, queue) {  

        // but it is hard to simulate the case that JVM executes GC because of no memory 
    }

    private void waitUtilObjectIsFinilized(final AtomicBoolean isFinalized) {
        while (true) {
            try {
                Thread.sleep(100);
                // ask JVM to do GC so that the content object will be GC
                System.gc();
            } catch (InterruptedException e) {
            }

            if (isFinalized.get()) {
                break;
            }
        }
    }
}
