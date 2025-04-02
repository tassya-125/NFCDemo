package com.example.nfcdemo.util

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.util.Log
import android.widget.Toast


object NFCUtil {

    private val TAG = "NFC_UTIL"
    private lateinit  var nfcAdapter: NfcAdapter


    fun init(context: Context) {
        if (!::nfcAdapter.isInitialized) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(context.applicationContext)
        }
    }


    fun check(activity: Activity):Boolean{
        // 检查 NFC 是否启用
        if (!nfcAdapter.isEnabled) {
            Toast.makeText(activity, "NFC 未启用", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun enableNfcForegroundDispatch(activity: Activity) {
        if(!check(activity)){
            return
        }
        nfcAdapter.enableForegroundDispatch(
            activity,
            PendingIntent.getActivity(
                activity,
                0,
                Intent(activity,  activity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
            ),
            null, null
        )
    }

    fun disableNfcForegroundDispatch(activity: Activity) {
        if(!check(activity)){
            return
        }
        nfcAdapter.disableForegroundDispatch(activity)
    }

    fun bytesToHex(bytes: ByteArray): String {
        val stringBuilder = StringBuilder()
        for (b in bytes) {
            stringBuilder.append(String.format("%02X", b))
        }
        return stringBuilder.toString()
    }

     fun readNfcData(tag: Tag) :String {
        // 检查标签的技术类型
        val nfcA = NfcA.get(tag)
        val nfcB = NfcB.get(tag)
        val isoDep = IsoDep.get(tag)

        try {
            if (nfcA != null) {
                // 读取 NfcA 标签的数据
                try {
                    nfcA.connect() // 连接到 NFC 标签

                    // 获取 NFC 标签的 UID（唯一标识符）
                    val uid = nfcA.tag.id
                    val uidHex = bytesToHex(uid)
                    Log.d(TAG, "NFC-A UID: $uidHex")
                    // 关闭连接
                    nfcA.close()
                    return uidHex
                } catch (e: java.lang.Exception) {
                    Log.e(TAG, "读取 NFC-A 标签数据失败", e)
                }
            }

            if (nfcB != null) {
                // 读取 NfcB 标签的数据
                nfcB.connect()
                val uid = nfcB.tag.id
                val uidHex = bytesToHex(uid)
                Log.d(TAG, "NfcB UID: $uidHex")

                nfcB.close()
                return uidHex
            }

            if (isoDep != null) {
                // 读取 IsoDep 标签的数据（例如银行卡或支付卡）
                isoDep.connect()
                val historicalBytes = isoDep.historicalBytes
                val historicalData = bytesToHex(historicalBytes)
                isoDep.close()
                return historicalData
            }
        } catch (e: Exception) {
            Log.e(TAG, "读取 NFC 标签数据失败", e)
        }
         return ""
    }
}
