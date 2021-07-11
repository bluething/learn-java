package io.github.bluething.java.java8inaction.ch1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FilteringApples {

    public static void main(String[] args) {
        List<Apple> apples = Arrays.asList(new Apple(80,"green"),
                new Apple(155, "green"),
                new Apple(120, "red"));

        List<Apple> greenApples = new ArrayList<>();
        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        greenApples = filterGreenApple(apples);
        System.out.println(greenApples);
        greenApples = filterApples(apples, FilteringApples::isGreenApple);
        System.out.println(greenApples);

        List<Apple> heavyApples = new ArrayList<>();
        // [Apple{color='green', weight=155}]
        heavyApples = filterHeavyApples(apples);
        System.out.println(heavyApples);
        heavyApples = filterApples(apples, FilteringApples::isHeavyApple);
        System.out.println(heavyApples);

        List<Apple> lightAndGreenApples = filterApples(apples,
                (Apple apple) -> apple.getWeight() < 100 && "Green".equalsIgnoreCase(apple.getColor()));
        System.out.println(lightAndGreenApples);

    }

    public static List<Apple> filterGreenApple(List<Apple> apples) {
        List<Apple> filteredApples = new ArrayList<>();
        for (Apple apple : apples) {
            if("green".equalsIgnoreCase(apple.getColor())) {
                filteredApples.add(apple);
            }
        }
        return filteredApples;
    }

    public static boolean isGreenApple(Apple apple) {
        return "Green".equalsIgnoreCase(apple.getColor());
    }
    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }

    public static List<Apple> filterApples(List<Apple> apples, Predicate<Apple> predicate) {
        List<Apple> filteredApples = new ArrayList<>();
        for (Apple apple : apples) {
            if(predicate.test(apple)) {
                filteredApples.add(apple);
            }
        }
        return filteredApples;
    }

    public static List<Apple> filterHeavyApples(List<Apple> apples) {
        List<Apple> filteredApples = new ArrayList<>();
        for (Apple apple : apples) {
            if(apple.getWeight() > 150) {
                filteredApples.add(apple);
            }
        }
        return filteredApples;
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
