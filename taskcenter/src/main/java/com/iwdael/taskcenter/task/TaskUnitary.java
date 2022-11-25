package com.iwdael.taskcenter.task;

import com.iwdael.taskcenter.core.Task;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public abstract class TaskUnitary<S,D> extends Task {
    public abstract D run(S src);
}
