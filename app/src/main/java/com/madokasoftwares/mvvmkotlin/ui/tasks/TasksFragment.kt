package com.madokasoftwares.mvvmkotlin.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madokasoftwares.mvvmkotlin.R
import com.madokasoftwares.mvvmkotlin.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint

//inject our viewmodel to our fragment
@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks) {
    private val viewModel: TasksViewModel by viewModels()

    //initiate our adapter to our fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTasksBinding.bind(view)
        val taskAdapter = TasksAdapter()

        //our recylerview binding
        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)

            }
        }

        //fragment observing our data from viewmodel
        viewModel.tasks.observe(viewLifecycleOwner){
         taskAdapter.submitList(it)
        }
    }

}