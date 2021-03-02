package com.madokasoftwares.mvvmkotlin.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.madokasoftwares.mvvmkotlin.data.PreferencesManager
import com.madokasoftwares.mvvmkotlin.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


class TasksViewModel @ViewModelInject constructor(//injecting our DAO in the viewModel
    private val taskDao: TaskDao, //
    private val preferencesManager: PreferencesManager //our preferencesManager class
) : ViewModel() {

    val preferencesFlow = preferencesManager.preferencesFlow
    val searchQuery = MutableStateFlow("")//for searching


    private val tasksFlow = combine(
        searchQuery,
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted:Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    //preventing our data in fragment from being lost when we change the state of application eg rotate or pause
    val tasks = tasksFlow.asLiveData() //return flow of lists of tasks from our TaskDao
}

//sorting our item by date and name
enum class SortOrder { BY_NAME, BY_DATE }


/**
//if you have no shared preferences class ie jetpackdatastore use the code below uncoment it..it will aslo work perefectly but it wont use the sharedpreference class whihch is important when restoring the sate of our app
class TasksViewModel @ViewModelInject constructor(
//injecting our DAO in the viewModel
private val taskDao: TaskDao

) : ViewModel() {

val sortOrder = MutableStateFlow(SortOrder.BY_DATE) //for sorting them first by date by default
val hideCompleted = MutableStateFlow(false) //

val searchQuery = MutableStateFlow("")//for searching

//this for for searching only but instead we will use the combine function to shorten the code since we also need to sort and hide
// private val tasksFlow = searchQuery.flatMapLatest { //whenever the value in the search text changes
// taskDao.getTasks(it)//use the value and run the sql lite and assign the value to the taskflow
// }


//we use the combine function to do all operations of search.sort and hide completed together
private val tasksFlow = combine(
searchQuery,
sortOrder,
hideCompleted
) { query, sortOrder, hideCompleted ->
Triple(query, sortOrder, hideCompleted)
}.flatMapLatest { (query, sortOrder, hideCompleted) ->
taskDao.getTasks(query, sortOrder, hideCompleted)
}

//preventing our data in fragment from being lost when we change the state of application eg rotate or pause
val tasks = tasksFlow.asLiveData() //return flow of lists of tasks from our TaskDao
// val tasks = taskDao.getTasks().asLiveData()//use this if not searching items

}

//sorting our item by date and name
enum class SortOrder { BY_NAME, BY_DATE }
 **/