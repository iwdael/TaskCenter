package com.iwdael.taskcenter.util;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.iwdael.taskcenter.TaskCenter;
import com.iwdael.taskcenter.TaskRecorder;
import com.iwdael.taskcenter.core.Node;
import com.iwdael.taskcenter.core.TaskPersistence;
import com.iwdael.taskcenter.core.TaskProcessor;
import com.iwdael.taskcenter.core.TaskProgress;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class ConfigUtil {
    public static synchronized MutableLiveData<TaskProgress> acquireTaskProgress(TaskCenter.Config config, String id) {
        MutableLiveData<TaskProgress> progressLiveData = config.taskProgressRegister.get(id);
        if (progressLiveData == null) {
            progressLiveData = new MutableLiveData<>();
            config.taskProgressRegister.put(id, progressLiveData);
        }
        return progressLiveData;
    }

    public static synchronized boolean taskIsRunning(TaskCenter.Config config, Object task) {
        TaskProcessor taskProcessor = config.taskProcessorRegister.get(sign(config, task));
        return taskProcessor != null && !taskProcessor.isAchieved();
    }

    public static synchronized boolean tryToRunning(ThreadPoolExecutor executor, TaskCenter.Config config, Object task) {
        String taskId = ConfigUtil.sign(config, task);
        Node<Object, Object, Object> node = config.taskChainRegister.get(task.getClass());
        MutableLiveData<TaskProgress> taskProgress = ConfigUtil.acquireTaskProgress(config, taskId);
        if (node == null) {
            taskProgress.postValue(new TaskProgress(TaskProgress.Stage.Error, 0));
            Logger.tag(TaskCenter.TAG).v("not support class:" + task.getClass().getName());
            return false;
        } else {
            TaskProcessor.startProcessorForWaiting(executor, config, node, task, taskId);
            Logger.tag(TaskCenter.TAG).v("task running: " + task);
            return true;
        }
    }

    public static boolean tryToStop(TaskCenter.Config config, Object task) {
        String taskId = ConfigUtil.sign(config, task);
        Node<Object, Object, Object> node = config.taskChainRegister.get(task.getClass());
        TaskProcessor taskProcessor = config.taskProcessorRegister.get(taskId);
        if (node == null) {
            Logger.tag(TaskCenter.TAG).v("not support class:" + task.getClass().getName());
            return false;
        } else if (taskProcessor == null) {
            Logger.tag(TaskCenter.TAG).w("task is not running: " + task);
            return false;
        } else {
            taskProcessor.interrupted();
            Logger.tag(TaskCenter.TAG).v("task stop: " + task);
            return true;
        }

    }

    public static String sign(TaskCenter.Config config, Object task) {
        return config.taskSignGenerator.generate(task);
    }

    public static boolean taskIsAchieved(TaskCenter.Config config, Object task) {
        String taskId = sign(config, task);
        TaskProcessor taskProcessor = config.taskProcessorRegister.get(taskId);
        if (taskProcessor != null) {
            return taskProcessor.isAchieved();
        }
        TaskPersistence taskPersistence = TaskRecorder.acquire(taskId, TaskPersistence.class);
        String json = new Gson().toJson(taskPersistence);
        if (taskPersistence == null) return false;
        return taskPersistence.currentProgress() == 100;
    }

    public static void taskDelete(TaskCenter.Config config, Object task) {
        String taskId = sign(config, task);
        config.taskProcessorRegister.put(taskId, null);
        TaskRecorder.keep(taskId, null);
        Logger.tag(TaskCenter.TAG).v("task delete: " + task);
    }


}
