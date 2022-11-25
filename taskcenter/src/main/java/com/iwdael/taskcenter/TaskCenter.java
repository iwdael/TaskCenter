package com.iwdael.taskcenter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.iwdael.taskcenter.core.Node;
import com.iwdael.taskcenter.core.NodeClosure;
import com.iwdael.taskcenter.core.NodeDisperser;
import com.iwdael.taskcenter.core.NodeUnitary;
import com.iwdael.taskcenter.core.TaskProcessor;
import com.iwdael.taskcenter.core.TaskProgress;
import com.iwdael.taskcenter.interfaces.CloseCreator;
import com.iwdael.taskcenter.interfaces.DisperseCreator;
import com.iwdael.taskcenter.interfaces.TaskSignGenerator;
import com.iwdael.taskcenter.interfaces.UnitCreator;
import com.iwdael.taskcenter.util.ConfigUtil;
import com.iwdael.taskcenter.util.DefaultTaskSignGenerator;
import com.iwdael.taskcenter.util.Logger;

import org.jetbrains.annotations.NotNull;

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
    public static final String TAG = "Center";
    private static volatile TaskCenter sTaskCenter;
    private final ThreadPoolExecutor executor;
    private final Config config;

    private TaskCenter(Config config) {
        this.config = config;
        this.executor = new ThreadPoolExecutor(config.corePoolSize, config.maximumPoolSize, config.keepAliveTime, config.unit, config.workQueue);
    }

    public static TaskCenter init(@NotNull Context context, @NotNull Config config) {
        if (sTaskCenter == null) {
            synchronized (TaskCenter.class) {
                if (sTaskCenter == null) {
                    TaskRecorder.init(context);
                    sTaskCenter = new TaskCenter(config);
                }
            }
        }
        return sTaskCenter;
    }

    private static TaskCenter acquire() {
        if (sTaskCenter == null) {
            synchronized (TaskCenter.class) {
                if (sTaskCenter == null) {
                    throw new RuntimeException("please initial TaskCenter");
                }
            }
        }
        return sTaskCenter;
    }

    public synchronized static boolean start(@NotNull Object task) {
        if (isRunning(task)) {
            Logger.tag(TAG).w("task is running: " + task);
            return false;
        } else if (isAchieved(task)) {
            Logger.tag(TAG).w("task is achieved: " + task);
            return false;
        } else {
            return ConfigUtil.tryToRunning(acquire().executor, acquire().config, task);
        }
    }


    public synchronized static boolean stop(@NotNull Object task) {
        if (!isRunning(task)) {
            Logger.tag(TAG).w("task is not running: " + task);
            return false;
        } else {
            return ConfigUtil.tryToStop(acquire().config,task);
        }
    }


    public synchronized static boolean delete(@NotNull Object task) {
        if (isAchieved(task)) {
            ConfigUtil.taskDelete(acquire().config, task);
            return true;
        } else {
            Logger.tag(TAG).w("task is not achieved: " + task);
            return false;
        }
    }

    public synchronized static boolean isRunning(@NotNull Object task) {
        return ConfigUtil.taskIsRunning(acquire().config, task);
    }

    public synchronized static boolean isAchieved(@NotNull Object task) {
        return ConfigUtil.taskIsAchieved(acquire().config, task);
    }


    public synchronized static TaskCenter taskChain(@NotNull Chain chain) {
        TaskCenter taskCenter = acquire();
        taskCenter.config.taskChainRegister.put(chain.type, chain.node);
        return acquire();
    }

    public static void observeTaskProgress(@NotNull Object task, @NonNull LifecycleOwner owner, @NonNull Observer<TaskProgress> observer) {
        Config config = acquire().config;
        MutableLiveData<TaskProgress> taskProgress = ConfigUtil.acquireTaskProgress(config, ConfigUtil.sign(config, task));
        taskProgress.observe(owner, observer);
    }

    public static void observeForeverTaskProgress(@NotNull Object task, @NonNull Observer<TaskProgress> observer) {
        Config config = acquire().config;
        MutableLiveData<TaskProgress> taskProgress = ConfigUtil.acquireTaskProgress(config, ConfigUtil.sign(config, task));
        taskProgress.observeForever(observer);
    }

    public static class Config {
        public final int corePoolSize;
        public final int maximumPoolSize;
        public final long keepAliveTime;
        public final TimeUnit unit;
        public final BlockingQueue<Runnable> workQueue;
        public final Map<Class<?>, Node<Object, Object, Object>> taskChainRegister;
        public final TaskSignGenerator taskSignGenerator;
        public final Map<String, MutableLiveData<TaskProgress>> taskProgressRegister;
        public final Map<String, TaskProcessor> taskProcessorRegister;
        public static final Config defaultConfig = Config.newBuilder().build();

        public Config(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, Map<Class<?>, Node<Object, Object, Object>> taskChainRegister, Map<String, MutableLiveData<TaskProgress>> taskProgressRegister, Map<String, TaskProcessor> taskProcessorRegister, TaskSignGenerator taskSignGenerator) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
            this.unit = unit;
            this.workQueue = workQueue;
            this.taskChainRegister = taskChainRegister;
            this.taskProgressRegister = taskProgressRegister;
            this.taskProcessorRegister = taskProcessorRegister;
            this.taskSignGenerator = taskSignGenerator;
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
            private TaskSignGenerator taskSignGenerator = new DefaultTaskSignGenerator();

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

            public Builder taskSignGenerator(TaskSignGenerator taskSignGenerator) {
                this.taskSignGenerator = taskSignGenerator;
                return this;
            }

            public Config build() {
                return new Config(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new HashMap<>(), new HashMap<>(), new HashMap<>(), taskSignGenerator);
            }
        }
    }

    public static class Chain {
        protected final Class<?> type;
        protected final Node<Object, Object, Object> node;

        public Chain(Class<?> type, Node<Object, Object, Object> node) {
            this.type = type;
            this.node = node;
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public static class Builder {
            public <S, D> NodeUnitary<S, D> append(Class<?> type, UnitCreator<S, D> unitCreator) {
                return new NodeUnitary<>(type, unitCreator);
            }

            public <S, D> NodeDisperser<S, D> append(Class<?> type, DisperseCreator<S, D> unitCreator) {
                return new NodeDisperser<>(type, unitCreator);
            }

            public <S> NodeClosure<S> append(Class<?> type, CloseCreator<S> closeCreator) {
                return new NodeClosure<>(type, closeCreator);
            }
        }
    }
}
