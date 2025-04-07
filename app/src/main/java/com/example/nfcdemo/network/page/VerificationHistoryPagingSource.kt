package com.example.nfcdemo.network.page

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.nfcdemo.network.RetrofitClient
import com.example.nfcdemo.network.api.VerificationHistoryApi
import com.example.nfcdemo.network.data.response.VerificationHistoryResponse
import com.example.nfcdemo.util.UserManager

class VerificationHistoryPagingSource: PagingSource<Int, VerificationHistoryResponse>() {
    private val api = RetrofitClient.instance.create(VerificationHistoryApi::class.java)

    private val TAG = "verificationHistory"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VerificationHistoryResponse> {
        Log.d("verification","开始加载数据")
        return try {
            val page = params.key ?: 1 // 当前页码，默认 1
            val queryParams :MutableMap<String,String> = mapOf(
                "page" to page.toString(),
                "limit" to "10",
            ) .toMutableMap()

            UserManager.getUserId()?.let{
                queryParams["id"]=it.toString()
            }
            Log.d(TAG,queryParams.toString())
            val response = api.getList(queryParams) // 直接传 page 和 pageSize
            Log.d(TAG, response.toString())
            if (response.isSuccessful) {
                val data = response.body()?.page?.list ?: emptyList()
                Log.d(TAG,data.toString())
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.size<10) null else page + 1
                )
            } else {
                Log.e(TAG,"Network error")
                LoadResult.Error(Exception("Network error"))
            }
        } catch (e: Exception) {
            Log.e(TAG,e.toString())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VerificationHistoryResponse>): Int? {
        // 计算刷新时的起始页码（可选实现）
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }
}