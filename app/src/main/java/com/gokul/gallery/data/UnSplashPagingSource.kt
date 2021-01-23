package com.gokul.gallery.data

import androidx.paging.PagingSource
import com.gokul.gallery.data.api.UnSplashApi
import com.gokul.gallery.model.UnSplashModel
import java.io.IOException
import retrofit2.HttpException

private const val UNSPLASH_PAGE_POSITION=1
class UnSplashPagingSource(
    private val unSplashApi: UnSplashApi,
    private val query:String
):PagingSource<Int,UnSplashModel>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnSplashModel> {
        val position=params.key?: UNSPLASH_PAGE_POSITION
        return try {
            val response=unSplashApi.getPhotos(query,position,params.loadSize) // Retrofit Api Call
            val photos=response.results
            LoadResult.Page(
                data = photos,
                prevKey = if(position== UNSPLASH_PAGE_POSITION)null else position-1,
                nextKey = if(photos.isEmpty())null else position+1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

}