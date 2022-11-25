package com.iwdael.taskcenter.exceptions;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class RunningTaskException extends RuntimeException{
    public RunningTaskException(String message) {
        super(message);
    }
}
