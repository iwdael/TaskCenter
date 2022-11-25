package com.iwdael.taskcenter.defaults;

import com.iwdael.taskcenter.interfaces.Interceptor;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskInterceptor<S> implements Interceptor<S, Collection<S>> {
    @Override
    public Collection<S> intercept(Collection<S> collection) {
        return collection;
    }
}
