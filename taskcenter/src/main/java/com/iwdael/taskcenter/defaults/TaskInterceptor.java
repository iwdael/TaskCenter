package com.iwdael.taskcenter.defaults;

import com.iwdael.taskcenter.operators.Interceptor;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskInterceptor<SRC> implements Interceptor<SRC, Collection<SRC>> {
    @Override
    public Collection<SRC> intercept(Collection<SRC> collection) {
        return collection;
    }
}
