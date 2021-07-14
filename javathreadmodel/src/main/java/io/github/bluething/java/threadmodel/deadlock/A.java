package io.github.bluething.java.threadmodel.deadlock;

public class A {
    synchronized void foo(B b) {
        String name = Thread.currentThread().getName();
        System.out.println(name + " entered A.foo()");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("A interrupted");
        }

        System.out.println(name + " trying to call last() B");
        b.last();
    }
    synchronized void last() {
        System.out.println("Inside last() A");
    }
}
