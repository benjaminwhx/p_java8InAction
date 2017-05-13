package com.jd.jr.jdk8.book.chapter5;

import com.jd.jr.jdk8.book.entity.Dish;

import java.util.Optional;

/**
 * User: 吴海旭
 * Date: 2016-11-14
 * Time: 下午8:15
 * 5.3: 查找和匹配
 */
public class Finding {

    public static void main(String[] args) {
        if (isVegetarianFriendlyMenu()) {
            System.out.println("Vegetarian Friendly");
        }

        System.out.println(isHealthyMenu());

        System.out.println(isHealthyMenu2());

        Optional<Dish> firstVegetarianDish = findFirstVegetarianDish();
        firstVegetarianDish.ifPresent(d -> System.out.println(d.getName()));

        Optional<Dish> vegetarianDish = findVegetarianDish();
        vegetarianDish.ifPresent(d -> System.out.println(d.getName()));
    }

    private static boolean isVegetarianFriendlyMenu() {
        // anyMatch至少匹配一个元素
        return Dish.menu.stream().anyMatch(Dish::isVegetarian);
    }

    private static boolean isHealthyMenu() {
        // allMatch匹配所有商品是否满足条件
        return Dish.menu.stream().allMatch(d -> d.getCalories() < 1000);
    }

    private static boolean isHealthyMenu2() {
        // noneMatch匹配所有商品是否都不满足条件
        return Dish.menu.stream().noneMatch(d -> d.getCalories() >= 1000);
    }

    private static Optional<Dish> findFirstVegetarianDish() {
        // findFirst找到第一个元素返回
        return Dish.menu.stream().filter(Dish::isVegetarian).findFirst();
    }

    private static Optional<Dish> findVegetarianDish() {
        // findAny随机找一个满足条件的
        return Dish.menu.stream().filter(Dish::isVegetarian).findAny();
    }

}
