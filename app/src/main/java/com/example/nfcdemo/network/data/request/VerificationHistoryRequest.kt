package com.example.nfcdemo.network.data.request

import com.google.gson.annotations.SerializedName
import java.util.Date

data class VerificationHistoryRequest(
    @SerializedName("verifierId")val verifierId:Long,
    @SerializedName("verificationResult") val verificationResult:Boolean,
    @SerializedName("potteryUid") val  potteryUid :Long,
    @SerializedName("verificationTime") val verificationTime :Date
)