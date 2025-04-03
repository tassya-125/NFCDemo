package com.example.nfcdemo.network.data.response

import com.example.nfcdemo.model.PageData
import com.example.nfcdemo.model.PotteryEntity
import com.example.nfcdemo.model.User
import com.google.gson.annotations.SerializedName

data class VerificationHistoryListResponse(
    val page: PageData<VerificationHistoryResponse> // PageData 是泛型类，包含分页数据
)

