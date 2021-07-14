package io.github.bluething.java.threadmodel.interthreadcommunication.correct;

public class Queue {
    int n;
    boolean valueSet = false;

    synchronized int get() {
        while (!valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }
        valueSet = false;
        System.out.println("Got " + n);
        notify();
        return n;
    }

    synchronized void put(int n) {
        while (valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }
        valueSet = true;
        this.n = n;
        System.out.println("Put " + n);
        notify();
    }
}
