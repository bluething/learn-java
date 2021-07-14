package io.github.bluething.java.threadmodel.synchronization;

public class CallerWithSynchronizedStatement implements Runnable {
    Thread thread;
    Callme callme;
    String msg;

    public CallerWithSynchronizedStatement(Callme callme, String msg) {
        thread = new Thread(this);
        this.callme = callme;
        this.msg = msg;
    }

    @Override
    public void run() {
        synchronized (callme) {
            callme.call(msg);
        }
    }
}