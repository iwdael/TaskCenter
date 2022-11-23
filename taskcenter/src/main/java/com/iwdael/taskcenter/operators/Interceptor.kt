package com.iwdael.taskcenter.operators

interface Interceptor<SRC, DST> {
    fun intercept(source: Collection<SRC>): DST
}