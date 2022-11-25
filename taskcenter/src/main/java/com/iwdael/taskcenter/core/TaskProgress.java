package com.iwdael.taskcenter.core;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskProgress {
    public Stage stage;
    public float progress;

    public TaskProgress(Stage stage, float progress) {
        this.stage = stage;
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "TaskProgress{" + "stage=" + stage + ", progress=" + progress + '}';
    }

    public enum Stage {
        Prepared, Running, Achieved, Error, Stop
    }
}
