package io.github.bluething.java.threadmodel;

public class NewThreadCreateAndStartDemo {

    public static void main(String[] args) {
        NewThreadCreateAndStart newThreadCreateAndStart = createAndStart();

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

    public static NewThreadCreateAndStart createAndStart() {
        NewThreadCreateAndStart newThreadCreateAndStart = new NewThreadCreateAndStart();
        newThreadCreateAndStart.thread.start();
        return newThreadCreateAndStart;
    }

    static class NewThreadCreateAndStart implements Runnable {
        Thread thread;

        NewThreadCreateAndStart() {
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
