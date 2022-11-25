package com.iwdael.taskcenter.task;

import com.iwdael.taskcenter.core.Task;

import org.jetbrains.annotations.NotNull;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public abstract class TaskClosure<S> extends Task {
    public abstract void run(@NotNull S src);
}
