package com.gokul.gallery.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.gokul.gallery.data.UnSplashPagingSource
import com.gokul.gallery.data.api.UnSplashApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnSplashRepo @Inject constructor(private val unSplashApi: UnSplashApi) {
    fun getSearchResults(query: String) =
            Pager(
                    config = PagingConfig(
                            pageSize = 10,
                            maxSize = 100,
                            enablePlaceholders = false
                    ),
                    pagingSourceFactory = { UnSplashPagingSource(unSplashApi, query) }
            ).liveData
}