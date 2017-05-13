首先先直接给一个答案：Spliterator（splitable iterator可分割迭代器）接口是Java为了并行遍历数据源中的元素而设计的迭代器，这个可以类比最早Java提供的顺序遍历迭代器Iterator，但一个是顺序遍历，一个是并行遍历

从最早Java提供顺序遍历迭代器Iterator时，那个时候还是单核时代，但现在多核时代下，顺序遍历已经不能满足需求了...如何把多个任务分配到不同核上并行执行，才是能最大发挥多核的能力，所以Spliterator应运而生啦

因为对于数据源而言...集合是描述它最多的情况，所以Java已经默认在集合框架中为所有的数据结构提供了一个默认的Spliterator实现，相应的这个实现其实就是底层Stream如何并行遍历（Stream.isParallel()）的实现啦，因此平常用到Spliterator的情况是不多的...因为Java8这次正是一次引用函数式编程的思想，你只需要告诉JDK你要做什么并行任务，关注业务本身，至于如何并行，怎么并行效率最高，就交给JDK自己去思考和优化速度了（想想以前写如何并发的代码被支配的恐惧吧）作为调用者我们只需要去关心一些filter，map，collect等业务操作即可

所以想要看Spliterator的实现，可以直接去看JDK对于集合框架的实现，很多实现类你可以在Spliterators中找到的，也可以直接去你对应集合的stream方法中找到，比如ArrayList点进去的是Collection的默认实现，只需要提供一个Spliterator的实现，然后用StreamSupport就可以构造一个Stream了，相当方便

```
default Stream<E> stream() {
    return StreamSupport.stream(spliterator(), false);
}

@Override
default Spliterator<E> spliterator() {
    return Spliterators.spliterator(this, 0);
}
```

对于Spliterator接口的设计思想，应该要提到的是Java7的Fork/Join(分支/合并)框架，总得来说就是用递归的方式把并行的任务拆分成更小的子任务，然后把每个子任务的结果合并起来生成整体结果。带着这个理解来看看Spliterator接口提供的方法

```
boolean tryAdvance(Consumer<? super T> action);
Spliterator<T> trySplit();
long estimateSize();
int characteristics();
```

第一个方法tryAdvance就是顺序处理每个元素，类似Iterator，如果还有元素要处理，则返回true，否则返回false
第二个方法trySplit，这就是为Spliterator专门设计的方法，区分与普通的Iterator，该方法会把当前元素划分一部分出去创建一个新的Spliterator作为返回，两个Spliterator变会并行执行，如果元素个数小到无法划分则返回null
第三个方法estimateSize，该方法用于估算还剩下多少个元素需要遍历
第四个方法characteristics，其实就是表示该Spliterator有哪些特性，用于可以更好控制和优化Spliterator的使用，具体属性你可以随便百度到，这里就不再赘言

理解了接口方法的意思，现在再来看看自己的实现吧，由于大多数集合都被官方实现了，所以不能搞集合了，只有搞搞类似集合但又不是集合的东西...举以下这个例子，例子反正感觉不是很好，只能说帮助理解哈Spliterator接口了：

问题：求这个字符串"12%3 21sdas s34d dfsdz45 R3 jo34 sjkf8 3$1P 213ikflsd fdg55 kfd"中所有的数字之和例子：比如这种"12%sdf3"，和就是12+3=15，这种"12%3 21sdas"和就是12+3+21=36

思路：字符串要用到Stream，只有把整个字符串拆分成一个个Character，而是否是并行，按道理讲只需要改一个标志位即可

先顺序执行代码方式：

```
/**
 * 字符串中的数字计算器实现
 */
public class NumCounter {

    private int num;
    private int sum;
    // 是否当前是个完整的数字
    private boolean isWholeNum;

    public NumCounter(int num, int sum, boolean isWholeNum) {
        this.num = num;
        this.sum = sum;
        this.isWholeNum = isWholeNum;
    }

    public NumCounter accumulate(Character c){
        if (Character.isDigit(c)){
            return isWholeNum ? new NumCounter(Integer.parseInt("" + c), sum + num, false) : new NumCounter(Integer.parseInt("" + num + c), sum, false);
        }else {
            return new NumCounter(0, sum + num, true);
        }
    }

    public NumCounter combine(NumCounter numCounter){
        return new NumCounter(numCounter.num, this.getSum() + numCounter.getSum(), numCounter.isWholeNum);
    }

    public int getSum() {
        return sum + num;
    }
}
```

