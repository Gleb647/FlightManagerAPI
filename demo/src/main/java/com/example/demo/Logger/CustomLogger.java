package com.example.demo.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogger {
    static Logger logger = LoggerFactory.getLogger("log");

    public static void info(String message, String name){
        logger.info(message, name);
    }

    public static void info(String message, String name, String id){
        logger.info(message, name, id);
    }

    public static void warn(String message, String name){
        logger.warn(message, name);
    }

    public static void warn(String message, String name, String id){
        logger.info(message, name, id);
    }

    public static void error(String message, String name){
        logger.error(message, name);
    }
    public static void error(String message, String name, String id){
        logger.error(message, name, id);
    }
}
