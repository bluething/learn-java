package io.github.bluething.java.threaddump.simulateiocongestion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FileSystemOperator {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Please specify the operation type (read/write) and the number of workers");
        }

        int numOfWorker = Integer.parseInt(args[1]);
        ExecutorService executorService = Executors.newFixedThreadPool(numOfWorker);
        List<Future> futures = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numOfWorker; i++) {
            if (args[0].equals("read")) {

            } else if (args[0].equals("write")){

            }
        }

        System.out.println("Number of thread submitted " + String.valueOf(futures.size()) + "\n");

        for (Future future : futures) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long totalExecutionTime = endTime - startTime;
        System.out.println("Total execution time " + totalExecutionTime + " ms");

        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
            executorService.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("\n[" + Thread.currentThread().getName() + "] | terminating program...");
        }
    }
}