```
/**
 * 测试类
 */
public class NumCounterTest {

    public static void main(String[] args) {
        String arr = "12%3 21sdas s34d dfsdz45   R3 jo34 sjkf8 3$1P 213ikflsd fdg55 kfd";
        Stream<Character> stream = IntStream.range(0, arr.length()).mapToObj(arr::charAt);
        System.out.println("ordered total: " + countNum(stream));
    }

    private static int countNum(Stream<Character> stream){
        NumCounter numCounter = stream.reduce(new NumCounter(0, 0, false), NumCounter::accumulate, NumCounter::combine);
        return numCounter.getSum();
    }
}
```

执行结果如下：

> ordered total: 432

如果按照普通方式直接把stream改为并行流...执行结果明显有点...不对

```
/**
 * 测试类
 */
public class NumCounterTest {

    public static void main(String[] args) {
        String arr = "12%3 21sdas s34d dfsdz45   R3 jo34 sjkf8 3$1P 213ikflsd fdg55 kfd";
        Stream<Character> stream = IntStream.range(0, arr.length()).mapToObj(arr::charAt);
        // 调用parallel()变成并行流
        System.out.println("ordered total: " + countNum(stream.parallel()));
    }

    private static int countNum(Stream<Character> stream){
        NumCounter numCounter = stream.reduce(new NumCounter(0, 0, false), NumCounter::accumulate, NumCounter::combine);
        return numCounter.getSum();
    }
}
```

此时错误的并行执行结果如下：

> ordered total: 293

为什么会执行错误，是因为默认的Spliterator在并行时并不知道整个字符串从哪里开始切割，由于切割错误，导致把本来完整的数字比如123，可能就切成了12和3，这样加起来的数字肯定不对
若是理解了上诉顺序执行的NumCounter的逻辑，再来看看Spliterator的实现

```
/**
 * 字符串中的数字分割迭代计算器实现
 */
public class NumCounterSpliterator implements Spliterator<Character> {

    private String str;
    private int currentChar = 0;

    public NumCounterSpliterator(String str) {
        this.str = str;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(str.charAt(currentChar++));
        return currentChar < str.length();
    }

    @Override
    public Spliterator<Character> trySplit() {

        int currentSize = str.length() - currentChar;
        if (currentSize < 10) return null;

        for (int pos = currentSize/2 + currentSize; pos < str.length(); pos++){
            if (pos+1 < str.length()){
                // 当前Character是数字，且下一个Character不是数字，才需要划分一个新的Spliterator
                if (Character.isDigit(str.charAt(pos)) && !Character.isDigit(str.charAt(pos+1))){
                    Spliterator<Character> spliterator = new NumCounterSpliterator(str.substring(currentChar, pos));
                    currentChar = pos;
                    return spliterator;
                }
            }else {
                if (Character.isDigit(str.charAt(pos))){
                    Spliterator<Character> spliterator = new NumCounterSpliterator(str.substring(currentChar, pos));
                    currentChar = pos;
                    return spliterator;
                }
            }
        }
        return null;
    }

    @Override
    public long estimateSize() {
        return str.length() - currentChar;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}
```

```
/**
 * 测试类
 */
public class NumCounterTest {

    public static void main(String[] args) {
        String arr = "12%3 21sdas s34d dfsdz45   R3 jo34 sjkf8 3$1P 213ikflsd fdg55 kfd";
        Stream<Character> stream = IntStream.range(0, arr.length()).mapToObj(arr::charAt);
        System.out.println("ordered total: " + countNum(stream));

        Spliterator<Character> spliterator = new NumCounterSpliterator(arr);
        // 传入true表示是并行流
        Stream<Character> parallelStream = StreamSupport.stream(spliterator, true);
        System.out.println("parallel total: " + countNum(parallelStream));
    }

    private static int countNum(Stream<Character> stream){
        NumCounter numCounter = stream.reduce(new NumCounter(0, 0, false), NumCounter::accumulate, NumCounter::combine);
        return numCounter.getSum();
    }
}
```

这下可以看到执行结果是正确的了

> ordered total: 432
  parallel total: 432

