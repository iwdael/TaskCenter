package com.iwdael.taskcenter.operators;

import org.jetbrains.annotations.NotNull;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public interface Mapper<SRC, DST> {
    @NotNull
    DST map(@NotNull SRC src);
}
