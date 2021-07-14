package io.github.bluething.java.threadmodel.synchronization;

public class Caller implements Runnable {
    Thread thread;
    Callme callme;
    String msg;

    public Caller(Callme callme, String msg) {
        thread = new Thread(this);
        this.callme = callme;
        this.msg = msg;
    }

    @Override
    public void run() {
        callme.call(msg);
    }
}
