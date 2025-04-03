package com.example.nfcdemo.network.data.response

import com.example.nfcdemo.model.PotteryEntity
import com.example.nfcdemo.model.User

data class VerificationHistoryResponse(
    val pottery: PotteryEntity?,
    val user: User?,
    val verificationResult: Boolean,
    val verificationDate: String
)