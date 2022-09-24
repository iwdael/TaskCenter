package com.iwdael.taskcenter.converter

import com.iwdael.taskcenter.interfaces.Task

class DefaultInterceptor<I> : Task.Interceptor<I, Collection<I>> {
    override fun convert(source: Collection<I>) = source
}