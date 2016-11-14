package chapter4;

import entity.Dish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * User: 吴海旭
 * Date: 2016-11-14
 * Time: 下午2:18
 * 在Java7和Java8种分别得到集合的方法
 */
public class StreamBasic {

    public static void main(String[] args) {
        // Java 7
        getLowCaloricDishesNamesInJava7(Dish.menu).forEach(System.out::println);

        System.out.println("====");

        // Java 8
        getLowCaloricDishesNamesInJava8(Dish.menu).forEach(System.out::println);
    }

    public static List<String> getLowCaloricDishesNamesInJava7(List<Dish> dishes) {
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for (Dish d : dishes) {
            if (d.getCalories() < 400) {
                lowCaloricDishes.add(d);
            }
        }
        List<String> lowCaloricDishesNames = new ArrayList<>();
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Integer.compare(o1.getCalories(), o2.getCalories());
            }
        });
        for (Dish d : lowCaloricDishes) {
            lowCaloricDishesNames.add(d.getName());
        }
        return lowCaloricDishesNames;
    }

    public static List<String> getLowCaloricDishesNamesInJava8(List<Dish> dishes) {
        return dishes.stream()
                .filter(dish -> {
                    System.out.println("filtering: " + dish.getName());
                    return dish.getCalories() < 400;
                })
                .sorted(comparing(Dish::getCalories))
                .map(d -> {
                    System.out.println("mapping: " + d.getName());
                    return d.getName();
                })
                .collect(toList());
    }
}
