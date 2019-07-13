package com.mavenr;

import java.io.File;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("开始遍历mvn库");

        File file = new File("/Users/mawenrui/Desktop/dependencies");

        File[] files = file.listFiles();
        for (File f : files) {
            System.out.println(f.getName());
            System.out.println(f.getAbsolutePath());
            System.out.println("==========================");
        }

    }
}
