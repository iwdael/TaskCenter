package com.iwdael.taskcenter.interfaces;

import com.iwdael.taskcenter.annotations.TaskMethodCreator;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public interface Interceptor<S, D> {
    @TaskMethodCreator
    D intercept(Collection<S> collection);
}
