package com.example.nfcdemo.activity


import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nfcdemo.R


class NFCActivity : AppCompatActivity()  {
    private var nfcAdapter: NfcAdapter? = null
    private var nfcDataTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        // 获取 NFC 适配器
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // 确保设备支持 NFC
        if (nfcAdapter == null) {
            Toast.makeText(this, "此设备不支持 NFC", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 检查 NFC 是否启用
        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "NFC 未启用", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 设置显示 NFC 数据的 TextView
        nfcDataTextView = findViewById<TextView>(R.id.nfcDataTextView)
    }

    override fun onResume() {
        super.onResume()
        // 启动 NFC 标签监听
        enableNfcForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        // 停止 NFC 标签监听
        disableNfcForegroundDispatch()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // 获取 NFC 标签
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

        if (tag != null) {
            // 获取 NFC 标签的 ID
            val tagId = tag.id
            val tagIdString = bytesToHex(tagId)

            // 显示标签 ID
            nfcDataTextView!!.text = "NFC 标签 ID: $tagIdString"
            Log.d(TAG, "NFC 标签 ID: $tagIdString")

            // 读取标签的数据
            readNfcData(tag)
        }else{
            Log.d(TAG, "tag is null")
        }

    }

    private fun enableNfcForegroundDispatch() {
        // 为当前 Activity 启用 NFC 前台分发
        nfcAdapter!!.enableForegroundDispatch(
            this,
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0
            ),
            null, null
        )
    }

    private fun disableNfcForegroundDispatch() {
        // 禁用 NFC 前台分发
        nfcAdapter!!.disableForegroundDispatch(this)
    }

    private fun readNfcData(tag: Tag) {
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
                    nfcDataTextView!!.text = "NFC-A UID: $uidHex"

                    // 读取标签的数据（例如读取块数据）
                    // 读取块 4（MIFARE Classic 标签的一个数据块）
                    val blockData = nfcA.transceive(byteArrayOf(0x30.toByte(), 0x04.toByte()))
                    val blockDataHex = bytesToHex(blockData)
                    Log.d(TAG, "Block 4 Data: $blockDataHex")
                    nfcDataTextView!!.append("\nBlock 4 Data: $blockDataHex")

                    // 关闭连接
                    nfcA.close()
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
                nfcDataTextView!!.append("\nNfcB UID: $uidHex")
                nfcB.close()
            }

            if (isoDep != null) {
                // 读取 IsoDep 标签的数据（例如银行卡或支付卡）
                isoDep.connect()
                val historicalBytes = isoDep.historicalBytes
                val historicalData = bytesToHex(historicalBytes)
                Log.d(TAG, "IsoDep Data: $historicalData")
                nfcDataTextView!!.append("\nIsoDep Data: $historicalData")
                isoDep.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, "读取 NFC 标签数据失败", e)
        }
    }

    // 将字节数组转换为十六进制字符串
    private fun bytesToHex(bytes: ByteArray): String {
        val stringBuilder = StringBuilder()
        for (b in bytes) {
            stringBuilder.append(String.format("%02X", b))
        }
        return stringBuilder.toString()
    }

    companion object {
        private const val TAG = "NfcActivity"
    }
}
