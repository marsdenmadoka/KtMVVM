package com.madokasoftwares.mvvmkotlin.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao  //data acess object
interface TaskDao {

    @Query("SELECT * FROM task_table") //return all tasks from task-table note this is not the name of our db
    fun getTasks():Flow<List<Task>>//flow represents stream of data same as LiveData -when we call this fun we will get a stream of tasks//when changes are made in our db
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task) //suspend because we want our fun to be execute in background using courintine flow

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)



}
