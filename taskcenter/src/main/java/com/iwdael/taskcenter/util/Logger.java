package com.iwdael.taskcenter.util;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class Logger {
    public static Log tag(String tag) {
        return new Log(tag);
    }

    public static class Log {
        private final String tag;

        public Log(String tag) {
            this.tag = tag;
        }

        public void v(String content) {
            System.out.println(tag + "  /" + content);
        }
    }
}
