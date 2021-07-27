package io.github.bluething.java.threaddump.simulatethreadstatetransition.waitingstate;

public class WaitingStateDemo {
    public static void main(String[] args) {
        ThreadA threadA = new ThreadA("Thread A from main");
        threadA.thread.start();

        synchronized (threadA.thread) {
            try {
                threadA.thread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Just for breakpoint");
    }
}
