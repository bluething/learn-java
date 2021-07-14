package io.github.bluething.java.threadmodel.deadlock;

public class Deadlock implements Runnable {
    A a = new A();
    B b = new B();
    Thread thread;

    public Deadlock() {
        Thread.currentThread().setName("MainThread");
        thread = new Thread(this, "Racing thread");
    }

    void deadlockStart() {
        thread.start();
        a.foo(b);
        System.out.println("Back in main thread");
    }

    @Override
    public void run() {
        b.bar(a);
        System.out.println("Back in other thread");
    }

    public static void main(String[] args) {
        Deadlock deadlock = new Deadlock();
        deadlock.deadlockStart();
    }
}
