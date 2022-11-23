package com.iwdael.taskcenter.core

abstract class Node<I : Any, R : Any> {
    internal var index = 0
    internal var next: Node<Any, Any>? = null
    internal var pre: Node<Any, Any>? = null
    internal fun hasNext() = next != null
    internal fun next() = next!!
    internal abstract fun make(src: I): R
    internal fun asNode() = this as Node<Any, Any>
}