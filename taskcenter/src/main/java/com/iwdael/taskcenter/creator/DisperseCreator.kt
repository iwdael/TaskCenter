package com.iwdael.taskcenter.creator

import com.iwdael.taskcenter.core.Task

interface DisperseCreator<SRC:Any, DST:Any> : Creator<SRC, Task<SRC, Collection<DST>>>
