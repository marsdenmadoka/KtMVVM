package com.madokasoftwares.mvvmkotlin.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.madokasoftwares.mvvmkotlin.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest


class TasksViewModel @ViewModelInject constructor(//injecting our DAO in the viewModel
    private val taskDao: TaskDao //
) : ViewModel() {

    val searchQuery = MutableStateFlow("")//for searching
    private val tasksFlow = searchQuery.flatMapLatest { //whenever the value in the search text changes
        taskDao.getTasks(it)//use he value and run the sql lite and assign the value to the taskflow
    }


    //preventing our data in fragment from being lost when we change the state of application eg rotate or pause
    val tasks = tasksFlow.asLiveData() //return flow of lists of tasks from our TaskDao

    // val tasks = taskDao.getTasks().asLiveData()//use this if not searhing

}