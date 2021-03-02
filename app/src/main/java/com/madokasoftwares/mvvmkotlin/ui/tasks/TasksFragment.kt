package com.madokasoftwares.mvvmkotlin.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madokasoftwares.mvvmkotlin.R
import com.madokasoftwares.mvvmkotlin.databinding.FragmentTasksBinding
import com.madokasoftwares.mvvmkotlin.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
        setHasOptionsMenu(true)//activating our options menu
    }

    //our search view
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView


        //this onQueryTextChanged is from our extension function in util/ViewExt package
        searchView.onQueryTextChanged {
            //update search query
            viewModel.searchQuery.value = it
        }

        //we want to read our ischecked value from our view model so that it does'nt get lost when we rotate
     viewLifecycleOwner.lifecycleScope.launch {
         menu.findItem(R.id.action_hide_complete_tasks).isChecked =
             viewModel.preferencesFlow.first().hideCompleted
     }

    }

    //when other menu items are selecte
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                //viewModel.sortOrder.value = SortOrder.BY_NAME //use this when no SharedPreferences class
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_date_created -> {
                //viewModel.sortOrder.value = SortOrder.BY_DATE
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_hide_complete_tasks -> {
                item.isChecked = !item.isChecked
                //viewModel.hideCompleted.value =item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                true
            }
            R.id.action_delete_all_completed_tasks -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}