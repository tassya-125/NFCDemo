package com.example.nfcdemo.network.data.request

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class VerificationHistoryRequest(
    @SerializedName("verifierId") val verifierId:Long,
    @SerializedName("verificationResult") val verificationResult:Boolean,
    @SerializedName("potteryUid") val potteryUid:String,
    @SerializedName("verificationTime") val verificationTime: String
)