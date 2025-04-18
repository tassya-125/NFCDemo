package com.example.nfcdemo.network.page

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.nfcdemo.model.SaleRecord
import com.example.nfcdemo.network.RetrofitClient
import com.example.nfcdemo.network.api.SaleRecordApi
import com.example.nfcdemo.util.UserManager

class SaleRecordPagingSource : PagingSource<Int, SaleRecord>() {
    private val api = RetrofitClient.instance.create(SaleRecordApi::class.java)
    private val TAG = "SaleRecordPaging"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SaleRecord> {
        Log.d(TAG, "开始加载销售记录数据")
        return try {
            val page = params.key ?: 1 // 当前页码，默认为1
            val queryParams = mutableMapOf<String, String>().apply {
                put("page", page.toString())
                put("limit", "10") // 每页10条数据

                // 添加用户ID（如果需要）
                UserManager.getUserId()?.let { userId ->
                    put("id", userId.toString())
                }
            }

            Log.d(TAG, "请求参数: $queryParams")
            val response = api.getList(queryParams)

            if (response.isSuccessful) {
                val data = response.body()?.page?.list ?: emptyList()
                Log.d(TAG, "获取到 ${data.size} 条销售记录")

                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.size < 10) null else page + 1
                )
            } else {
                Log.e(TAG, "网络请求失败: ${response.code()}")
                LoadResult.Error(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "加载异常: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SaleRecord>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }
}