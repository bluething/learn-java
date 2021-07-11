package io.github.bluething.java.java8inaction.ch2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilteringApples {

    public static void main(String[] args) {

        List<Apple> inventory = Arrays.asList(
                new Apple(80,"green"),
                new Apple(155, "green"),
                new Apple(120, "red"));

        // I want to filtering green apples
        List<Apple> filteredApple = new ArrayList<>();

        // Traditional way
        filteredApple = filterGreenApples(inventory);
        System.out.println(filteredApple);

        // Requirement changes, I want filter color by query
        // duplicate filterGreenApples then change the condition -> don't do this!
        // after writing similar code, try to abstract
        filteredApple = filterApplesByColor(inventory, "red");
        System.out.println(filteredApple);

        // Requirement changes, I want filter by another criteria
        // create another method
        filteredApple = filterApplesByWeight(inventory, 155);
        System.out.println(filteredApple);

        // Remember DRY, we have 3 identical method!
        // create other method with all possible criteria
        // ugly
        // what if requirement changes again? create another method? -> don't do this!
        filteredApple = filterApples(inventory, "green", 100);
        System.out.println(filteredApple);

        // Make it good, how about the method can take any criteria
        filteredApple = filter(inventory, new AppleColorPredicate());
        System.out.println(filteredApple);
        filteredApple = filter(inventory, new AppleWeightPredicate());
        System.out.println(filteredApple);

        // Wait! Do I need create new class for every criteria? Do I need to create object every time I need to filter?
        // Verbose! A feature or concept that’s cumbersome to use will be avoided!
        // Try anon class
        filteredApple = filter(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return "red".equalsIgnoreCase(apple.getColor());
            }
        });
        System.out.println(filteredApple);

        // But anon class is confusing and take few lines to write it
        // Use lambda!
        filteredApple = filter(inventory, (Apple apple) -> "red".equalsIgnoreCase(apple.getColor()));
        System.out.println(filteredApple);

        // I don't want to work with apple, I have other inventory type? Let's abstract it
        List<Integer> integers = Arrays.asList(Integer.valueOf(10),
                Integer.valueOf(100),
                Integer.valueOf(1000));
        List<Integer> filteredIntegers = filter(integers, (Integer i) -> i > 100);
        System.out.println(filteredIntegers);

    }

    private static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> filteredApple = new ArrayList<>();
        for (Apple apple : inventory) {
            if("green".equalsIgnoreCase(apple.getColor())) {
                filteredApple.add(apple);
            }
        }
        return filteredApple;
    }

    private static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
        List<Apple> filteredApples = new ArrayList<>();
        for (Apple apple : inventory) {
            if(color.equalsIgnoreCase(apple.getColor())) {
                filteredApples.add(apple);
            }
        }
        return filteredApples;
    }

    private static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight){
        List<Apple> filteredApples = new ArrayList<>();
        for(Apple apple: inventory){
            if(apple.getWeight() > weight){
                filteredApples.add(apple);
            }
        }
        return filteredApples;
    }

    private static List<Apple> filterApples(List<Apple> inventory, String color, int weight) {
        List<Apple> filteredApples = new ArrayList<>();
        for(Apple apple: inventory){
            if(color.equalsIgnoreCase(apple.getColor()) || apple.getWeight() > weight){
                filteredApples.add(apple);
            }
        }
        return filteredApples;
    }

    private static List<Apple> filter(List<Apple> inventory, ApplePredicate applePredicate) {
        List<Apple> filteredApples = new ArrayList<>();
        for (Apple apple : inventory) {
            if (applePredicate.test(apple)) {
                filteredApples.add(apple);
            }
        }
        return filteredApples;
    }

    // predicate (a function that returns a boolean)
    interface ApplePredicate {
        public boolean test(Apple apple);
    }
    static class AppleWeightPredicate implements ApplePredicate {

        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() > 150;
        }
    }
    static class AppleColorPredicate implements ApplePredicate {
        public boolean test(Apple apple){
            return "green".equals(apple.getColor());
        }
    }
    static class AppleRedAndHeavyPredicate implements ApplePredicate {
        public boolean test(Apple apple){
            return "red".equals(apple.getColor())
                    && apple.getWeight() > 150;
        }
    }

    interface Predicate<T> {
        boolean test(T t);
    }
    public static <T> List<T> filter(List<T> inventory, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T t : inventory) {
            if (predicate.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    public static class Apple {
        private final int weight;
        private final String color;

        public Apple(int weight, String color) {
            this.weight = weight;
            this.color = color;
        }

        public int getWeight() {
            return weight;
        }

        public String getColor() {
            return color;
        }

        public String toString() {
            return "Apple{" +
                    "color='" + color + '\'' +
                    ", weight=" + weight +
                    '}';
        }
    }

}
