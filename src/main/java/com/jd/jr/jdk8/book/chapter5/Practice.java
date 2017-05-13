package com.jd.jr.jdk8.book.chapter5;

import com.jd.jr.jdk8.book.entity.Trader;
import com.jd.jr.jdk8.book.entity.Transaction;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: 吴海旭
 * Date: 2016-11-16
 * Time: 下午1:11
 * 5.5：付诸实践
 */
public class Practice {

    public static void main(String[] args) {
        // 构建实体
        Trader zhangsan = new Trader("张三", "江苏");
        Trader lisi = new Trader("李四","河北");
        Trader wangwu = new Trader("王五","江苏");
        Trader zhaoliu = new Trader("赵六","江苏");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(zhaoliu, 2011, 300),
                new Transaction(zhangsan, 2012, 1000),
                new Transaction(zhangsan, 2011, 400),
                new Transaction(lisi, 2012, 710),
                new Transaction(lisi, 2012, 700),
                new Transaction(wangwu, 2012, 950)
        );

        // Question1: 找出2011年发生的所有交易，并按交易额排序（从低到高）
        System.out.println("Question1 Result=========");

        transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .forEach(System.out::println);

        // Question2：交易员都在哪些不同的城市工作过？
        System.out.println("\nQuestion2 Result=========");
        transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct() // 这里可以把distinct改为toSet，同样可以起到过滤的作用
                .forEach(r -> System.out.print(r + " "));
        System.out.println();

        // Question3：查找所有来自于江苏的交易员，并按姓名排序。
        System.out.println("\nQuestion3 Result=========");
        transactions.stream()
                .map(Transaction::getTrader)
                .filter(t -> "江苏".equals(t.getCity()))
                .distinct()
                .sorted((t1, t2) -> Collator.getInstance(java.util.Locale.CHINA).compare(t1.getName(), t2.getName()))   // 这里做了个中文排序处理
                .forEach(System.out::println);

        // Question4：返回所有交易员的姓名字符串，按字母顺序排序。
        System.out.println("\nQuestion4 Result=========");
        String allNames = transactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .sorted(Collator.getInstance(Locale.CHINA)::compare)
                .collect(Collectors.joining(","));
        System.out.println(allNames);

        // Question5：有没有交易员实在河北工作的？
        System.out.println("\nQuestion5 Result=========");
        boolean hebeiBased = transactions.stream()
                .anyMatch(t -> "河北".equals(t.getTrader().getCity()));
        if (hebeiBased) {
            System.out.println("找到有交易员在河北工作的");
        }

        // Question6：打印生活在江苏的交易员的所有交易额
        System.out.println("\nQuestion6 Result=========");
        transactions.stream()
                .filter(t -> "江苏".equals(t.getTrader().getCity()))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        // Question7：所有交易中，最高的交易额是多少？
        System.out.println("\nQuestion7 Result=========");
        transactions.stream()
                .map(Transaction::getValue)
                .max(Integer::compare)  // 还可以用reduce(Integer::max)
                .ifPresent(System.out::println);

        // Question8：找到交易额最小的交易
        System.out.println("\nQuestion8 Result=========");
        transactions.stream()
//                .reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2)
                .min(Comparator.comparing(Transaction::getValue))
                .ifPresent(System.out::println);
    }
}
