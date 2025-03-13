package com.example.nfcdemo.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfcdemo.R

@Composable
fun ContentScreen(
    imageRes: Int? ,
    text1: String = "这是第一段文字。",
    text2: String = "这是第二段文字，描述一些内容。",
    text3: String = "这是第三段文字，进一步补充信息。"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes ?: R.drawable.empty_image),
            contentDescription = "Sample Image",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = text1, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = text2, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = text3, fontSize = 14.sp)
    }
}

@Preview
@Composable
fun PreviewContentScreen() {
    ContentScreen(imageRes = null)
}
