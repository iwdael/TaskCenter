package com.iwdael.taskcenter.example

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.iwdael.taskcenter.TaskCenter
import com.iwdael.taskcenter.defaults.TaskInterceptor
import com.iwdael.taskcenter.example.databinding.ActivityMain2Binding
import com.iwdael.taskcenter.example.source.Source1
import com.iwdael.taskcenter.example.task.*
import com.iwdael.taskcenter.util.Logger.Log
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMain2Binding>(this, R.layout.activity_main2)
        initTask()
    }

    private fun initTask() {
        TaskCenter.init(this, TaskCenter.Config.defaultConfig)
        val chain = TaskCenter.Chain.newBuilder()//
            .append(Source1::class.java, Task1.Creator())//0
            .append(Task2.Creator())//1
            .append(Task3.Creator())//2
            .append(TaskInterceptor())//3
            .append(Task4.Creator())//4
            .append(Task5.Creator())//5
            .build()
        TaskCenter.taskChain(chain)
        TaskCenter.observeTaskProgress(Source1(), this) {
            binding.progress = Gson().toJson(it)
            android.util.Log.v("tag",binding.progress.toString())
        }
    }

    fun onStart(view: View) {
        thread {
            TaskCenter.start(Source1())
        }
    }

    fun onPause(view: View) {
        thread { TaskCenter.stop(Source1()) }
    }

    fun onDelete(view: View) {
        thread { TaskCenter.delete(Source1()) }
    }

    fun rel() {
        val params = TaskCenter::stop.parameters
    }
}