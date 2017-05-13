package com.jd.jr.jdk8.book.chapter6;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * User: 吴海旭
 * Date: 2016-11-22
 * Time: 下午1:44
 * 6.6：开发一个自己的收集器以获得更好的性能
 */
public class PartitionPrimeNumbers {

    public static void main(String ... args) {
        System.out.println("Numbers partitioned in prime and non-prime: " + partitionPrimes(100));
        System.out.println("Numbers partitioned in prime and non-prime: " + partitionPrimesWithCustomCollectors(100));
        System.out.println("Numbers partitioned in prime and non-prime: " + partitionPrimesWithInlineCollector(100));
    }

    public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
        return IntStream.rangeClosed(2, n).boxed().collect(partitioningBy(i -> isPrime(i)));
    }

    public static boolean isPrime(int candidate) {
        return IntStream.rangeClosed(2, candidate - 1)
                .limit((long) Math.floor(Math.sqrt((double) candidate) - 1))
                .noneMatch(i -> candidate % i == 0);
    }

    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollectors(int n) {
        return IntStream.rangeClosed(2, n).boxed().collect(new CustomCollector());
    }

    public static boolean isPrime(List<Integer> primes, Integer candidate) {
        double candidateRoot = Math.sqrt((double) candidate);
        // 不推荐使用filter的方式，因为filter要处理整个流才能返回恰当的结果。如果质数和非质数的列表都非常大，这就是个问题了。
//        return primes.stream().filter(p -> p <= candidateRoot).noneMatch(i -> candidate % i == 0);
        return takeWhile(primes, p -> p <= candidateRoot).stream().noneMatch(i -> candidate % i == 0);
    }

    /**
     * 质数大于被测数平方根的时候直接停下来返回
     * @param list
     * @param p
     * @param <A>
     * @return
     */
    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
        int i = 0;
        for (A item : list) {
            if (!p.test(item)) {
                return list.subList(0, i);
            }
            ++i;
        }
        return list;
    }

    public static class CustomCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

        @Override
        public Supplier<Map<Boolean, List<Integer>>> supplier() {
            return () -> new HashMap<Boolean, List<Integer>>() {{
                    put(true, new ArrayList<>());
                    put(false, new ArrayList<>());
                }
            };
        }

        @Override
        public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
            return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
                acc.get(isPrime(acc.get(true), candidate)).add(candidate);
            };
        }

        @Override
        public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
            return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
                map1.get(true).addAll(map2.get(true));
                map1.get(false).addAll(map2.get(false));
                return map1;
            };
        }

        @Override
        public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)) ;
        }
    }

    public static Map<Boolean, List<Integer>> partitionPrimesWithInlineCollector(int n) {
        return Stream.iterate(2, i -> i + 1).limit(n - 1)
                .collect(
                        // Supplier
                        () -> new HashMap<Boolean, List<Integer>>(){{
                            put(true, new ArrayList<>());
                            put(false, new ArrayList<>());
                        }},
                        // Accumulator
                        (m1, i) -> {
                            m1.get(isPrime(m1.get(true), i)).add(i);
                        },
                        // Combiner
                        (m1, m2) -> {
                            m1.get(true).addAll(m2.get(true));
                            m1.get(false).addAll(m2.get(true));
                        }
                );
    }
}
