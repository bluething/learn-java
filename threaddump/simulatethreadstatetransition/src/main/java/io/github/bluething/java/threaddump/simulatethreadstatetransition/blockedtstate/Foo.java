package io.github.bluething.java.threaddump.simulatethreadstatetransition.blockedtstate;

public class Foo {
    synchronized void synchronizedMethod() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
