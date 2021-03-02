package com.madokasoftwares.mvvmkotlin.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.madokasoftwares.mvvmkotlin.data.TaskDao


class TasksViewModel @ViewModelInject constructor(//injecting our DAO in the viewModel
    private val taskDao: TaskDao
) : ViewModel() {


    //preventing our data in fragment from being lost when we change the state of application eg rotate or pause
    val tasks=taskDao.getTasks().asLiveData() //return flow of lists of tasks from our TaskDao

}