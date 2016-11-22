package chapter6;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * User: 吴海旭
 * Date: 2016-11-22
 * Time: 下午0:02
 */
public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

    @Override
    public Supplier<List<T>> supplier() {
//        return () -> new ArrayList<T>();
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
//        return (list, item) -> list.add(item);
        return List::add;
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        };
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
//        return e -> e;
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.CONCURRENT, Characteristics.IDENTITY_FINISH));
    }
}
