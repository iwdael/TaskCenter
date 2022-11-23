package com.iwdael.taskcenter.creator

interface Creator<SRC, DST> {
    fun create(src: SRC): DST
}