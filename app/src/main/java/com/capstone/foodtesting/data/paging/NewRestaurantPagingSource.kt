package com.capstone.foodtesting.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.foodtesting.data.api.UnsplashApi
import com.capstone.foodtesting.data.model.Result
import retrofit2.HttpException
import java.io.IOException

class NewRestaurantPagingSource (
    private val api: UnsplashApi,
    private val query: String,
    private val order_by: String
        ): PagingSource<Int, Result >() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) // 그주위의 페이지를 읽어오는 역할
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val pageNumber = params.key ?: STARTING_PAGE_INDEX
            val response = api.getKoreanFoods(query = query, page = pageNumber, per_page = params.loadSize, order_by = order_by)
            val endOfPaginationReached = response.body()?.results?.isEmpty()?: true

            val data = response.body()?.results ?: throw IOException()
            val prevKey = if(pageNumber == STARTING_PAGE_INDEX) null else pageNumber-1
            val nextKey = if(endOfPaginationReached) {
                null
            } else {
                pageNumber + (params.loadSize / PAGING_SIZE)
            }
            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey
            )


        } catch (E: IOException) {
            LoadResult.Error(E)
        } catch (E: HttpException) {
            LoadResult.Error(E)
        }
    }

    companion object {
        const val  STARTING_PAGE_INDEX = 1
        const val PAGING_SIZE = 15
    }
}