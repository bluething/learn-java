package io.github.bluething.java.threadmodel.deadlock;

public class B {
    synchronized  void bar(A a) {
        String name = Thread.currentThread().getName();
        System.out.println(name + " entered B.bar()");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("B interrupted");
        }

        System.out.println(name + "trying to call last() A");
        a.last();
    }
    synchronized void last() {
        System.out.println("Inside last() B");
    }
}
