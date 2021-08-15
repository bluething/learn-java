package io.github.bluething.java.heapdump.simulatethreadlocalmemoryleak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadLocalLeakRunner {
    private static final int POOL_SIZE = 50;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(POOL_SIZE);

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        System.out.println("Press any key to continue");
        System.in.read();

        final List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < POOL_SIZE; i++) {
            final Future<?> future = threadPool.submit(new LeakingAction());
            futures.add(future);
        }

        for (Future<?> future : futures) {
            future.get();
        }

        System.out.println("Work Complete");
    }
}
