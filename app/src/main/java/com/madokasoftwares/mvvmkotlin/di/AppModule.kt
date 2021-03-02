package com.madokasoftwares.mvvmkotlin.di

import android.app.Application
import android.app.SharedElementCallback
import androidx.room.Room
import com.madokasoftwares.mvvmkotlin.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


//dependency injection

@Module
@InstallIn(ApplicationComponent::class) //we want to use the same db throughout our app
object AppModule {

    @Singleton //when need one instance of our Taskdatabase
    @Provides //instruction function to tell dager what to do
    fun provideDatabase( //task database when we later need this object dagger will automatically provide for us
   app:Application,
   callback: TaskDatabase.Callback
    )= Room.databaseBuilder(app,TaskDatabase::class.java,"task_database") //task_database-our db name
            .fallbackToDestructiveMigration()
            .addCallback(callback)//we dont want our recycleview to be empty
            .build()
//= means that return

    @Provides //createas our TaskDao object
    fun provideTaskDao(db:TaskDatabase) =db.taskDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
//our corountinescope we want dagger to provide this corountinescope so that we can inject it wherever we want
//SupervisorJob() tell the   corountine when one child fails sholud go on with other jobs
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

