package io.github.bluething.java.threaddump.simulatethreadstatetransition.waitingstate;

public class ThreadA implements Runnable {
    Thread thread;
    String name;

    public ThreadA(String name) {
        this.thread = new Thread(this, name);
        this.name = name;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notify();
        }
    }
}
