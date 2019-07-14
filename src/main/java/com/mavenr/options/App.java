package com.mavenr.options;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Set;

/**
 * mavrelcmd
 */
public class App {
    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("开始遍历mvn库");

        String initPath = "/Users/mawenrui/Desktop/dependencies";

        FileSearch fileSearch = new FileSearch();
        // 所有依赖文件的路径
        Set<String> search = fileSearch.search(initPath);

        // 遍历路径，生成dependencies
        FileOutputStream fos = null;
        FileChannel channel = null;
        try {
            fos = new FileOutputStream(new File("/Users/mawenrui/Desktop/dependencies.txt"));
            channel = fos.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            for (String st : search) {
                buffer.clear();
                logger.info("最终文件路径为 " + st);
                String[] params = st.split("/");
                // 获取artifactid
                String artiId = params[params.length - 3];
                // 获取groupid
                String groupId = st.substring(0, st.indexOf(artiId, st.indexOf("/")) - 1).replaceAll("/", ".");
                String version = params[params.length - 1].replace(artiId + "-", "").replace(".jar", "");

                if (st.endsWith("jar")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<dependency>").append("\n");
                    sb.append("    <groupId>");
                    sb.append(groupId);
                    sb.append("</groupId>").append("\n");
                    sb.append("    <artifactId>");
                    sb.append(artiId);
                    sb.append("</artifactId>").append("\n");
                    sb.append("    <version>");
                    sb.append(version);
                    sb.append("</version>").append("\n");
                    sb.append("</dependency>").append("\n");
                    buffer.put(sb.toString().getBytes());
                    buffer.flip();
                    channel.write(buffer);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                channel.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }


    }
}
