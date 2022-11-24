package com.iwdael.taskcenter.operators;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public interface Interceptor<SRC, DST> {
    DST intercept(Collection<SRC> collection);
}
