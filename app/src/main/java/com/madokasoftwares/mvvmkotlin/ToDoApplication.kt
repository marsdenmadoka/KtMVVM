package com.madokasoftwares.mvvmkotlin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//this is just setup necessarily to activate daggerHilt
//daggerHilt activation
@HiltAndroidApp
class ToDoApplication:Application() { //we should include it in the manifest
}