package com.madokasoftwares.mvvmkotlin.ui.tasks

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.madokasoftwares.mvvmkotlin.R
import dagger.hilt.android.AndroidEntryPoint

//inject our viewmodel to our fragment
@AndroidEntryPoint
class TasksFragment:Fragment(R.layout.fragment_tasks) {
private val viewModel:TasksViewModel by viewModels()

}