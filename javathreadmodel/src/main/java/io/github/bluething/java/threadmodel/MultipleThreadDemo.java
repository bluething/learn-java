package io.github.bluething.java.threadmodel;

public class MultipleThreadDemo {

    public static void main(String[] args) {
        NewThread newThread1 = new NewThread("one");
        NewThread newThread2 = new NewThread("two");
        NewThread newThread3 = new NewThread("three");

        newThread1.thread.start();
        newThread2.thread.start();
        newThread3.thread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }

        System.out.println("Main thread exiting");
    }

    static class NewThread implements Runnable {
        Thread thread;
        String name;

        public NewThread(String name) {
            thread = new Thread(this, name);
            this.name = name;
        }

        @Override
        public void run() {
            try {
                for (int i = 5; i > 0; i--) {
                    System.out.println(name + " : " + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println(name + " interrupted");
            }

            System.out.println(name + " exiting");
        }
    }
}
