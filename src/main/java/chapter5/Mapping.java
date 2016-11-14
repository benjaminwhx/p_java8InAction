package chapter5;

import entity.Dish;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: 吴海旭
 * Date: 2016-11-14
 * Time: 下午5:45
 * 5.2：映射
 */
public class Mapping {

    public static void main(String[] args) {
        // 1.对流中每一个元素应用函数
        List<String> dishes = Dish.menu.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());
        // [pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon]
        System.out.println(dishes);

        // 2.返回集合中每个元素的长度
        List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
        List<Integer> wordLengths = words.stream().map(String::length).collect(Collectors.toList());
        // [6, 7, 2, 6]
        System.out.println(wordLengths);

        // 3.返回列表中的单词中互不相同的字符的集合
        String[] words2 = {"Hello", "World"};
        List<String> uniqueCharacters = Arrays.stream(words2)
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        // [H, e, l, o, W, r, d]
        System.out.println(uniqueCharacters);

        // 4.输出输入的每个数的平方构成的列表
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> numberResult = numbers.stream().map(n -> n * n).collect(Collectors.toList());
        // [1, 4, 9, 16, 25]
        System.out.println(numberResult);

        // 5.给定两个数字列表，返回所有的数对
        List<Integer> a1 = Arrays.asList(1, 2, 3);
        List<Integer> a2 = Arrays.asList(4, 5);
        List<int[]> r = a1.stream()
                .flatMap(i -> a2.stream()
                        .map(j -> new int[]{i, j}))
                .collect(Collectors.toList());
        r.forEach(t -> {
            // [1, 4][1, 5][2, 4][2, 5][3, 4][3, 5]
            System.out.print("[" + t[0] + ", " + t[1] + "]");
        });
        System.out.println();

        // 6.给定两个数字列表，返回所有只被3整除的数对
        List<int[]> r2 = a1.stream()
                .flatMap(i -> a2.stream()
                        .filter(j -> (i + j) % 3 == 0)
                        .map(j -> new int[]{i, j}))
                .collect(Collectors.toList());
        r2.forEach(t -> {
            // [1, 5][2, 4]
            System.out.print("[" + t[0] + ", " + t[1] + "]");
        });
        System.out.println();
    }
}
