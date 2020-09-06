package com.yubin.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Stream创建方法的演示
 *
 * @author YUBIN
 * @create 2020-09-06
 */
public class StreamGenerateDemo {

    public static void main(String[] args) {
        //generate1();
        //generate2();
        //generate3();
        //generate4();
        generate5();
    }

    /**
     * 通过数组创建Stream
     */
    private static void generate1() {
        // 创建一个String数组
        String[] strArr = {"a", "c", "d", "f", "e", "b"};
        // 通过数组创建Stream
        Stream<String> stream = Stream.of(strArr);
        // 遍历数组中的每一个元素(forEach方法的入参是Consumer函数式接口:代表一个输入)
        //stream.forEach((str) -> System.out.println(str));
        // 采用对象方法引用的方式书写
        stream.forEach(System.out::println);
    }

    /**
     * 通过集合创建Stream
     */
    private static void generate2() {
        // 创建一个List集合
        List<String> list = Arrays.asList("a", "b", "c", "d");
        // 通过集合创建Stream
        Stream<String> stream = list.stream();
        // 遍历数组中的每一个元素(forEach方法的入参是Consumer函数式接口:代表一个输入)
        stream.forEach((str) -> System.out.println(str));
        // 采用对象方法引用的方式书写
        //stream.forEach(System.out::println);
    }

    /**
     * 通过Stream.generate方法创建Stream
     */
    private static void generate3() {
        // 通过Stream的generate方法创建Stream
        // generate(Supplier<T> s)  返回无限顺序无序流，其中每个元素由提供的Supplier 。
        Stream<Integer> stream = Stream.generate(() -> 1);
        // 由于是无限流,这里通过limit限制个数
        stream.limit(10).forEach(System.out::println);
    }

    /**
     * 通过Stream.iterate方法来创建Stream
     */
    private static void generate4() {
        // 通过Stream.iterate方法来创建Stream 第一次的输出充当第一次的输入
        Stream<Integer> stream = Stream.iterate(1, x -> x + 1);
        stream.limit(10).forEach(System.out::println);
    }

    /**
     * 其它方式创建Stream
     */
    private static void generate5() {
        // 其它方式创建Stream
        String str = "abcdefg";
        IntStream stream = str.chars();
        //stream.forEach(System.out::println);
        stream.forEach(x -> System.out.println((char)x));
    }
}
