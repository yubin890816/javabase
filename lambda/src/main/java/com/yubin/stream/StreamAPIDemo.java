package com.yubin.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream常用API
 *
 * @author YUBIN
 * @create 2020-09-06
 */
public class StreamAPIDemo {

    public static void main(String[] args) {
        //filterTest();
        //mapTest();
        //mapToIntTest();
        //flatmapTest();
        //peekTest();
        //findFirstTest();
        //testOptional();
        //reduceTest();
        //testLimitAndSkip();
        //testLimitAndSkipBySort();
        //sortedTest();
        //maxMinDistinctTest();
        //matchTest();
        concatTest();
    }

    /**
     * Stream常用API之中间操作 -- filter
     */
    private static void filterTest() {
        // 过滤出集合中的偶数
        Arrays.asList(1, 2, 3, 4, 5).stream().filter(x -> x % 2 == 0).forEach(System.out::println);
    }

    /**
     * Stream常用API之中间操作 -- map操作(转换 对Stream中的每个元素进行操作)
     *  案例：将List<String> 中的每个元素转换成大写, 同时使用终止操作forEach遍历集合中的每个元素
     */
    private static void mapTest() {
        List<String> list = Arrays.asList("a", "b", "c", "d");
        list.stream().map(x -> x.toUpperCase()).forEach(System.out::println);
    }

    /**
     * Stream常用API之中间操作 -- mapToInt操作(转换 相当于Stream<Integer>)
     */
    private static void mapToIntTest() {
        // 过滤出集合中的偶数并求和
        long sum = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9).stream().filter(x -> x % 2 == 0).mapToLong(x -> x).sum();
        System.out.println(sum);
    }

    /**
     * Stream常用API之中间操作 -- flatMap操作
     */
    private static void flatmapTest() {
        Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6));
        Stream<Integer> outputStream = inputStream.flatMap((childList) -> childList.stream());
        List<Integer> collect = outputStream.collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * Stream常用API之中间操作 -- peek
     *  peek对每个元素执行操作并返回一个新的Stream
     */
    private static void peekTest() {
        List<String> list = Arrays.asList("a", "b", "c", "d");
        List<String> collect = list.stream().peek(x -> print(x)).collect(Collectors.toList());
    }

    private static void print(String str) {
        System.out.println(str);
    }

    /**
     * Stream常用API之中间操作 -- findFirst/findAny
     *  这是一个 termimal 兼 short-circuiting 操作，它总是返回 Stream 的第一个元素，或者空。
     *  这里比较重点的是它的返回值类型：Optional。这也是一个模仿Scala语言中的概念，作为一个容器，它可能含有某值，或者不包含。
     *  使用它的目的是尽可能避免NullPointerException。
     */
    private static void findFirstTest() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Optional<Integer> first1 = list.stream().filter(x -> x % 2 == 0).findFirst();
        if (first1.isPresent()) {
            System.out.println(first1.get());
        }
        Optional<Integer> first2 = list.stream().filter(x -> x % 2 == 0).findAny();
        if (first2.isPresent()) {
            System.out.println(first2.get());
        }
    }

    /**
     * Optional的两个操作实例
     */
    private static void testOptional() {
        String strA = "abcd", strB = null;
        optionalPrint(strA);
        optionalPrint(strB);

        System.out.println(getLength(strA, true));
        System.out.println(getLength(strB, true));

        System.out.println(getLength(strA, false));
        System.out.println(getLength(strB, false));
    }


    /**
     * Stream常用API之终止操作 -- reduce
     *  这个方法的主要作用是把Stream元素组合起来。它提供一个起始值（种子），然后依照运算规则（BinaryOperator），
     *  和前面 Stream 的第一个、第二个、第 n 个元素组合。从这个意义上说，字符串拼接、数值的 sum、min、max、average 都是特殊的 reduce。
     *  例如 Stream 的 sum 就相当于Integer sum = integers.reduce(0, (a, b) -> a+b); 或Integer sum = integers.reduce(0, Integer::sum);
     *  也有没有起始值的情况，这时会把Stream的前面两个元素组合起来，返回的是 Optional。
     */
    private static void reduceTest() {
        // 字符串连接，concat = "ABCD"
        String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        System.out.println(concat);
        // 求最小值，minValue = -3.0
        double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        System.out.println(minValue);
        // 求和，sumValue = 10, 有起始值
        int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        System.out.println(sumValue);
        // 求和，sumValue = 10, 无起始值
        sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
        System.out.println(sumValue);
        // 过滤，字符串连接，concat = "ace"
        concat = Stream.of("a", "B", "c", "D", "e", "F").filter(x -> x.compareTo("Z") > 0).reduce("", String::concat);
        System.out.println(concat);
    }

    /**
     * Stream常用API之中间操作 -- limit/skip
     */
    private static void testLimitAndSkip() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        List<Integer> collect = list.stream().limit(10).skip(3).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * Stream常用API之中间操作 -- limit/skip
     */
    private static void testLimitAndSkipBySort() {
        List<Integer> list = new ArrayList<>();
        for (int i = 9; i > 0; i--) {
            list.add(i);
        }
        List<Integer> collect = list.stream().sorted().limit(2).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * Stream API之中间操作sorted
     */
    private static void sortedTest() {
        List<Person> list = new ArrayList<>();
        for (int i = 9; i > 0; i--) {
            Person person = new Person("userName" + i);
            list.add(person);
        }
        List<Person> collect1 = list.stream().sorted(Comparator.comparing(Person::getName)).limit(2).collect(Collectors.toList());
        System.out.println("=====================================================================");
        List<Person> collect2 = list.stream().limit(2).sorted(Comparator.comparing(Person::getName)).collect(Collectors.toList());
        System.out.println(collect1);
        System.out.println(collect2);
    }

    /**
     * Stream API之终止操作max、min、distinct
     */
    private static void maxMinDistinctTest() {
        Integer max = Stream.of(1, 2, 3, 5, 1, 3).max(Integer::compareTo).get();
        System.out.println(max);

        Integer min = Stream.of(1, 2, 3, 5, 1, 3).min(Integer::compareTo).get();
        System.out.println(min);

        List<Integer> collect = Stream.of(1, 2, 3, 5, 1, 3).distinct().sorted().collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * Stream API之终止操作match
     */
    private static void matchTest() {
        List<Integer> users = Arrays.asList(1, 2, 3, 4, 6, 8);
        boolean isAllAdult = users.stream().allMatch(p -> p > 18);
        System.out.println("All are adult? " + isAllAdult);
        boolean isThereAnyChild = users.stream().anyMatch(p -> p < 12);
        System.out.println("Any child? " + isThereAnyChild);
    }

    /**
     * Stream API之中间操作 -- concat
     */
    private static void concatTest() {
        Stream<Integer> stream = Stream.concat(Stream.of(1, 2, 3), Stream.of(4, 5, 6));
        stream.forEach(System.out::println);
    }

    private static int getLength(String text,boolean flag) {
        // java 8
        if (flag) {
            return Optional.ofNullable(text).map(String::length).orElse(-1);
        } else {
            // pre java 8
            return text == null ? -1 : text.length();
        }
    }

    private static void optionalPrint(String text) {
        // java 8
        Optional.ofNullable(text).ifPresent(System.out::println);

        // pre java 8
        if (text != null) {
            System.out.println(text);
        }
    }
}
