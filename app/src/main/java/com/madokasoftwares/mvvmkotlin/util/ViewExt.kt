package com.madokasoftwares.mvvmkotlin.util

import androidx.appcompat.widget.SearchView

//extension function for our searchview  so later in our fragment we can call it directly
inline fun SearchView.onQueryTextChanged(crossinline listener:(String) -> Unit){
    this.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty()) //pasting te text we type in the searchviewer to our listerner
            return true
        }
    })
}
//inline-make our code more efficient it has no important for functionality