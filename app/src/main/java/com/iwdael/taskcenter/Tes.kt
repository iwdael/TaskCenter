package com.iwdael.taskcenter

import com.iwdael.taskcenter.converter.DefaultInterceptor
import com.iwdael.taskcenter.source.Source1
import com.iwdael.taskcenter.task.Task1
import com.iwdael.taskcenter.task.Task2
import com.iwdael.taskcenter.task.Task3
import com.iwdael.taskcenter.task.Task4

fun main(){
    TaskCenter.append(Source1::class.java,Task1.Creator())
        .append(Task2.Creator())
        .append(Task3.Creator())
        .append(DefaultInterceptor())
        .append(Task4.Creator())

    TaskCenter.submit(Source1())
    while (true){}
}