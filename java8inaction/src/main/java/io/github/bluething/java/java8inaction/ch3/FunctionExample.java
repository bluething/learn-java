package io.github.bluething.java.java8inaction.ch3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionExample {

    public static void main(String[] args) {
        List<Integer> lengthOfString = map(Arrays.asList("lambdas", "in", "action"), (String s) -> s.length());
        System.out.println(lengthOfString);
    }

    // Functional interface: Function<T, R>
    // Function descriptor: T -> R
    // T or R can be bound only to reference types
    public interface Function<T, R> {
        R apply(T t);
    }
    public static <T, R>List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();
        for (T t : list) {
            result.add(f.apply(t));
        }
        return result;
    }

}
