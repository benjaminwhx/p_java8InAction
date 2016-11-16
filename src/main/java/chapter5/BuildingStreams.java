package chapter5;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * User: 吴海旭
 * Date: 2016-11-16
 * Time: 下午4:15
 * 5.7：构建流
 */
public class BuildingStreams {

    public static void main(String[] args) throws IOException {
        // 1.由值创建流
        Stream<String> stream = Stream.of("Java 8 ", "Lambda ", "In ", "Action");
        stream.map(String::toUpperCase).forEach(System.out::print);
        System.out.println();

        // 2.由数组创建流
        int[] numbers = {2, 3, 5, 7, 11, 13};
        int sum = Arrays.stream(numbers).sum();
        System.out.println(sum);

        // 3.由文件生成流
        long uniqueWords = Files.lines(Paths.get("lambdasinaction/chapter5/data.txt"), Charset.defaultCharset())
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .distinct()
                .count();
        System.out.println("There are " + uniqueWords + " unique words in data.txt");

        // 4.由函数生成流：创建无限流
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);

        // 5.斐波那契元祖序列
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(20)
                .forEach(t -> System.out.print("(" + t[0] + ", " + t[1] + ")"));
        System.out.println();
        // 斐波那契数列
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(20)
                .map(t -> t[0])
                .forEach(t -> System.out.print(t + ","));
        System.out.println();

        // 6.generate生成随机数
        Stream.generate(Math::random)
                .limit(10)
                .forEach(System.out::println);

        IntStream.generate(() -> 1)
                .limit(5)
                .forEach(System.out::println);

        // generate生成斐波那契数列
        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1;
            @Override
            public int getAsInt() {
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };
        IntStream.generate(fib).limit(10).forEach(System.out::println);
    }
}
