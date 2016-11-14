package chapter5;

import entity.Dish;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * User: 吴海旭
 * Date: 2016-11-14
 * Time: 下午8:52
 * 5.4: 归约
 */
public class Reducing {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(3,4,5,1,2);
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        System.out.println(sum);    // 15

        int sum2 = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum2);   // 15

        int max = numbers.stream().reduce(0, Integer::max);
        System.out.println(max);    // 5

        Optional<Integer> min = numbers.stream().reduce(Integer::min);
        min.ifPresent(System.out::println); // 1

        Optional<Integer> caloriesSum = Dish.menu.stream().map(Dish::getCalories).reduce(Integer::sum);
        caloriesSum.ifPresent(System.out::println); // 4300
    }
}
