package com.example.nfcdemo.network.data.response

import com.example.nfcdemo.model.PageData
import com.example.nfcdemo.model.SaleRecord

data class SaleRecordListResponse(
    val page :PageData<SaleRecord>
)