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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.madokasoftwares.mvvmkotlin.R
import com.madokasoftwares.mvvmkotlin.data.Task
import com.madokasoftwares.mvvmkotlin.databinding.FragmentTasksBinding
import com.madokasoftwares.mvvmkotlin.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//inject our viewmodel to our fragment
@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks),TasksAdapter.OnItemClickListener {
    private val viewModel: TasksViewModel by viewModels()

    //initiate our adapter to our fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTasksBinding.bind(view)
        val taskAdapter = TasksAdapter(this)

        //our recylerview binding
        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)

            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override  fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder:RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ):Boolean{
                 return false
                }
                //deleting item
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task =taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(recyclerViewTasks)
        }

        //fragment observing our data from viewmodel
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        //snake bar inside our corountine
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                 when(event){
                     is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage ->{
                         Snackbar.make(requireView(),"Task deleted",Snackbar.LENGTH_LONG) //snakebar
                             .setAction("UNDO"){
                                 viewModel.onUndoDeleteClick(event.task)
                             }.show()
                     }
                 }
            }
        }

        setHasOptionsMenu(true)//activating our options menu
    }

    //methods from our adapter interface
    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task,isChecked)
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