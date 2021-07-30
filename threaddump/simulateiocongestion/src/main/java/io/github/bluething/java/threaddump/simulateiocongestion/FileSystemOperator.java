package io.github.bluething.java.threaddump.simulateiocongestion;

import net.smacke.jaydio.DirectRandomAccessFile;

import java.io.File;
import java.io.IOException;
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
                futures.add(executorService.submit(new DirectIOReaderCallable()));
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

class DirectIOReaderCallable implements Callable {

    @Override
    public Object call() throws Exception {
        int bufferSize = 1 << 20;
        byte[] buf = new byte[bufferSize];
        File input = new File("");
        long totalByteRead = 0;
        try(DirectRandomAccessFile randomAccessFile = new DirectRandomAccessFile(input, "r", bufferSize)) {
            int remaining = 0;
            while(randomAccessFile.getFilePointer() < randomAccessFile.length()){
                remaining = (int) Math.min(bufferSize, randomAccessFile.length() - randomAccessFile.getFilePointer());
                randomAccessFile.read(buf, 0, remaining);
                totalByteRead = totalByteRead + remaining;
            }
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
        return Thread.currentThread().getName() + " done reading " + totalByteRead + " bytes";
    }
}
