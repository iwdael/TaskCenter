package com.iwdael.taskcenter.core;

import androidx.lifecycle.MutableLiveData;

import com.iwdael.taskcenter.TaskCenter;
import com.iwdael.taskcenter.TaskRecorder;
import com.iwdael.taskcenter.annotations.TaskProportion;
import com.iwdael.taskcenter.interfaces.Interceptor;
import com.iwdael.taskcenter.interfaces.Mapper;
import com.iwdael.taskcenter.util.ConfigUtil;
import com.iwdael.taskcenter.util.NodeUtil;
import com.iwdael.taskcenter.util.Pair;
import com.iwdael.taskcenter.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskProcessor {
    public static final String TAG = "Processor";
    private final Node<Object, Object, Object> root;
    private final TaskPersistence taskPersistence;
    private final TaskCenter.Config config;
    private final ThreadPoolExecutor executor;
    private final String taskId;
    private final List<Future<?>> futures;
    private volatile boolean interrupted = false;

    private TaskProcessor(ThreadPoolExecutor executor, TaskCenter.Config config, Node<Object, Object, Object> root, Object task, String taskId) {
        this.executor = executor;
        this.config = config;
        this.root = root;
        this.taskId = taskId;
        this.config.taskProcessorRegister.put(taskId, this);
        TaskPersistence persistence = TaskRecorder.acquire(taskId, TaskPersistence.class);
        this.taskPersistence = persistence == null ? new TaskPersistence() : persistence;
        this.futures = new ArrayList<>();
    }

    public synchronized static void startProcessorForWaiting(ThreadPoolExecutor executor, TaskCenter.Config config, Node<Object, Object, Object> root, Object task, String taskId) {
        TaskProcessor taskProcessor = new TaskProcessor(executor, config, root, task, taskId);
        Runnable runnable = new TaskRunnable(taskProcessor, config, root, task, taskId);
        taskProcessor.futures.add(executor.submit(runnable));
        MutableLiveData<TaskProgress> taskProgress = ConfigUtil.acquireTaskProgress(config, taskId);
        taskProgress.postValue(new TaskProgress(TaskProgress.Stage.Prepared, taskProcessor.taskPersistence.currentProgress()));
    }

    protected synchronized TaskPersistence.NodeFrame acquireNodeFrame(Node<Object, Object, Object> node) {
        TaskPersistence.NodeFrame nodeFrame = this.taskPersistence.get(node.index);
        if (nodeFrame == null) {
            Object creator = node.creator;
            TaskProportion taskProportion = creator.getClass().getAnnotation(TaskProportion.class);
            float proportion = 1;
            if (taskProportion != null) {
                proportion = taskProportion.value();
            }
            if (creator instanceof Mapper || creator instanceof Interceptor) {
                proportion = 0;
            }
            nodeFrame = new TaskPersistence.NodeFrame();
            nodeFrame.taskProportion = proportion;
            this.taskPersistence.put(node.index, nodeFrame);
        }
        return nodeFrame;
    }

    protected synchronized void initPersistence() {
        Node<Object, Object, Object> node = root;
        while (node != null) {
            TaskPersistence.NodeFrame nodeFrame = acquireNodeFrame(node);
            for (TaskPersistence.TaskFrame taskFrame : nodeFrame) {
                if (taskFrame.stage == TaskPersistence.TaskFrame.RUNNING || taskFrame.stage == TaskPersistence.TaskFrame.ERROR || taskFrame.stage == TaskPersistence.TaskFrame.SUSPEND) {
                    taskFrame.stage = TaskPersistence.TaskFrame.PREPARED;
                }
                Pair type = NodeUtil.nodeCreatorInfo(node);
                taskFrame.source = Utils.objectConvertObject(taskFrame.source, type.parameter);
            }
            node = node.next;
        }
    }

    protected synchronized void stagePrepared(Node<Object, Object, Object> node, Object source) {
        if (this.acquireNodeFrame(node).contain(config, source)) return;
        TaskPersistence.TaskFrame taskFrame = new TaskPersistence.TaskFrame();
        taskFrame.source = source;
        this.acquireNodeFrame(node).add(taskFrame);
    }

    protected synchronized void stageRunning(Node<Object, Object, Object> node, Object source) {
        if (!this.acquireNodeFrame(node).contain(config, source)) throw new IllegalStateException("not found source:" + source);
        TaskPersistence.NodeFrame nodeFrame = this.acquireNodeFrame(node);
        for (TaskPersistence.TaskFrame taskFrame : nodeFrame) {
            if (source != taskFrame.source) continue;
            taskFrame.stage = TaskPersistence.TaskFrame.RUNNING;
        }
    }

    protected synchronized void stageAchieved(Node<Object, Object, Object> node, Object source) {
        if (!this.acquireNodeFrame(node).contain(config, source)) throw new IllegalStateException("not found source:" + source);
        TaskPersistence.NodeFrame nodeFrame = this.acquireNodeFrame(node);
        for (TaskPersistence.TaskFrame taskFrame : nodeFrame) {
            if (source != taskFrame.source) continue;
            taskFrame.stage = TaskPersistence.TaskFrame.ACHIEVED;
        }
    }

    protected synchronized void stageSuspend(Node<Object, Object, Object> node, Object source) {
        if (!this.acquireNodeFrame(node).contain(config, source)) throw new IllegalStateException("not found source:" + source);
        TaskPersistence.NodeFrame nodeFrame = this.acquireNodeFrame(node);
        for (TaskPersistence.TaskFrame taskFrame : nodeFrame) {
            if (source != taskFrame.source) continue;
            taskFrame.stage = TaskPersistence.TaskFrame.SUSPEND;
        }
    }

    protected synchronized void stageError(Node<Object, Object, Object> node, Object source) {
        if (!this.acquireNodeFrame(node).contain(config, source)) throw new IllegalStateException("not found source:" + source);
        TaskPersistence.NodeFrame nodeFrame = this.acquireNodeFrame(node);
        for (TaskPersistence.TaskFrame taskFrame : nodeFrame) {
            if (source != taskFrame.source) continue;
            taskFrame.stage = TaskPersistence.TaskFrame.ERROR;
        }
    }

    public synchronized void dispatch() {
        if (interrupted) return;
        Node<Object, Object, Object> node = root;
        while (node != null) {
            if (node instanceof NodeInterceptor) {
                if (!isAchieved(node)) break;
                int initialSize = this.acquireNodeFrame(node).getCount(TaskPersistence.TaskFrame.PREPARED);
                int handledSize = this.acquireNodeFrame(node).getCount(TaskPersistence.TaskFrame.ACHIEVED);
                int informationSize = this.acquireNodeFrame(node).size();
                if (initialSize == informationSize) {
                    List<TaskPersistence.TaskFrame> taskFrames = this.acquireNodeFrame(node).taskFrames(TaskPersistence.TaskFrame.PREPARED);
                    futures.add(executor.submit(new TaskInterceptRunnable(this, config, taskId, node, taskFrames)));
                    break;
                } else if (handledSize != informationSize) {
                    break;
                }
            } else {
                List<TaskPersistence.TaskFrame> taskFrames = this.acquireNodeFrame(node).taskFrames(TaskPersistence.TaskFrame.PREPARED);
                for (TaskPersistence.TaskFrame taskFrame : taskFrames) {
                    Runnable task;
                    if (node instanceof NodeMapper) {
                        task = new TaskMapRunnable(this, config, taskId, node, taskFrame);
                    } else if (node instanceof NodeDisperser) {
                        task = new TaskDisperseRunnable(this, config, taskId, node, taskFrame);
                    } else if (node instanceof NodeClosure) {
                        task = new TaskCloseRunnable(this, config, taskId, node, taskFrame);
                    } else {
                        task = new TaskUnitRunnable(this, config, taskId, node, taskFrame);
                    }
                    futures.add(executor.submit(task));
                }

            }
            node = node.next;
        }
        if (isAchieved()) {
            MutableLiveData<TaskProgress> taskProgress = ConfigUtil.acquireTaskProgress(config, taskId);
            taskProgress.postValue(new TaskProgress(TaskProgress.Stage.Achieved, 100f));
        }
    }

    public synchronized void interrupted() {
        this.interrupted = true;
        for (Future<?> future : this.futures) future.cancel(true);
        config.taskProcessorRegister.put(taskId, null);
    }

    public synchronized boolean isAchieved() {
        Node<Object, Object, Object> node = root;
        while (node != null) {
            TaskPersistence.NodeFrame nodeFrame = acquireNodeFrame(node);
            for (TaskPersistence.TaskFrame info : nodeFrame) {
                if (info.stage != TaskPersistence.TaskFrame.ACHIEVED) return false;
            }
            node = node.next;
        }
        return true;
    }


    protected synchronized boolean isAchieved(Node<Object, Object, Object> target) {
        Node<Object, Object, Object> node = root;
        while (node != target) {
            TaskPersistence.NodeFrame nodeFrame = acquireNodeFrame(node);
            for (TaskPersistence.TaskFrame taskFrame : nodeFrame) {
                if (taskFrame.stage != TaskPersistence.TaskFrame.ACHIEVED) return false;
            }
            node = node.next;
        }
        return true;
    }

    protected synchronized void notifyProgress() {
        keepFrame();
        if (interrupted) return;
        MutableLiveData<TaskProgress> taskProgress = ConfigUtil.acquireTaskProgress(config, taskId);
        float progress = taskPersistence.currentProgress();
        taskProgress.postValue(new TaskProgress(TaskProgress.Stage.Running, progress));
    }

    protected synchronized void keepFrame() {
        TaskRecorder.keep(taskId, taskPersistence);
    }


    protected synchronized void notifySuspend() {
        keepFrame();
        MutableLiveData<TaskProgress> taskProgress = ConfigUtil.acquireTaskProgress(config, taskId);
        taskProgress.postValue(new TaskProgress(TaskProgress.Stage.Stop, taskPersistence.currentProgress()));
    }


    protected synchronized void notifyError() {
        keepFrame();
        MutableLiveData<TaskProgress> taskProgress = ConfigUtil.acquireTaskProgress(config, taskId);
        taskProgress.postValue(new TaskProgress(TaskProgress.Stage.Error, taskPersistence.currentProgress()));
    }
}
