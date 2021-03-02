package com.madokasoftwares.mvvmkotlin.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.madokasoftwares.mvvmkotlin.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest


class TasksViewModel @ViewModelInject constructor(//injecting our DAO in the viewModel
    private val taskDao: TaskDao //
) : ViewModel() {

    val sortOrder = MutableStateFlow(SortOrder.BY_DATE) //for sorting them first by date by default
    val hideCompleted = MutableStateFlow(false) //

    val searchQuery = MutableStateFlow("")//for searching

/**
//    private val tasksFlow = searchQuery.flatMapLatest { //whenever the value in the search text changes
//        taskDao.getTasks(it)//use he value and run the sql lite and assign the value to the taskflow
//    } **/

//we use the combine function to do all operations of search.sort and hide completed together
private val tasksFlow = combine(
    searchQuery,
    sortOrder,
    hideCompleted
) {query,sortOrder,hideCompleted ->
    Triple(query,sortOrder,hideCompleted)
}.flatMapLatest {(query,sortOrder,hideCompleted)->
    taskDao.getTasks(query,sortOrder,hideCompleted)
}



    //preventing our data in fragment from being lost when we change the state of application eg rotate or pause
    val tasks = tasksFlow.asLiveData() //return flow of lists of tasks from our TaskDao

    // val tasks = taskDao.getTasks().asLiveData()//use this if not searhing

}
//sorting our item by date and name
enum class SortOrder{BY_NAME, BY_DATE}

