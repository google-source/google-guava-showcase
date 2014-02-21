import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ThreadFactory;

import org.testng.annotations.Test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * show case for class util.concurrent.ThreadFactoryBuilder.
 * 
 * @author Sky Ao
 *
 */
public class ThreadFactoryBuilderShowcase {

    @Test
    public void test() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Worker-%d").setDaemon(false).build();
        for (int i = 0; i < 100; i++) {
            Thread newThread = threadFactory.newThread(new Runnable() {
                public void run() {

                }
            });
            assertThat(newThread.isDaemon()).isFalse();
            assertThat(newThread.getName()).isEqualTo("Worker-" + i);
        }
    }
}
