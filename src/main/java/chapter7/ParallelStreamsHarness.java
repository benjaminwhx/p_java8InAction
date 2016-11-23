package chapter7;

import java.util.function.Function;
import java.util.stream.LongStream;

/**
 * User: 吴海旭
 * Date: 2016-11-23
 * Time: 上午11:37
 */
public class ParallelStreamsHarness {

    public static void main(String[] args) {
        // 2 msecs 用传统的for循环的迭代版本执行起来会快很多，因为它更底层，更重要的是不需要对原始类型做任何装箱或拆箱操作。
//        System.out.println("Iterative Sum done in: " + measurePerf(ParallelStreams::iterativeSum, 10_000_000L) + " msecs");
        // 92 msecs iterate生成的是装箱的对象，必须拆箱成数字才能求和。所以效率比直接for循环要低一点。
//        System.out.println("Sequential Sum done in: " + measurePerf(ParallelStreams::sequentialSum, 10_000_000L) + " msecs");
        // 259 msecs 并行版本要比顺序版本慢很多，因为我们很难把iterate分成多个独立块来并行执行。
//        System.out.println("Parallel forkJoinSum done in: " + measurePerf(ParallelStreams::parallelSum, 10_000_000L) + " msecs" );
        // 12 msecs 这个速度比iterate的要快很多，因为数值流避免了非针对性流那些没必要的自动装箱和拆箱的操作。
//        System.out.println("Range forkJoinSum done in: " + measurePerf(ParallelStreams::rangedSum, 10_000_000L) + " msecs");
        // 5 msecs 比顺序执行要快，因为可以拆分执行了。
//        System.out.println("Parallel range forkJoinSum done in: " + measurePerf(ParallelStreams::parallelRangedSum, 10_000_000L) + " msecs" );
        // 4 msecs
//        System.out.println("SideEffect sum done in: " + measurePerf(ParallelStreams::sideEffectSum, 10_000_000L) + " msecs" );
        // 1 msecs 但是结果是错误的，因为它共享了可变状态
//        System.out.println("SideEffect prallel sum done in: " + measurePerf(ParallelStreams::sideEffectParallelSum, 10_000_000L) + " msecs" );
        // 4 msecs
        long[] numbers = LongStream.rangeClosed(1, 10_000_000L).toArray();
        System.out.println("ForkJoin sum done in: " + measurePerf(ForkJoinSumCalculator::forkJoinSum, numbers) + " msecs" );
    }

    /**
     * 计算函数执行的性能
     * @param f
     * @param input
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> long measurePerf(Function<T, R> f, T input) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; ++i) {
            long start = System.nanoTime();
            R result = f.apply(input);
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Result: " + result);
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }
}
