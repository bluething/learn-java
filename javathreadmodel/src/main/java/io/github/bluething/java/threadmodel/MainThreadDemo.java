package io.github.bluething.java.threadmodel;

public class MainThreadDemo {
    public static void main(String[] args) {
        Thread thread = Thread.currentThread();

        System.out.println("Current thread " + thread);

        thread.setName("Main demo thread");
        System.out.println("Current thread " + thread);

        try {
            for (int i = 1; i <= 5; i++) {
                System.out.println(i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException iEx) {
            System.out.println("Main thread interrupted");
        }
    }
}
