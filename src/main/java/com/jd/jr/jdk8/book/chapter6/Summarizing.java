package com.jd.jr.jdk8.book.chapter6;

import com.jd.jr.jdk8.book.entity.Dish;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.function.BinaryOperator;

import static com.jd.jr.jdk8.book.entity.Dish.menu;
import static java.util.stream.Collectors.*;
/**
 * User: 吴海旭
 * Date: 2016-11-22
 * Time: 上午11:43
 * 6.2：归约和汇总
 */
public class Summarizing {

    public static void main(String ... args) {
        System.out.println("Nr. of dishes: " + howManyDishes());
        System.out.println("The most caloric dish is: " + findMostCaloricDish());
        System.out.println("The most caloric dish is: " + findMostCaloricDishUsingComparator());
        System.out.println("Total calories in menu: " + calculateTotalCalories());
        System.out.println("Average calories in menu: " + calculateAverageCalories());
        System.out.println("Menu statistics: " + calculateMenuStatistics());
        System.out.println("Short menu: " + getShortMenu());
        System.out.println("Short menu comma separated: " + getShortMenuCommaSeparated());
    }

    private static long howManyDishes() {
        return menu.stream().collect(counting());
    }

    private static Dish findMostCaloricDish() {
        return menu.stream().collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)).get();
    }

    private static Dish findMostCaloricDishUsingComparator() {
        Comparator<Dish> dishComparator = Comparator.comparingInt(Dish::getCalories);
        BinaryOperator<Dish> dishBinaryOperator = BinaryOperator.maxBy(dishComparator);
        return menu.stream().collect(reducing(dishBinaryOperator)).get();
    }

    private static int calculateTotalCalories() {
        return menu.stream().collect(summingInt(Dish::getCalories));
    }

    private static double calculateAverageCalories() {
        return menu.stream().collect(averagingInt(Dish::getCalories));
    }

    private static IntSummaryStatistics calculateMenuStatistics() {
        return menu.stream().collect(summarizingInt(Dish::getCalories));
    }

    private static String getShortMenu() {
        return menu.stream().map(t -> t.getName()).collect(joining());
    }

    private static String getShortMenuCommaSeparated() {
        return menu.stream().map(t -> t.getName()).collect(joining(", "));
    }
}
