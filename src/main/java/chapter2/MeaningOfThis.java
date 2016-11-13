package chapter2;

/**
 * User: 吴海旭
 * Date: 2016-11-13
 * Time: 下午5:45
 */
public class MeaningOfThis {
    public final int value = 4;

    public void doIt() {
        int value = 6;
        Runnable r = new Runnable() {
            public final int value = 5;
            @Override
            public void run() {
                int value = 10;
                System.out.println(value);  // 10
                System.out.println(this.value); // 5
            }
        };
        r.run();
    }

    /**
     * this.value输出的是当前内部类的value的值,所以是5
     * @param args
     */
    public static void main(String[] args) {
        MeaningOfThis meaningOfThis = new MeaningOfThis();
        meaningOfThis.doIt();
    }
}
