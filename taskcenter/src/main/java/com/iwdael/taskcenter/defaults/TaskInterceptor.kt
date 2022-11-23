package com.iwdael.taskcenter.defaults

import com.iwdael.taskcenter.operators.Interceptor

class TaskInterceptor<I> : Interceptor<I, Collection<I>> {
    override fun intercept(source: Collection<I>) = source
}