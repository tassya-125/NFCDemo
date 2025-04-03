package com.example.nfcdemo.compose


import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nfcdemo.model.PotteryEntity
import com.example.nfcdemo.network.data.response.PotteryResponse
import com.example.nfcdemo.util.StringUtil
import com.example.nfcdemo.util.TimeUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PotteryDetailScreen(
    pottery: PotteryEntity,
    modifier: Modifier = Modifier
) {

    var avatarUri by remember { mutableStateOf<Uri?>( pottery.imageUrl?.let{ Uri.parse(it) } ) }

    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 紫砂壶图片
            AsyncImage(
                model = avatarUri,
                contentDescription = "紫砂壶图片",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.height(24.dp))

            // 详细信息区域
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 作者信息
                InfoRow(
                    icon = Icons.Default.Person,
                    title = "作者",
                    content = pottery.creator ?: "未知"
                )

                // 产地信息
                InfoRow(
                    icon = Icons.Default.LocationOn,
                    title = "产地",
                    content = pottery.origin?.let{StringUtil.getOriginValue(it)} ?: "未记录"
                )

                // 制作时间
                InfoRow(
                    icon = Icons.Default.DateRange,
                    title = "制作时间",
                    content = pottery.productionTime?.let{ TimeUtil.formatDate(it)} ?: "年代未知"
                )

                // 工艺标题
                Text(
                    text = "制作工艺",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // 工艺内容
                Text(
                    text = pottery.craftsmanshipProcess ?: "暂无工艺描述",
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    title: String,
    content: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "$title：",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// 日期格式化扩展函数
fun Date?.format(): String {
    if (this == null) return ""
    return SimpleDateFormat("yyyy年MM月", Locale.CHINA).format(this)
}

@SuppressLint("SimpleDateFormat")
@Preview
@Composable
fun PreviewPotteryDetailScreen() {
    MaterialTheme {
        PotteryDetailScreen(
            pottery = PotteryEntity(
                uid = "1",
                creator = "顾景舟",
                origin = "江苏宜兴",
                productionTime = SimpleDateFormat("yyyy-MM").parse("1985-05").toString(),
                craftsmanshipProcess = "采用传统拍打成型工艺，历经选料、陈腐、成型、烧制等32道工序。泥料选用上等紫泥，经三年以上陈腐，窑温控制精准，呈现独特紫茄色泽。",
                imageUrl = "https://example.com/zisha-pot"
            )
        )
    }
}



