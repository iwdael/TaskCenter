package com.iwdael.taskcenter.creator;

import com.iwdael.taskcenter.core.TaskUnitary;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public interface DisperseCreator<SRC, DST> extends Creator<SRC, TaskUnitary<SRC, Collection<DST>>> {
}
