package chapter3;

import entity.Apple;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * User: 吴海旭
 * Date: 2016-11-14
 * Time: 上午10:58
 * 3.7节：lambda和方法引用实战
 */
public class SortCollection {

    public static void main(String[] args) {
        // 实现各种类型的排序
        List<Apple> inventory = Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red"));

        // 1.行为参数化
        inventory.sort(new AppleComparator());

        // 2.使用匿名类
        inventory.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });

        // 3.使用lambda表达式
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
        // 3.1使用lambda表达式自动推断类型
        inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));

        // 4.使用Comparator的静态辅助方法comparing
        inventory.sort(Comparator.comparing(a -> a.getWeight()));

        // 5.使用方法引用
        inventory.sort(Comparator.comparing(Apple::getWeight));

        // 6.逆序排列
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed());

        // 7.比较器链，在重量相等的情况下按颜色比较
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));

    }

    static class AppleComparator implements Comparator<Apple> {
        @Override
        public int compare(Apple o1, Apple o2) {
            return o1.getWeight().compareTo(o2.getWeight());
        }
    }
}
