package com.jd.jr.jdk8.feature;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * User: 吴海旭
 * Date: 2017-05-13
 * Time: 下午5:05
 */
public class NumCounterTest {

	public static void main(String[] args) {
		String arr = "12%3 21sdas s34d dfsdz45   R3 jo34 sjkf8 3$1P 213ikflsd fdg55 kfd";
		Stream<Character> stream = IntStream.range(0, arr.length()).mapToObj(arr::charAt);
		// 432
		System.out.println("ordered total: " + countNum(stream));
		// 调用parallel()变成并行流，这个计算是错误的
		// 293
//		System.out.println("ordered total: " + countNum(stream.parallel()));

		Spliterator<Character> spliterator = new NumCounterSpliterator(arr);
		// 传入true表示是并行流
		Stream<Character> parallelStream = StreamSupport.stream(spliterator, true);
		// 432
		System.out.println("parallel total: " + countNum(parallelStream));
	}

	private static int countNum(Stream<Character> stream){
		NumCounter numCounter = stream.reduce(new NumCounter(0, 0, false), NumCounter::accumulate, NumCounter::combine);
		return numCounter.getSum();
	}
}
