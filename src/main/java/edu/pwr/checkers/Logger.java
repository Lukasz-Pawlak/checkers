package edu.pwr.checkers;

public class Logger {
    public static void debug(String msg) {
        //System.out.println(msg);// nothing for release
    }

    public static void info(String msg) {
        System.out.println(msg);
    }

    public static void err(String msg) {
        System.err.println(msg);
    }
}
