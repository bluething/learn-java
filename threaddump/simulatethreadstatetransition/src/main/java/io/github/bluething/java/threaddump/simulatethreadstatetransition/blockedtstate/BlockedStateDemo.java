package io.github.bluething.java.threaddump.simulatethreadstatetransition.blockedtstate;

public class BlockedStateDemo {
    public static void main(String[] args) {
        Foo foo = new Foo();

        ThreadB threadBOne = new ThreadB(foo);
        ThreadB threadBTwo = new ThreadB(foo);

        threadBOne.thread.start();
        threadBTwo.thread.start();

        System.out.println("Just for breakpoint");
    }
}
