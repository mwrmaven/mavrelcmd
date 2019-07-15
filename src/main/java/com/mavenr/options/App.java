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

        FileOutputStream fos = null;
        FileChannel channel = null;

        FileOutputStream fos1 = null;
        FileChannel channel1 = null;
        try {
            fos = new FileOutputStream(new File("/Users/mawenrui/Desktop/dependencies.txt"));
            channel = fos.getChannel();

            fos1 = new FileOutputStream(new File("/Users/mawenrui/Desktop/mvncommonds.txt"));
            channel1 = fos1.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(4096);

            for (String st : search) {
                // 生成dependencies
                String absoluteP = st;
                st = st.replace(initPath + "/", "");
                buffer.clear();
                logger.info("最终文件路径为 " + st);
                String[] params = st.split("/");
                // 获取artifactid
                String artiId = params[params.length - 3];
                // 获取groupid
                String groupId = st.substring(0, st.indexOf(artiId, st.indexOf("/")) - 1).replaceAll("/", ".");
                String version = params[params.length - 1].replace(artiId + "-", "");
                version = version.substring(0, version.lastIndexOf("."));

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

                buffer.clear();
                // 生成maven命令行
                StringBuilder sb1 = new StringBuilder("mvn deploy:deploy-file -DgroupId=")
                        .append(groupId).append(" -DartifactId=").append(artiId)
                        .append(" -Dversion=").append(version).append(" -Dpackaging=")
                        .append(absoluteP.substring(absoluteP.length() - 3))
                        .append(" -Dfile=").append(absoluteP).append(" -DrepositoryId=releases -Durl=")
                        .append("http://127.0.0.1:8080/repository/releases").append("\n");
                buffer.put(sb1.toString().getBytes());
                buffer.flip();
                channel1.write(buffer);
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
                channel1.close();
                fos1.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }


    }
}
