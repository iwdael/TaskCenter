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
            android.util.Log.v("task-" + tag, content);
        }

        public void w(String content) {
            android.util.Log.w("task-" + tag, content);
        }
    }
}
