package com.iwdael.taskcenter.util;

import java.lang.reflect.Type;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class Pair {
    public final Type parameter;
    public final Type returnType;

    public Pair(Type parameter, Type returnType) {
        this.parameter = parameter;
        this.returnType = returnType;
    }
}
