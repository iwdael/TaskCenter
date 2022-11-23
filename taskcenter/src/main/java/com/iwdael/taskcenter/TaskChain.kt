package com.iwdael.taskcenter

import com.iwdael.taskcenter.core.Node
import com.iwdael.taskcenter.core.NodeClosure
import com.iwdael.taskcenter.core.NodeDisperser
import com.iwdael.taskcenter.core.NodeUnitary
import com.iwdael.taskcenter.creator.CloseCreator
import com.iwdael.taskcenter.creator.DisperseCreator
import com.iwdael.taskcenter.creator.UnitCreator

object TaskChain {
    @JvmStatic
    fun newBuilder(): Chain.Builder {
        return Chain.Builder()
    }

    class Chain(val type: Class<*>, val node: Node<Any, Any>) {

        class Builder {
            fun <SRC : Any, DST : Any> append(type: Class<SRC>, unitCreator: UnitCreator<SRC, DST>): NodeUnitary<SRC, DST> {
                return NodeUnitary(type, unitCreator)
            }

            fun <SRC : Any, DST : Any> append(type: Class<SRC>, disperseCreator: DisperseCreator<SRC, DST>): NodeDisperser<SRC, DST> {
                return NodeDisperser(type, disperseCreator)
            }

            fun <SRC : Any> append(type: Class<SRC>, closeCreator: CloseCreator<SRC>): NodeClosure<SRC> {
                return NodeClosure(type, closeCreator)
            }
        }


    }
}