package io.github.bluething.java.threadmodel;

public class MultipleThreadWithJoinDemo {

    public static void main(String[] args) {
        MultipleThreadDemo.NewThread newThread1 = new MultipleThreadDemo.NewThread("one");
        MultipleThreadDemo.NewThread newThread2 = new MultipleThreadDemo.NewThread("two");
        MultipleThreadDemo.NewThread newThread3 = new MultipleThreadDemo.NewThread("three");

        newThread1.thread.start();
        newThread2.thread.start();
        newThread3.thread.start();

        System.out.println("Thread one is alive " + newThread1.thread.isAlive());
        System.out.println("Thread two is alive " + newThread2.thread.isAlive());
        System.out.println("Thread three is alive " + newThread3.thread.isAlive());

        try {
            System.out.println("Waiting thread to finish");
            newThread1.thread.join();
            newThread2.thread.join();
            newThread3.thread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }

        System.out.println("Thread one is alive " + newThread1.thread.isAlive());
        System.out.println("Thread two is alive " + newThread2.thread.isAlive());
        System.out.println("Thread three is alive " + newThread3.thread.isAlive());

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
