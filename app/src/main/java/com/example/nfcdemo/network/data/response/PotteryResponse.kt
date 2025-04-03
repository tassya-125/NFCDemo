package com.example.nfcdemo.network.data.response

import com.example.nfcdemo.model.PotteryEntity
import com.example.nfcdemo.network.api.PotteryApi
import com.google.gson.annotations.SerializedName

data class PotteryResponse(
    @SerializedName("pottery") val pottery: PotteryEntity
)