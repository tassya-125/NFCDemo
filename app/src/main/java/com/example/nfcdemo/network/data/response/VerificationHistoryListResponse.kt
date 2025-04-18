package com.example.nfcdemo.network.data.response

import com.example.nfcdemo.model.PageData
import com.example.nfcdemo.model.VerificationHistory

data class VerificationHistoryListResponse(
    val page: PageData<VerificationHistory> // PageData 是泛型类，包含分页数据
)

