package io.github.bluething.java.threadmodel.synchronization;

public class SyncWithSynchronizedStatement {
    public static void main(String[] args) {
        Callme callme = new Callme();
        CallerWithSynchronizedStatement caller1 = new CallerWithSynchronizedStatement(callme, "Hello");
        CallerWithSynchronizedStatement caller2 = new CallerWithSynchronizedStatement(callme, "Synchronized");
        CallerWithSynchronizedStatement caller3 = new CallerWithSynchronizedStatement(callme, "World");

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
