package chapter3;

/**
 * User: 吴海旭
 * Date: 2016-11-13
 * Time: 下午7:49
 * 自定义函数式接口
 */
@FunctionalInterface
public interface MyFunctionInterface<A, T> {

    /**
     * 转换A类型为T
     * @param a
     * @return
     */
    T convert(A a);
}
