package com.example.nfcdemo.Activity

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfcdemo.ui.theme.NFCDemoTheme
import java.nio.charset.Charset

class NFCActivity : ComponentActivity() {
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var nfcContent = mutableStateOf("")
    private val TAG = "NFCActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        // 初始化 NFC 适配器
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // 初始化 PendingIntent
        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        // 处理启动 Intent
        handleIntent(intent)

        enableEdgeToEdge()
        setContent {
            NFCDemoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NFCContent(
                        nfcContent = nfcContent.value,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        nfcAdapter?.enableForegroundDispatch(
            this,
            pendingIntent,
            null,
            null
        )
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent")
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        Log.d(TAG, "handleIntent: ${intent?.action}")
        when (intent?.action) {
            NfcAdapter.ACTION_NDEF_DISCOVERED -> {
                Log.d(TAG, "ACTION_NDEF_DISCOVERED")
                intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                    val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                    // 处理第一条消息的第一个记录
                    val record = messages[0].records[0]
                    val payload = record.payload
                    val textData = String(payload, Charset.forName("UTF-8"))
                    // 更新 UI
                    nfcContent.value = textData
                }
            }
            NfcAdapter.ACTION_TECH_DISCOVERED -> {
                Log.d(TAG, "ACTION_TECH_DISCOVERED")
            }
            NfcAdapter.ACTION_TAG_DISCOVERED -> {
                Log.d(TAG, "ACTION_TAG_DISCOVERED")
            }
        }
    }
    @Composable
    fun NFCContent(
        nfcContent: String,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (nfcContent.isEmpty()) {
                Text(
                    text = "请将NFC标签靠近手机背面",
                    style = MaterialTheme.typography.headlineSmall
                )
            } else {
                Text(
                    text = "NFC内容：",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = nfcContent,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
