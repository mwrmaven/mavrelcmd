package com.mavenr.options;

import com.mavenr.Log4jTest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Set;

/**
 * @Description: FetchInfo
 * @Author: mawenrui
 * @Create: 2019-07-19 18:34:50
 **/
public class FetchInfo {
    private static final Logger logger = Logger.getLogger(FetchInfo.class);

    private static final String separator = File.separator;

    public void fetchMvnRepo(String[] args) {
        String repositoryId = "";
        String url = "";
        String command = "";
        String initPath = "";
        for (int i = 0; i < args.length; i++) {
            String[] params = args[i].split("=");
            if (args[i].startsWith("-DrepositoryId") && params.length == 2) {
                repositoryId = params[1];
            } else if (args[i].startsWith("-Durl") && params.length == 2) {
                url = params[1];
            } else if (args[i].startsWith("-Dcommand") && params.length == 2) {
                command = params[1];
            } else if (args[i].startsWith("-Dmvnpath") && params.length == 2) {
                initPath = params[1];
            }
        }

        if (StringUtils.isBlank(initPath)) {
            System.out.println("未找到mvn库，请在命令行中输入-Dmvnpath的配置");
            return;
        }
        logger.info("开始遍历mvn库");

//        String initPath = "." + separator + "dependencies";

        FileSearch fileSearch = new FileSearch();
        // 所有依赖文件的路径
        Set<String> search = fileSearch.search(initPath);

        FileOutputStream fos = null;
        FileChannel channel = null;

        FileOutputStream fos1 = null;
        FileChannel channel1 = null;
        try {
            fos = new FileOutputStream(new File("." + separator + "dependencies.txt"));
            channel = fos.getChannel();

            fos1 = new FileOutputStream(new File("." + separator + "mvncommonds.txt"));
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
                StringBuilder tmp = new StringBuilder();
                for (int i = params.length; i > 3; i--) {
                    tmp.append(params[params.length - i]).append(".");
                }
                String groupId = tmp.substring(0, tmp.length() - 1);
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
                StringBuilder sb1 = new StringBuilder("mvn");
                if (!StringUtils.isBlank(command)) {
                    sb1.append(" ").append(command);
                }
                sb1.append(" -DgroupId=")
                        .append(groupId).append(" -DartifactId=").append(artiId)
                        .append(" -Dversion=").append(version).append(" -Dpackaging=")
                        .append(absoluteP.substring(absoluteP.length() - 3))
                        .append(" -Dfile=").append(absoluteP);
                if (!StringUtils.isBlank(repositoryId)) {
                    sb1.append(" -DrepositoryId=").append(repositoryId);
                }
                if (!StringUtils.isBlank(url)) {
                    sb1.append(" -Durl=").append(url);
                }
                sb1.append("\n");

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
