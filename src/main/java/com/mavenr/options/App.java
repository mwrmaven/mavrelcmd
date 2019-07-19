package com.mavenr.options;

/**
 * mavrelcmd
 */
public class App {
    public static void main(String[] args) {
        FetchInfo fetchInfo = new FetchInfo();
//        fetchInfo.fetchMvnRepo(args);
        fetchInfo.fetchMvnRepo(new String[]{"-DrepositoryId=release", "-Durl=test",
                "-Dcommand=install:install-file", "-Dmvnpath=/Users/mawenrui/Desktop/dependencies"});
    }
}
