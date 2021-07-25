package io.github.bluething.java.threaddump.simulatelongpausethread;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("Zzzz");
        sleep();
    }

    static void sleep() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
