package io.github.bluething.java.threadmodel.interthreadcommunication.correct;

class Producer implements Runnable {
    Queue queue;
    Thread thread;

    public Producer(Queue queue) {
        thread = new Thread(this, "Producer");
        this.queue = queue;
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            queue.put(++i);
        }
    }
}
