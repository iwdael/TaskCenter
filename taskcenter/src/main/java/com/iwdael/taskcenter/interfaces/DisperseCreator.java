package com.iwdael.taskcenter.interfaces;

import com.iwdael.taskcenter.task.TaskUnitary;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public interface DisperseCreator<S, D> extends Creator<S, TaskUnitary<S, Collection<D>>> {
}
