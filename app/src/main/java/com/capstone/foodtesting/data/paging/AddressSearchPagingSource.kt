package com.capstone.foodtesting.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.foodtesting.data.api.KakaoAddressSearchApi
import com.capstone.foodtesting.data.api.UnsplashApi
import com.capstone.foodtesting.data.model.kakao.search.address.Document
import com.capstone.foodtesting.data.model.unsplash.Result
import com.capstone.foodtesting.util.Constants
import retrofit2.HttpException
import java.io.IOException

class AddressSearchPagingSource (
    private val api: KakaoAddressSearchApi,
    private val query: String
        ): PagingSource<Int, Document>() {
    override fun getRefreshKey(state: PagingState<Int, Document>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) // 그주위의 페이지를 읽어오는 역할
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Document> {
        return try {
            val pageNumber = params.key ?: Constants.STARTING_PAGE_INDEX
            val response = api.searchAddress(query = query, page = pageNumber, size = params.loadSize)
            val endOfPaginationReached: Boolean = response.body()?.meta?.isEnd == true

            val data: List<Document> = response.body()?.documents ?: throw IOException()

            data.map { document ->
                document.query = query
            }

            val prevKey = if(pageNumber == Constants.STARTING_PAGE_INDEX) null else pageNumber-1
            val nextKey = if(endOfPaginationReached) {
                null
            } else {
                pageNumber + (params.loadSize / Constants.PAGING_SIZE)
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

}