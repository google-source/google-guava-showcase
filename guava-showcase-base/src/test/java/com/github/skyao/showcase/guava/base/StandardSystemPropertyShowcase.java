package com.github.skyao.showcase.guava.base;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.google.common.base.StandardSystemProperty;
import static com.google.common.base.StandardSystemProperty.*;
import com.google.common.collect.Lists;

/**
 * show case for class base.StandardSystemProperty.
 * @author Sky Ao
 *
 */
public class StandardSystemPropertyShowcase {

    @Test
    public void listAll() {
        for (StandardSystemProperty ssp : StandardSystemProperty.values()) {
            System.out.println(ssp.name() + " = " + ssp.value());
        }
    }

    @Test
    public void frequentlyUsed() {
        ArrayList<StandardSystemProperty> frequentlyUsed = Lists.newArrayList(FILE_SEPARATOR, LINE_SEPARATOR,
                PATH_SEPARATOR, JAVA_VERSION, JAVA_HOME, JAVA_CLASS_PATH, OS_NAME);
        for (StandardSystemProperty ssp : frequentlyUsed) {
            System.out.println(ssp.name() + " = " + ssp.value());
        }
    }
}
