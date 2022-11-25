package com.iwdael.taskcenter.core;

import androidx.annotation.NonNull;

import com.iwdael.taskcenter.TaskCenter;
import com.iwdael.taskcenter.util.ConfigUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskPersistence extends HashMap<Integer, TaskPersistence.NodeFrame> {


    public float currentProgress() {
        float proportionCount = 0;
        float proportionProgress = 0;
        List<NodeFrame> nodeFrames = new ArrayList<>(this.values());
        for (NodeFrame nodeFrame : nodeFrames) {
            if (nodeFrame.taskProportion <= 0) continue;
            float taskProgress = 0;
            float proportion = 1;
            List<TaskFrame> taskFrames = nodeFrame.taskFrames;
            int taskCount = taskFrames.size();
            for (TaskPersistence.TaskFrame taskFrame : taskFrames) taskProgress += taskFrame.progress;
            if (taskCount > 0) proportionProgress += (taskProgress * proportion / taskCount);
            proportionCount += proportion;
        }
        return proportionProgress / proportionCount;
    }

    public static class NodeFrame implements Iterable<TaskFrame> {
        public float taskProportion = 1;
        private final List<TaskFrame> taskFrames = new ArrayList<>();

        public int getCount(int stage) {
            int count = 0;
            for (TaskFrame taskFrame : taskFrames) {
                if (taskFrame.stage == stage) count++;
            }
            return count;
        }


        public List<TaskFrame> taskFrames(int stage) {
            List<TaskFrame> taskFrames = new ArrayList<>();
            for (TaskFrame taskFrame : this.taskFrames) {
                if (taskFrame.stage == stage) {
                    taskFrames.add(taskFrame);
                }
            }
            return taskFrames;
        }

        public boolean contain(@NotNull TaskCenter.Config config, @NotNull Object o) {
            for (TaskFrame taskFrame : taskFrames) {
                if (Objects.equals(ConfigUtil.sign(config, taskFrame.source), ConfigUtil.sign(config, o))) return true;
            }
            return false;
        }

        public void add(TaskFrame taskFrame) {
            taskFrames.add(taskFrame);
        }

        @NonNull
        @Override
        public Iterator<TaskFrame> iterator() {
            return taskFrames.iterator();
        }

        public int size() {
            return taskFrames.size();
        }

        public TaskFrame get(int index) {
            return taskFrames.get(index);
        }
    }

    public static class TaskFrame {
        public static final int PREPARED = 1;
        public static final int RUNNING = 2;
        public static final int ACHIEVED = 3;
        public static final int ERROR = 4;
        public static final int SUSPEND = 5;
        public Object source;
        public int stage = PREPARED;
        public int progress = 0;
        public Object frame;
    }

}