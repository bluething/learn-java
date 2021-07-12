package io.github.bluething.java.threadmodel;

public class NewThreadWithRunnableDemo {
    public static void main(String[] args) {
        NewThreadWithRunnable newThreadWithRunnable = new NewThreadWithRunnable();
        newThreadWithRunnable.thread.start();

        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Main thread" + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }
        System.out.println("Main thread exiting");
    }

    static class NewThreadWithRunnable implements Runnable {
        Thread thread;

        NewThreadWithRunnable() {
            this.thread = new Thread(this, "Demo thread");
            System.out.println("Child thread " + thread);
        }

        @Override
        public void run() {
            try {
                for (int i = 5; i > 0; i--) {
                    System.out.println("Child thread " + i);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println("Child interrupted");
            }
            System.out.println("Exiting child thread");
        }
    }
}
