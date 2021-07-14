package io.github.bluething.java.threadmodel.interthreadcommunication.correct;

class Consumer implements Runnable {
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
