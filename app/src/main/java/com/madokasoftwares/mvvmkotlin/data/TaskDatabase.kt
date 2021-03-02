package com.madokasoftwares.mvvmkotlin.data


import android.app.Application
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.madokasoftwares.mvvmkotlin.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


//database
@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao //we will use this function to get handle to our TaskDao to the actual db operations //we will use dependency injection to get it to the place where is needed

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,//getting the db in the callback
        @ApplicationScope private val applicationScope: CoroutineScope //remember from our APPModule injection
    ) : RoomDatabase.Callback() { //@Inject works the same as @Provide

        //this method will be executed the first time we start/open our db
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()
            //db operations
            applicationScope.launch {
                //we can now execute our suspend functions in this coroutine
                dao.insert(Task("wash the dishes"))
                dao.insert(Task("Do the laundry", completed = true))
                dao.insert(Task("prepare food"))
                dao.insert(Task("welcome home"))
                dao.insert(Task("I love Mum", important = true))
                dao.insert(Task("visit grandma"))
                dao.insert(Task("hey there a using whatapp", completed = true))

            }

        }
    }

}

//dependency injection-classes that use certain other classes should not be responsible for either creating the classes or searching for them