package com.jd.jr.jdk8.book.chapter3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: 吴海旭
 * Date: 2016-11-13
 * Time: 下午6:59
 * 3.3节 把Lambda付诸实践:环绕执行模式
 */
public class ExecuteAround {

    public static void main(String[] args) throws IOException {
        String result = processFileLimited();
        System.out.println(result); // Java

        System.out.println("====");

        String result2 = processFile(br -> br.readLine());
        System.out.println(result2);    // Java

        String result3 = processFile(br -> br.readLine() + br.readLine());
        System.out.println(result3);    // Java8

        String result4 = processFile(ExecuteAround::processFile);
        System.out.println(result4);    // Java*8
    }

    public static String processFile(BufferedReader br) throws IOException {
        return br.readLine() + "*" + br.readLine();
    }

    public static String processFileLimited() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("lambdasinaction/chapter3/data.txt"))) {
            return br.readLine();
        }
    }

    public static String processFile(BufferedReaderProcess bufferedReaderProcess) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("lambdasinaction/chapter3/data.txt"))) {
            return bufferedReaderProcess.process(br);
        }
    }

    public interface BufferedReaderProcess {

        String process(BufferedReader br) throws IOException;
    }
}
