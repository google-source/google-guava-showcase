package com.github.skyao.showcase.guava.collect;

/**
 * Showcase for class base.Ordering.
 * <pre>
 * Reference to wiki page:
 *    http://code.google.com/p/guava-libraries/wiki/OrderingExplained
 * </pre>
 * @author Sky Ao
 *
 */
public class OrderingShowcase {

    private static class User {

        private String name;

        private int    age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
