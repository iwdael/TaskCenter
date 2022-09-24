package com.iwdael.taskcenter.util

class Logger {
    companion object {
        const val TAG = "task-center"
        fun tag(tag: String) = Log(tag)
    }

    class Log(private val tag: String) {
        fun v(format: String, vararg args: Any) {
            println("$TAG-${tag}: ${java.lang.String.format(format, args)}")
        }

        fun e(format: String, vararg args: Any) {
            System.err.println("$TAG-${tag}: ${java.lang.String.format(format, args)}")
        }
    }
}