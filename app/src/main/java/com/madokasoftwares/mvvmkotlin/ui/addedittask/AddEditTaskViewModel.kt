package com.madokasoftwares.mvvmkotlin.ui.addedittask

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.madokasoftwares.mvvmkotlin.data.Task
import com.madokasoftwares.mvvmkotlin.data.TaskDao

class AddEditTaskViewModel(
    private val taskDao: TaskDao,
    @Assisted private val  state:SavedStateHandle //@Assisted-dagger assertion
 ):ViewModel(
 ) {
     val task=state.get<Task>("task")

     var taskName =state.get<String>("taskName") ?: task?.name ?: ""
    set(value) { //setter method
        field=value
        state.set("taskName",value)
    }
    var taskimportance =state.get<Boolean>("taskImportance") ?: task?.important?: false
        set(value) { //setter method
            field=value
            state.set("taskImportance",value)
        }


}