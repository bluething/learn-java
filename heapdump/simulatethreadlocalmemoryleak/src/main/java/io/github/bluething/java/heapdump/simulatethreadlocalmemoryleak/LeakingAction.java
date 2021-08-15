package io.github.bluething.java.heapdump.simulatethreadlocalmemoryleak;

import java.util.stream.LongStream;

import static java.util.stream.Collectors.joining;

public class LeakingAction implements Runnable {
    private static final String PAD = "AAAAA";
    private static final int SIZE = 128 * 1024 / (PAD.length() + 1);

    private static final ThreadLocal<String> threadLocalValue = ThreadLocal.withInitial(() ->
            LongStream.range(0, SIZE)
                    .mapToObj(i -> PAD + i)
                    .collect(joining()));

    @Override
    public void run() {
        final String value = threadLocalValue.get();
        System.out.println(value.length());
        threadLocalValue.remove();
    }
}
