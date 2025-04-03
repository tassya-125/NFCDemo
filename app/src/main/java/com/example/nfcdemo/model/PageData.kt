package com.example.nfcdemo.model

data class PageData<T>(
    val list: List<T>,  // 具体的数据列表
    val totalCount: Int, // 总条数
    val pageSize: Int,   // 每页条数
    val totalPage: Int,  // 总页数
    val currPage: Int    // 当前页码
)
