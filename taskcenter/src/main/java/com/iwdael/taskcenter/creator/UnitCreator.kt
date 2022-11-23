package com.iwdael.taskcenter.creator

import com.iwdael.taskcenter.core.Task

interface UnitCreator<SRC:Any, DST:Any> : Creator<SRC, Task<SRC, DST>>