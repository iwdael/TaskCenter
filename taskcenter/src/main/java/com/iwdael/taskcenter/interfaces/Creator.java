package com.iwdael.taskcenter.interfaces;

import com.iwdael.taskcenter.annotations.TaskMethodCreator;

import org.jetbrains.annotations.NotNull;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public interface Creator<S, D> {
    @NotNull
    @TaskMethodCreator
    D create(@NotNull S src);
}
