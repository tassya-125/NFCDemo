package com.example.nfcdemo.model

data class VerificationHistory(
    val id : Long,
    val pottery: PotteryEntity?,
    val user: User?,
    val verificationResult: Boolean,
    val verificationDate: String
)