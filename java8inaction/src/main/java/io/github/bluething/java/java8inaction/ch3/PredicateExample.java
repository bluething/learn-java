package io.github.bluething.java.java8inaction.ch3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PredicateExample {

    public static void main(String[] args) {
        Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
        List<String> allStrings = Arrays.asList("", "short string", "long string");
        List<String> nonEmptyStrings = filter(allStrings, nonEmptyStringPredicate);
        System.out.println(nonEmptyStrings);
    }

    // Functional interface: Predicate<T>
    // Function descriptor: T -> boolean
    public interface Predicate<T> {
        boolean test(T t);
    }
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if(predicate.test(t)) {
                result.add(t);
            }
        }
        return result;
    }
}
