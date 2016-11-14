package chapter3;

import entity.Apple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

/**
 * User: 吴海旭
 * Date: 2016-11-13
 * Time: 下午7:41
 */
public class Lambdas {

    public static void main(String[] args) {
        // 什么也不接受
        Runnable r = () -> {};

        Function<String, Integer> stringToInteger = (String s) -> Integer.parseInt(s);
        Function<String, Integer> stringToInteger2 = s -> Integer.parseInt(s);
        // 上面那2句可以写成
        Function<String, Integer> stringToInteger3 = Integer::parseInt;

        List<String> stringList = Arrays.asList("a", "d", "c");
        // lambda表达式1
        stringList.sort((String s1, String s2) -> s1.compareTo(s2));
        // lambda表达式2
        stringList.sort((s1, s2) -> s1.compareTo(s2));
        // 方法引用
        stringList.sort(Comparator.comparing(String::toString));
        // 方法引用2
        stringList.sort(String::compareToIgnoreCase);
        System.out.println(stringList); // [a, c, d]

        BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element);
        BiPredicate<List<String>, String> contains2 = List::contains;

        // 构造函数引用
        Supplier<Apple> c1 = Apple::new;    // Supplier函数接口适用于无参构造方法
        Apple a1 = c1.get();
        Supplier<Apple> c2 = () -> new Apple();
        Apple a2 = c2.get();    // c2和c1都会调用Apple()这个无参构造函数

        Function<Integer, Apple> c3 = Apple::new;   // Function函数接口适用于有一个参数的构造方法
        Apple a3 = c3.apply(20);
        Function<Integer, Apple> c4 = weight -> new Apple(weight);
        Apple a4 = c4.apply(20);    // c4和c3将会调用Apple(int weight)这个构造函数

        BiFunction<Integer, String, Apple> c5 = Apple::new; // BuFunction函数接口适用于有两个参数的构造方法
        Apple a5 = c5.apply(20, "red");
        BiFunction<Integer, String, Apple> c6 = (weight, color) -> new Apple(weight, color);
        Apple a6 = c6.apply(20, "red"); // c6和c5将会调用Apple(int weight, String color)这个构造函数

        // 将构造函数引用传递给map方法
        List<Integer> weights = Arrays.asList(7, 3, 4, 10);
        List<Apple> apples = map(weights, Apple::new);

        // 谓词复合
        Predicate<Apple> redApples = apple -> "red".equals(apple.getColor());
        Predicate<Apple> notRedApples = redApples.negate(); // 非
        Predicate<Apple> redAndHeavyApples = redApples.and(apple -> apple.getWeight() > 150);   // 与
        Predicate<Apple> redAndHeavyApplesOrGreen = redAndHeavyApples.or(a -> "green".equals(a.getColor()));    // 或

        // 函数复合
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = f.andThen(g);    // 组合函数，先执行f，再执行g
        int andThenResult = h.apply(1);
        System.out.println(andThenResult); // 4
        Function<Integer, Integer> t = f.compose(g);    // 复合函数，先执行g，再执行f
        int composeResult = t.apply(1);
        System.out.println(composeResult);  // 3
        Function<Integer, Integer> identity = Function.identity();  // 返回输入数本身
        Integer identityResult = identity.apply(15);
        System.out.println(identityResult); // 15

        integrate((double x) -> x + 10, 3, 7);
    }

    public static double integrate(DoubleFunction<Double> f, double a, double b) {
        return (f.apply(a) + f.apply(b)) * (b - a) / 2.0;
    }

    public static List<Apple> map(List<Integer> list, Function<Integer, Apple> f) {
        List<Apple> result = new ArrayList<>();
        for (Integer e : list) {
            result.add(f.apply(e));
        }
        return result;
    }

}
