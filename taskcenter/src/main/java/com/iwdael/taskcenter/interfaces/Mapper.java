package com.iwdael.taskcenter.interfaces;

import com.iwdael.taskcenter.annotations.TaskMethodCreator;

import org.jetbrains.annotations.NotNull;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public interface Mapper<S, D> {
    @TaskMethodCreator
    @NotNull
    D map(@NotNull S src);
}
