package com.yolo.yolo_android.ui.community_list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yolo.yolo_android.CommunitySort
import com.yolo.yolo_android.api.ApiService
import com.yolo.yolo_android.db.post.PostEntity
import com.yolo.yolo_android.model.CommonResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CommunityDataSource @Inject constructor(
    private val service: ApiService,
    private val sorted: CommunitySort
): PagingSource<Int, PostEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostEntity> {

        try {
            val currentLoadingPageKey = params.key ?: 1

            val response = service.getCommunityList(
                page = currentLoadingPageKey,
                sort = sorted.sorted
            )
//            if (response.response.header.resultCode != "0000") throw Exception(response.response.header.resultCode)
            val responseData = response.result

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
            val nextKey = if (responseData.isEmpty()) null else currentLoadingPageKey.plus(1)
            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = nextKey
            )
        }catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(Exception("인터넷 연결을 확인해 주세요."))
        } catch (e: HttpException) {
            val body = e.response()?.errorBody()
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter<CommonResponse>(CommonResponse::class.java)
            val response = jsonAdapter.fromJson(body?.string())
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(Exception(response?.errorMessage))
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PostEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
//            state.closestPageToPosition(anchorPosition)?.prevKey
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}