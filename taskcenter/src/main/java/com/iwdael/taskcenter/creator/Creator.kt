package com.iwdael.taskcenter.creator

abstract class Creator<I, R> {
    var number = 0
    var next: Creator<*, *>? = null
    fun hasNext() = next != null
    fun next() = next!!
    abstract fun make(input: I): R
}