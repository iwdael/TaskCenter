package com.iwdael.taskcenter.creator

import com.iwdael.taskcenter.core.TaskClosure

interface CloseCreator<SRC : Any> : Creator<SRC, TaskClosure<SRC>>
