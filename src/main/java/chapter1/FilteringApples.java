package chapter1;

import entity.Apple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * User: 吴海旭
 * Date: 2016-11-13
 * Time: 下午4:14
 */
public class FilteringApples {

    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(new Apple(80,"green"),
                new Apple(155, "green"),
                new Apple(120, "red"));

        List<Apple> greenApples = filterApples(inventory, FilteringApples::isGreenApple);
        // [Apple{weight=80, color='green'}, Apple{weight=155, color='green'}]
        System.out.println(greenApples);

        List<Apple> heavyApples = filterApples(inventory, FilteringApples::isHeavyApple);
        // [Apple{weight=155, color='green'}]
        System.out.println(heavyApples);

        List<Apple> greenApples2 = filterApples(inventory, (Apple a) -> "green".equals(a.getColor()));
        // [Apple{weight=80, color='green'}, Apple{weight=155, color='green'}]
        System.out.println(greenApples2);

        List<Apple> heavyApples2 = filterApples(inventory, (Apple a) -> (a.getWeight() > 150));
        // [Apple{weight=155, color='green'}]
        System.out.println(heavyApples2);

        List<Apple> heavyApples3 = filterApples(inventory, (apple -> apple.getWeight() > 150));
        // [Apple{weight=155, color='green'}]
        System.out.println(heavyApples3);

        List<Apple> otherApples = filterApples(inventory, (Apple a) -> a.getWeight() > 150 && "brown".equals(a.getColor()));
        // []
        System.out.println(otherApples);
    }

    /**
     * 根据传入的苹果过滤出绿色的苹果并返回
     * @param inventory 库存的苹果
     * @return 绿色的苹果
     */
    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (isGreenApple(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * 根据传入的苹果过滤出超重的苹果并返回
     * @param inventory 库存的苹果
     * @return 超重的苹果
     */
    public static List<Apple> filterHeavyApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (isHeavyApple(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * 根据传入的苹果和谓语条件过滤出对应的苹果
     * @param inventory 库存的苹果
     * @param predicate 谓语,condition作用
     * @return 符合条件的苹果
     */
    public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> predicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (predicate.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static boolean isGreenApple(Apple apple) {
        return "green".equals(apple.getColor());
    }

    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }
}
