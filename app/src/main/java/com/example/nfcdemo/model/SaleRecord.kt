package com.example.nfcdemo.model

import java.util.Date

data class SaleRecord (
    val  saleId :Int,
    val seller : User,
    val buyer:User,
    val saleTime: Date,
    val pottery : PotteryEntity
)