package io.github.bluething.java.threaddump.simulatethreadstatetransition.blockedtstate;

public class ThreadB implements Runnable {
    Thread thread;
    Foo foo;

    public ThreadB(Foo foo) {
        thread = new Thread(this);
        this.foo = foo;
    }

    @Override
    public void run() {
        foo.synchronizedMethod();
    }
}
