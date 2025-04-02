package com.example.nfcdemo

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.nfcdemo.compose.screen.AppNavigation
import com.example.nfcdemo.util.NFCUtil
import com.example.nfcdemo.util.NFCUtil.readNfcData
import com.example.nfcdemo.util.ToastUtil


class MainActivity : AppCompatActivity() {

    private var nfcListener: ((String) -> Unit)? = null

    private val TAG = "MAIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NFCUtil.init(this)
        setContent {
            AppNavigation()
        }
    }

    fun setNfcListener(listener: (String) -> Unit) {
        nfcListener = listener
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "New NFC intent received: ${intent.action}")
        // 获取 NFC 标签
        val tag = intent.getParcelableExtra<android.nfc.Tag>(NfcAdapter.EXTRA_TAG)

        if (tag != null) {
            Log.d(TAG, tag.toString())
            // 获取 NFC 标签的 ID
            tag.let {
                val data = readNfcData(it)
                nfcListener?.invoke(data) // 通过回调传递数据
            }
        }else{
            ToastUtil.show(this,"NFC不符合格式",ToastUtil.ERROR)
            Log.e(TAG, "Tag is null. Intent extras: ${intent.extras}")
        }
    }


}