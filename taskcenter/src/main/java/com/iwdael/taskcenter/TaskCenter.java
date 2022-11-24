package com.iwdael.taskcenter;

import com.iwdael.taskcenter.core.Node;
import com.iwdael.taskcenter.core.NodeClosure;
import com.iwdael.taskcenter.core.NodeDisperser;
import com.iwdael.taskcenter.core.NodeUnitary;
import com.iwdael.taskcenter.core.TaskRunnable;
import com.iwdael.taskcenter.creator.CloseCreator;
import com.iwdael.taskcenter.creator.DisperseCreator;
import com.iwdael.taskcenter.creator.UnitCreator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskCenter {
    private static volatile TaskCenter sTaskCenter;
    private final Map<Class<?>, Node<Object, Object>> nodes;
    private final ThreadPoolExecutor executor;

    private TaskCenter(Config config) {
        this.nodes = new HashMap<>();
        this.executor = new ThreadPoolExecutor(config.corePoolSize, config.maximumPoolSize, config.keepAliveTime, config.unit, config.workQueue);
    }

    public static TaskCenter init(Config config) {
        if (sTaskCenter == null) {
            synchronized (TaskCenter.class) {
                if (sTaskCenter == null) {
                    sTaskCenter = new TaskCenter(config);
                }
            }
        }
        return sTaskCenter;
    }

    private static TaskCenter checkTaskCenter() {
        if (sTaskCenter == null) {
            synchronized (TaskCenter.class) {
                if (sTaskCenter == null) {
                    throw new RuntimeException("please initial TaskCenter");
                }
            }
        }
        return sTaskCenter;
    }

    public static TaskCenter start(Object task) {
        checkTaskCenter().executor.submit(new TaskRunnable(task));
        return checkTaskCenter();
    }

    public static TaskCenter taskChain(Chain chain) {
        checkTaskCenter().nodes.put(chain.type, chain.node);
        return checkTaskCenter();
    }

    public static ThreadPoolExecutor executors() {
        return checkTaskCenter().executor;
    }

    public static Node<Object, Object> obtainNode(Class<?> type) {
        return checkTaskCenter().nodes.get(type);
    }

    public static class Config {
        public final int corePoolSize;
        public final int maximumPoolSize;
        public final long keepAliveTime;
        public final TimeUnit unit;
        public final BlockingQueue<Runnable> workQueue;
        public static final Config defaultConfig = Config.newBuilder().build();

        public Config(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
            this.unit = unit;
            this.workQueue = workQueue;
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public static class Builder {
            private int corePoolSize = 0;
            private int maximumPoolSize = Integer.MAX_VALUE;
            private long keepAliveTime = 60;
            private TimeUnit unit = TimeUnit.SECONDS;
            private BlockingQueue<Runnable> workQueue = new SynchronousQueue<Runnable>();

            private Builder() {
            }

            public Builder corePoolSize(int corePoolSize) {
                this.corePoolSize = corePoolSize;
                return this;
            }

            public Builder maximumPoolSize(int maximumPoolSize) {
                this.maximumPoolSize = maximumPoolSize;
                return this;
            }

            public Builder keepAliveTime(long keepAliveTime) {
                this.keepAliveTime = keepAliveTime;
                return this;
            }

            public Builder unit(TimeUnit unit) {
                this.unit = unit;
                return this;
            }

            public Builder workQueue(BlockingQueue<Runnable> workQueue) {
                this.workQueue = workQueue;
                return this;
            }

            public Config build() {
                return new Config(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
            }
        }
    }

    public static class Chain {
        protected final Class<?> type;
        protected final Node<Object, Object> node;

        public Chain(Class<?> type, Node<Object, Object> node) {
            this.type = type;
            this.node = node;
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public static class Builder {
            public <SRC, DST> NodeUnitary<SRC, DST> append(Class<?> type, UnitCreator<SRC, DST> unitCreator) {
                return new NodeUnitary<>(type, unitCreator);
            }

            public <SRC, DST> NodeDisperser<SRC, DST> append(Class<?> type, DisperseCreator<SRC, DST> unitCreator) {
                return new NodeDisperser<>(type, unitCreator);
            }

            public <SRC> NodeClosure<SRC> append(Class<?> type, CloseCreator<SRC> closeCreator) {
                return new NodeClosure<>(type, closeCreator);
            }
        }
    }
}
