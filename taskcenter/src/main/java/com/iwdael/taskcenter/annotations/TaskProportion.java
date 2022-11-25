package com.iwdael.taskcenter.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskProportion {
    float value() default 1f;
}
