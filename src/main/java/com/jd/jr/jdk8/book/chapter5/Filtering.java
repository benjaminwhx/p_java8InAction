package com.jd.jr.jdk8.book.chapter5;

import com.jd.jr.jdk8.book.entity.Dish;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: 吴海旭
 * Date: 2016-11-14
 * Time: 下午3:45
 * 5.1：筛选和切片
 */
public class Filtering {

    public static void main(String[] args) {
        // 1.用谓词筛选(filter())
        List<Dish> vegetarianMenu = Dish.menu.stream()
                .filter(Dish::isVegetarian)
                .collect(Collectors.toList());

        vegetarianMenu.forEach(System.out::println);

        System.out.println("===============");

        // 2.筛选不重复的偶数(使用distinct())
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);

        System.out.println("===============");

        // 3.截短流(limit())
        Dish.menu.stream()
                .filter(d -> d.getCalories() > 300)
                .limit(3)
                .forEach(System.out::println);

        System.out.println("===============");

        // 4.跳过元素(skip())
        Dish.menu.stream()
                .filter(d -> d.getCalories() > 300)
                .skip(2)
                .forEach(System.out::println);
    }
}
