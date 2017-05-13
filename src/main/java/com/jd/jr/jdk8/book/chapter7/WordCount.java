package com.jd.jr.jdk8.book.chapter7;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * User: 吴海旭
 * Date: 2016-11-23
 * Time: 下午7:37
 * 7.3：spliterator
 */
public class WordCount {

    public static final String SENTENCE =
            " Nel   mezzo del cammin  di nostra  vita " +
                    "mi  ritrovai in una  selva oscura" +
                    " che la  dritta via era   smarrita ";

    public static void main(String[] args) {
        System.out.println("Found " + countWordsIteratively(SENTENCE) + " words");
        System.out.println("Found " + countWords(SENTENCE) + " words");
    }

    /**
     * 迭代式字数统计方法
     * @param s
     * @return
     */
    public static int countWordsIteratively(String s) {
        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) counter++;
                lastSpace = false;
            }
        }
        return counter;
    }

    public static int countWords(String s) {
        // 并行输出的结果不对，因为原始的String在任意位置拆分，所以有时一个词会被分为两个词，然后数了两次。
        // 这就说明，拆分流会影响结果，而把顺序流换成并行流就可能使结果出错。
//        Stream<Character> stream = IntStream.range(0, s.length()).mapToObj(SENTENCE::charAt).parallel();
//        Stream<Character> stream = IntStream.range(0, s.length()).mapToObj(SENTENCE::charAt);
        Spliterator<Character> spliterator = new WordCounterSpliterator(s);
        Stream<Character> stream = StreamSupport.stream(spliterator, true);
        return countWords(stream);
    }

    private static int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true), WordCounter::accumulate, WordCounter::combine);
        return wordCounter.getCounter();
    }

    private static class WordCounter {
        private final int counter;
        private final boolean lastSpace;

        public WordCounter(int counter, boolean lastSpace) {
            this.counter = counter;
            this.lastSpace = lastSpace;
        }

        public WordCounter accumulate(Character c) {
            if (Character.isWhitespace(c)) {
                return lastSpace ? this : new WordCounter(counter, true);
            } else {
                return lastSpace ? new WordCounter(counter+1, false) : this;
            }
        }

        public WordCounter combine(WordCounter wordCounter) {
            return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
        }

        public int getCounter() {
            return counter;
        }
    }

    private static class WordCounterSpliterator implements Spliterator<Character> {

        private final String string;
        private int currentChar = 0;

        public WordCounterSpliterator(String string) {
            this.string = string;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Character> action) {
            // 处理当前字符
            action.accept(string.charAt(currentChar++));
            // 如果还有字符要处理，则返回true
            return currentChar < string.length();
        }

        @Override
        public Spliterator<Character> trySplit() {
            int currentSize = string.length() - currentChar;
            if (currentSize < 10) {
                // 返回null表示要解析的String已经足够小，可以顺序处理
                return null;
            }
            for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
                // 让拆分位置前进到下一个空格
                if (Character.isWhitespace(string.charAt(splitPos))) {
                    // 创建一个新WordCounter Spliterator来解析String从开始到拆分位置的部分
                    Spliterator<Character> spliterator = new WordCounterSpliterator(string.substring(currentChar, splitPos));
                    // 将这个WordCounter Spliterator的起始位置设为拆分位置
                    currentChar = splitPos;
                    return spliterator;
                }
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return string.length() - currentChar;
        }

        @Override
        public int characteristics() {
            return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
        }
    }
}
