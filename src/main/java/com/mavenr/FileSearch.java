package com.mavenr;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description: FileSearch
 * @Author: mawenrui
 * @Create: 2019-07-13 23:36:21
 **/
public class FileSearch {

    private Set<String> localtions = new HashSet<>();

    public Set<String> search(String path) {
        // 获取path下的files
        File file = new File(path);
        if (file.isDirectory()) {
            System.out.println("遍历文件夹" + path);
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    search(f.getAbsolutePath());
                } else {
                    System.out.println("找到文件" + f);
                    String absolutePath = f.getAbsolutePath();
                    if (absolutePath.endsWith("pom") || absolutePath.endsWith("jar")) {
                        System.out.println("===" + absolutePath);
                        localtions.add(absolutePath);
                    }
                }
            }
        } else {
            System.out.println("未找到文件夹");
        }

        return localtions;
    }
}
