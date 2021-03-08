package com.madokasoftwares.mvvmkotlin.ui.deleteallcompleted

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.madokasoftwares.mvvmkotlin.data.TaskDao
import com.madokasoftwares.mvvmkotlin.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteAllCompletedViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
@ApplicationScope private val applicationScope:CoroutineScope
):ViewModel() {

    fun onConfirmClick()=applicationScope.launch {
        taskDao.deleteCompletedTask()
    }
}