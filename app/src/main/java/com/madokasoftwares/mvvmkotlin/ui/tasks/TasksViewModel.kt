package com.madokasoftwares.mvvmkotlin.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.madokasoftwares.mvvmkotlin.data.TaskDao

//injecting our DAO in the viewModel
class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao
):ViewModel() {
}