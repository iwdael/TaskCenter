package com.iwdael.taskcenter.operators

interface Mapper<SRC, DST> {
    fun map(src: SRC): DST
}