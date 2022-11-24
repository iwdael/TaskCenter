package com.iwdael.taskcenter.core;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public interface TaskUnitary<SRC,DST> extends Task{
    DST run(SRC src);
}
