package com.gokul.gallery.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gokul.gallery.repository.UnSplashRepo
import kotlin.random.Random


class UnSplashViewModel @ViewModelInject constructor(private val unSplashRepo: UnSplashRepo):ViewModel() {
    private val list= listOf("dogs","cats","pugs","pug","rog","space","tech","flowers","animals")
    private val random= Random.nextInt(list.size)
    private val DEFAULT_QUERY=list[random]
    private var currentQuery=MutableLiveData(DEFAULT_QUERY)
    fun searchPhotos(query:String){
        currentQuery.value=query

    }
    val photos=currentQuery.switchMap {queryString->
        unSplashRepo.getSearchResults(queryString).cachedIn(viewModelScope)
    }
}