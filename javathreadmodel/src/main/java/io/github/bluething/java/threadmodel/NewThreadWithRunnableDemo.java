package io.github.bluething.java.threadmodel;

public class NewThreadWithRunnableDemo {

    static class NewThreadWithRunnable implements Runnable {
        Thread thread;

        NewThreadWithRunnable() {
            this.thread = new Thread(this, "Demo thread");
            System.out.println("Child thread " + thread);
        }

        @Override
        public void run() {

        }
    }
}
