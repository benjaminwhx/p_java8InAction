package chapter3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

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

        // 方法引用
        List<String> stringList = Arrays.asList("a", "b", "c");
        stringList.sort((String s1, String s2) -> s1.compareTo(s2));
        // 可以简写为
        stringList.sort(Comparator.comparing(String::toString));
    }
}
