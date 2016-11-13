package chapter2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * User: 吴海旭
 * Date: 2016-11-13
 * Time: 下午5:31
 * 行为参数化
 */
public class FilteringApples {

    /**
     * 1和2 都是我们之前使用的老方法,但是用户的行为随时在改变,我们不可能对每一个行为都增加一个方法,而是利用
     * 策略模式的思想来扩展用户的行为。
     * 3.4.5.6 都是用了策略模式来动态传递需要的策略。
     * 7 则是lambda表达式来传递用户所需要的行为。
     * @param args
     */
    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red"));

        List<Apple> greenApples = filterApplesByColor(inventory, "green");
        // 1.[Apple{weight=80, color='green'}, Apple{weight=155, color='green'}]
        System.out.println(greenApples);

        List<Apple> weightApples = filterApplesByWeight(inventory, 140);
        // 2.[Apple{weight=155, color='green'}]
        System.out.println(weightApples);

        List<Apple> appleColorFilters = filter(inventory, new AppleColorPredicate());
        // 3.[Apple{weight=80, color='green'}, Apple{weight=155, color='green'}]
        System.out.println(appleColorFilters);

        List<Apple> appleWeightFilters = filter(inventory, new AppleWeightPredicate());
        // 4.[Apple{weight=155, color='green'}]
        System.out.println(appleWeightFilters);

        List<Apple> appleColorAndWeightFilters = filter(inventory, new AppleWeightAndColorPredicate());
        // 5.[]
        System.out.println(appleColorAndWeightFilters);

        List<Apple> customFilters = filter(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return apple.getWeight() < 100;
            }
        });
        // 6. [Apple{weight=80, color='green'}]
        System.out.println(customFilters);

        List<Apple> customFilters2 = filter(inventory, (apple -> apple.getWeight() < 100));
        // 7.[Apple{weight=80, color='green'}]
        System.out.println(customFilters2);
    }

    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(apple.getColor().equals(color)){
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(apple.getWeight() > weight){
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * 根据条件(库存的商品和行为参数)返回对应的集合
     * @param inventory 库存的商品
     * @param p 行为
     * @param <T> 商品
     * @return 对应的集合
     */
    public static <T> List<T> filter(List<T> inventory, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for(T t: inventory){
            if(p.test(t)){
                result.add(t);
            }
        }
        return result;
    }

    public static class Apple {
        private int weight;
        private String color;

        public Apple(int weight, String color) {
            this.weight = weight;
            this.color = color;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "Apple{" +
                    "weight=" + weight +
                    ", color='" + color + '\'' +
                    '}';
        }
    }

    interface ApplePredicate extends Predicate<Apple> {
        boolean test(Apple apple);
    }

    static class AppleWeightPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() > 150;
        }
    }

    static class AppleColorPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return "green".equals(apple.getColor());
        }
    }

    static class AppleWeightAndColorPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return "red".equals(apple.getColor()) && apple.getWeight() > 150;
        }
    }

}
