package com.jd.jr.jdk8.book.chapter5;

import com.jd.jr.jdk8.book.entity.Dish;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * User: 吴海旭
 * Date: 2016-11-16
 * Time: 下午2:59
 * 5.6：数值流
 */
public class NumericStreams {

    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(3, 4, 5, 1, 2);
        Arrays.stream(numbers.toArray()).forEach(System.out::print);
        System.out.println();

        int sumCalories = Dish.menu.stream().mapToInt(Dish::getCalories).sum();
        System.out.println(sumCalories);

        OptionalInt maxCalories = Dish.menu.stream()
                .mapToInt(Dish::getCalories)
                .max();
        int max;
        if (maxCalories.isPresent()) {
            max = maxCalories.getAsInt();
        } else {
            // 设置一个默认值
            max = 1;
        }
//        maxCalories.orElse(1);

        long rangeCount = IntStream.range(1, 100).filter(i -> i % 2 == 0).count();  // range不包含结束值
        long rangeClosedCount = IntStream.rangeClosed(1, 100).filter(i -> i % 2 == 0).count();  // rangeClosed包含结束值
        System.out.println(rangeCount + " " + rangeClosedCount);    // 49 50

        // 查找勾股数，方法1
        Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                                .filter(b -> Math.sqrt(a*a + b*b) % 1 == 0).boxed()
                                .map(b -> new int[]{a, b, (int)Math.sqrt(a*a + b*b)})
                        );
        pythagoreanTriples.forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));

        // 查找勾股数，方法2，上面的方法1用了两次平方根
        Stream<int[]> pythagoreanTriples2 = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                        .mapToObj(b -> new int[]{a, b, (int)Math.sqrt(a*a + b*b)})
                        .filter(b -> b[2] % 1 == 0));
        pythagoreanTriples2.forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
    }
}
