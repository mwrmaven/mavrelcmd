package com.mavenr;

import org.apache.log4j.Logger;

/**
 * @Description: Log4jTest
 * @Author: mawenrui
 * @Create: 2019-07-13 23:31:18
 **/
public class Log4jTest {
    private static final Logger logger = Logger.getLogger(Log4jTest.class);

    public static void main(String[] args) {
//        PropertyConfigurator.configure(ClassLoader.getSystemResource("log4j.properties"));

        logger.debug("this is debug message");

        logger.info("this is info message");

        logger.error("this is error message");
    }
}
