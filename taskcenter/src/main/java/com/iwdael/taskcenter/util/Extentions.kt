package com.iwdael.taskcenter.util

import com.iwdael.taskcenter.interfaces.Task
import com.iwdael.taskcenter.creator.Creator
import com.iwdael.taskcenter.creator.InterceptCreator
import com.iwdael.taskcenter.TaskNode
import com.iwdael.taskcenter.creator.TreeCreator


fun Any.creator(): Creator<Any, Any> {
    return this as Creator<Any, Any>
}

fun Any.task() = this as Task<Any, Any>


fun MutableMap<Creator<*, *>, MutableList<TaskNode.Node>>.obtain(creator: Creator<*, *>): MutableList<TaskNode.Node> {
    return this.getOrPut(creator) { mutableListOf() }
}


fun Creator<*, *>.isIntercept() = this is InterceptCreator<*, *>
fun Creator<*, *>.isTree() = this is TreeCreator<*, *>

fun Any.collection()  = this as Collection<Any>