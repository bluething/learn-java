package io.github.bluething.java.threadmodel.interthreadcommunication.incorrect;

class Queue {
    int n;

    synchronized int get() {
        System.out.println("Got " + n);
        return n;
    }

    synchronized void put(int n) {
        this.n = n;
        System.out.println("Put " + n);
    }
}
