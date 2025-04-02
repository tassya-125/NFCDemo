package com.example.nfcdemo.model

import java.util.Date

data class PotteryEntity(val uid:String, val creator :String, val origin:String, val productionTime : Date, val craftsmanshipProcess :String, val imageUrl :String ) {
}