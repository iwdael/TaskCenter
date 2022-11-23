package com.iwdael.taskcenter.core

import com.iwdael.taskcenter.TaskChain
import com.iwdael.taskcenter.creator.CloseCreator

class NodeClosure<SRC : Any>(private val type: Class<*>, private val creator: CloseCreator<SRC>) : Node<SRC, Task<SRC, Unit>>() {
    override fun make(src: SRC) = creator.create(src)

    fun build(): TaskChain.Chain {
        var root: Node<Any, Any> = this.asNode()
        while (root.pre != null) {
            root = root.pre!!
        }
        return TaskChain.Chain(type, root)
    }
}