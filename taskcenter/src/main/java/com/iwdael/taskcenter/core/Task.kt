package com.iwdael.taskcenter.core

interface Task<SRC : Any, DST : Any> {
    fun run(src: SRC): DST
}