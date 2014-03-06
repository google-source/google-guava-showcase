package com.github.skyao.showcase.guava.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.testng.annotations.Test;

import com.google.common.util.concurrent.ExecutionList;

/**
 * show case for class util.concurrent.ExcutionList.
 * 
 * @author Sky Ao
 *
 */
public class ExcutionListShowcase {

    @Test
    public void execute() throws Exception {
        ExecutionList el = new ExecutionList();
        Executor executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
            final String taskName = "task" + (i + 1);
            el.add(new Runnable() {
                @Override
                public void run() {
                    // please check the print result, the order is not exactly in the order of add()
                    System.out.println("run " + taskName);
                }
            }, executor);
        }

        el.execute();
    }
}
