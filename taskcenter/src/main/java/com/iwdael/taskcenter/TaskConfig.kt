package com.iwdael.taskcenter

import java.util.concurrent.BlockingQueue
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.TimeUnit

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
object TaskConfig {
    @JvmStatic
    val defaultConfig = Config(0, Int.MAX_VALUE, 60, TimeUnit.SECONDS, SynchronousQueue<Runnable>())

    @JvmStatic
    fun newBuilder(): Config.Builder {
        return Config.Builder()
    }

    class Config(val corePoolSize: Int, val maximumPoolSize: Int, val keepAliveTime: Long, val unit: TimeUnit, val workQueue: BlockingQueue<Runnable>) {
        class Builder internal constructor() {
            private var corePoolSize = 0
            private var maximumPoolSize = Int.MAX_VALUE
            private var keepAliveTime = 60L
            private var unit = TimeUnit.SECONDS
            private var workQueue: BlockingQueue<Runnable> = SynchronousQueue()
            fun corePoolSize(corePoolSize: Int): Builder {
                this.corePoolSize = corePoolSize
                return this
            }

            fun maximumPoolSize(maximumPoolSize: Int): Builder {
                this.maximumPoolSize = maximumPoolSize
                return this
            }

            fun keepAliveTime(keepAliveTime: Long): Builder {
                this.keepAliveTime = keepAliveTime
                return this
            }

            fun unit(unit: TimeUnit): Builder {
                this.unit = unit
                return this
            }

            fun workQueue(workQueue: BlockingQueue<Runnable>): Builder {
                this.workQueue = workQueue
                return this
            }

            fun build(): Config {
                return Config(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue)
            }
        }
    }

}