package com.jd.jr.jdk8.book.chapter6;

import com.jd.jr.jdk8.book.entity.Dish;

import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * User: 吴海旭
 * Date: 2016-11-22
 * Time: 下午0:27
 * 6.3：分组
 */
public class Grouping {

    enum CaloricLevel { DIET, NORMAL, FAT }

    public static void main(String[] args) {
        System.out.println("Dishes grouped by type: " + groupDishesByType());
        System.out.println("Dishes grouped by caloric level: " + groupDishesByCaloricLevel());
        System.out.println("Dishes grouped by type and caloric level: " + groupDishedByTypeAndCaloricLevel());
        System.out.println("Count dishes in groups: " + countDishesInGroups());
        System.out.println("Most caloric dishes by type: " + mostCaloricDishesByType());
        System.out.println("Most caloric dishes by type: " + mostCaloricDishesByTypeWithoutOptionals());
        System.out.println("Sum calories by type: " + sumCaloriesByType());
        System.out.println("Caloric levels by type: " + caloricLevelsByType());
    }

    private static Map<Dish.Type, List<Dish>> groupDishesByType() {
        return Dish.menu.stream().collect(groupingBy(Dish::getType));
    }

    private static Map<CaloricLevel, List<Dish>> groupDishesByCaloricLevel() {
        return Dish.menu.stream().collect(groupingBy(t -> {
            if (t.getCalories() <= 400) return CaloricLevel.DIET;
            else if (t.getCalories() <= 700) return CaloricLevel.NORMAL;
            else return CaloricLevel.FAT;
        }));
    }

    private static Map<Dish.Type, Map<CaloricLevel, List<Dish>>> groupDishedByTypeAndCaloricLevel() {
        return Dish.menu.stream().collect(groupingBy(Dish::getType, groupingBy(t -> {
            if (t.getCalories() <= 400) return CaloricLevel.DIET;
            else if (t.getCalories() <= 700) return CaloricLevel.NORMAL;
            else return CaloricLevel.FAT;
        })));
    }

    private static Map<Dish.Type, Long> countDishesInGroups() {
        return Dish.menu.stream().collect(groupingBy(Dish::getType, counting()));
    }

    private static Map<Dish.Type, Optional<Dish>> mostCaloricDishesByType() {
        return Dish.menu.stream().collect(groupingBy(Dish::getType, maxBy(Comparator.comparing(Dish::getCalories))));
//        return Dish.menu.stream().collect(
//                groupingBy(Dish::getType,
//                        reducing((Dish d1, Dish d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)));
    }

    private static Map<Dish.Type, Dish> mostCaloricDishesByTypeWithoutOptionals() {
        return Dish.menu.stream().collect(
                groupingBy(Dish::getType,
                        collectingAndThen(
                                maxBy(Comparator.comparing(Dish::getCalories)), Optional::get)
                ));
    }

    private static Map<Dish.Type, Integer> sumCaloriesByType() {
        return Dish.menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
    }

    private static Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType() {
        return Dish.menu.stream().collect(
                groupingBy(Dish::getType,
                        mapping(t -> {
                            if (t.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (t.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        }, toSet())
                ));
    }
}
