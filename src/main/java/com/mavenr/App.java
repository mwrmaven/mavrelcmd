package com.mavenr;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * mavrelcmd
 */
public class App {
    public static void main(String[] args) {
        System.out.println("开始遍历mvn库");

        String initPath = "/Users/mawenrui/Desktop/dependencies";

//        File file = new File(initPath);

//        File[] files = file.listFiles();
//        for (File f : files) {
//            System.out.println(f.getName());
//            System.out.println(f.getAbsolutePath());
//            System.out.println("==========================");
//        }

        FileSearch fileSearch = new FileSearch();
        Set<String> search = fileSearch.search(initPath);

        for (String st : search) {
            System.out.println("最终文件路径为 " + st);
        }

    }
}
