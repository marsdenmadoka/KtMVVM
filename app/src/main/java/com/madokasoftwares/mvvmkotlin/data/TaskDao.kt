package com.madokasoftwares.mvvmkotlin.data


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao  //data access object
interface TaskDao {

    @Query("SELECT * FROM task_table WHERE name LIKE '%' || :searchQuery || '%'  ORDER BY important DESC ") //return all tasks from task-table we are also using the searchquery to search/filter for our data by name using searchview //'%'-means the query string can be anywhere //we alos order it by important in desc mean the important task will be at the top
    fun getTasks(searchQuery: String): Flow<List<Task>>//flow represents stream of data same as LiveData -when we call this fun we will get a stream of tasks//when changes are made in our db

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task) //suspend because we want our fun to be execute in background using courintine flow

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)


}
