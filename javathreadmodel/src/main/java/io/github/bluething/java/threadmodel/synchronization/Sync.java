package io.github.bluething.java.threadmodel.synchronization;

public class Sync {
    public static void main(String[] args) {
        SynchronizedCallme callme = new SynchronizedCallme();
        SynchronizedCaller caller1 = new SynchronizedCaller(callme, "Hello");
        SynchronizedCaller caller2 = new SynchronizedCaller(callme, "Synchronized");
        SynchronizedCaller caller3 = new SynchronizedCaller(callme, "World");

        caller1.thread.start();
        caller2.thread.start();
        caller3.thread.start();

        try {
            caller1.thread.join();
            caller2.thread.join();
            caller3.thread.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}
