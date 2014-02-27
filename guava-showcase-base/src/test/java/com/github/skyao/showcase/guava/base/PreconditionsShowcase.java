package com.github.skyao.showcase.guava.base;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Showcase for class base.Preconditions.
 * <pre>
 * Reference to wiki page:
 *    http://code.google.com/p/guava-libraries/wiki/PreconditionsExplained
 * </pre>
 * @author Sky Ao
 *
 */
public class PreconditionsShowcase {

    public void checkNotNull(String a) {
        // normal code
        if (a == null) {
            throw new NullPointerException("parameter a should not be null");
        }

        // guava style code
        Preconditions.checkNotNull(a, "parameter a should not be null");

        // real business code here
        // ...
    }

    public void execute(int a) {
        Preconditions.checkArgument(a >= 0, "parameter a should bigger than 0: a = %s", a);

        //real business code here
    }

    public void execute(long startTime, long endTime) {
        Preconditions.checkArgument(startTime <= endTime,
                "parameter endTime should bigger or equals to parameter startTime: startTime = %s, endTime = %s",
                startTime, endTime);

        //real business code here
    }

    private int status = -1;

    public void execute() {
        Preconditions.checkState(status >= 0, "property status should bigger than 0: status = %s", status);

        //real business code here
    }

    private List<Integer> idList = Lists.newArrayList(1, 2, 3, 4, 5, 6);

    public Integer getElementByIndex(int index) {
        return idList.get(Preconditions.checkElementIndex(index, idList.size()));
    }
}
