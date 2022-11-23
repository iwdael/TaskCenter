package com.iwdael.taskcenter.core

interface TaskClosure<SRC : Any> : Task<SRC, Unit> {
    override fun run(src: SRC)
}