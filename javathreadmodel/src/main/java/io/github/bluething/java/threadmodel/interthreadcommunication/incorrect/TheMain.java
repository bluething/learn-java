package io.github.bluething.java.threadmodel.interthreadcommunication.incorrect;

public class TheMain {
    public static void main(String[] args) {
        Queue queue = new Queue();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.thread.start();
        consumer.thread.start();

        System.out.println("Ctrl+C to stop");
    }
}
