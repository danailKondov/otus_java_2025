package ru.otus;

import com.google.common.collect.Lists;
import java.util.List;

public class HelloOtus {

    public static void main(String... args) {
        List<String> helloList = Lists.newArrayList("Hello", "Otus", "world!");
        System.out.println(String.join(" ", helloList));
    }
}
