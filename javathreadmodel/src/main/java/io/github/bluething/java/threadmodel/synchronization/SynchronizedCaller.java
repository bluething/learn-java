package io.github.bluething.java.threadmodel.synchronization;

public class SynchronizedCaller implements Runnable {
    Thread thread;
    SynchronizedCallme callme;
    String msg;

    public SynchronizedCaller(SynchronizedCallme callme, String msg) {
        thread = new Thread(this);
        this.callme = callme;
        this.msg = msg;
    }

    @Override
    public void run() {
        callme.call(msg);
    }
}
