package com.iwdael.taskcenter.creator;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public interface Creator<SRC, DST> {
    DST create(SRC src);
}
