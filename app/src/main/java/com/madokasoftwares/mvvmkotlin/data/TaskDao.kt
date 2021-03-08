package com.madokasoftwares.mvvmkotlin.data


import androidx.room.*
import com.madokasoftwares.mvvmkotlin.ui.tasks.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao  //data access object
interface TaskDao {

    fun getTasks(query:String,sortOrder: SortOrder,hideCompleted: Boolean):Flow<List<Task>> = //flow represents stream of data same as LiveData -when we call this fun we will get a stream of tasks//when changes are made in our db
        when(sortOrder){
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query,hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query,hideCompleted)
        }

    @Query("SELECT * FROM task_table WHERE(completed !=:hideCompleted OR completed=0) AND name LIKE '%' || :searchQuery || '%'  ORDER BY important DESC,name ") //return all tasks from task-table we are also using the searchquery to search/filter for our data by name using searchview //'%'-means the query string can be anywhere //we alos order it by important in desc mean the important task will be at the top
    fun getTasksSortedByName(searchQuery: String,hideCompleted:Boolean): Flow<List<Task>>


    @Query("SELECT * FROM task_table WHERE(completed !=:hideCompleted OR completed=0) AND name LIKE '%' || :searchQuery || '%'  ORDER BY important DESC,created ") //return all tasks from task-table we are also using the searchquery to search/filter for our data by name using searchview //'%'-means the query string can be anywhere //we alos order it by important in desc mean the important task will be at the top
    fun getTasksSortedByDateCreated(searchQuery: String,hideCompleted:Boolean): Flow<List<Task>>

    //@Query("SELECT * FROM task_table WHERE name LIKE '%' || :searchQuery || '%'  ORDER BY important DESC ")/return all tasks from task-table we are also using the searchquery to search/filter for our data by name using searchview //'%'-means the query string can be anywhere //we alos order it by important in desc mean the important task will be at the top
    //fun getTasks(searchQuery: String): Flow<List<Task>>//flow represents stream of data same as LiveData -when we call this fun we will get a stream of tasks//when changes are made in our db

    
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task) //suspend because we want our fun to be execute in background using courintine flow

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    //deleting all
    @Query("DELETE FROM task_table WHERE completed=1")
    suspend fun  deleteCompletedTask()

}
