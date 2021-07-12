package io.github.bluething.java.threadmodel;

public class NewThreadWithExtendingThreadDemo {

    static class NewThreadWithExtendingThread extends Thread {

        NewThreadWithExtendingThread() {
            super("Demo thread");
            System.out.println("Child thread: " + this);
        }

        @Override
        public void run() {
            try {
                for (int i = 5; i > 0; i--) {
                    System.out.println("Child thread " + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("Child thread interrupted");
            }
            System.out.println("Exiting child thread");
        }
    }
}
