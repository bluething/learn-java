package io.github.bluething.java.threadmodel.interthreadcommunication.incorrect;

public class Consumer implements Runnable {
    Queue queue;
    Thread thread;

    public Consumer(Queue queue) {
        thread = new Thread(this, "Consumer");
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            queue.get();
        }
    }
}
