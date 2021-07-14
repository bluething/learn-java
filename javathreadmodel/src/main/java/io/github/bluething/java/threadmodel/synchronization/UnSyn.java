package io.github.bluething.java.threadmodel.synchronization;

public class UnSyn {
    public static void main(String[] args) {
        Callme callme = new Callme();
        Caller caller1 = new Caller(callme, "Hello");
        Caller caller2 = new Caller(callme, "Synchronized");
        Caller caller3 = new Caller(callme, "World");

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
