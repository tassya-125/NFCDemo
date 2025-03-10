package com.example.nfcdemo.compose


import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

@Composable
fun UserProfileScreen() {
    // 模拟用户信息
    var username by remember { mutableStateOf("John Doe") }
    var phoneNumber by remember { mutableStateOf("123-456-7890") }
    var email by remember { mutableStateOf("johndoe@example.com") }

    // 编辑模式控制
    var isEditing by remember { mutableStateOf(false) }

    // 临时存储编辑的数据
    var newUsername by remember { mutableStateOf(username) }
    var newPhoneNumber by remember { mutableStateOf(phoneNumber) }
    var newEmail by remember { mutableStateOf(email) }

    // 模拟头像上传
    var avatarUrl by remember { mutableStateOf("") } // 默认为空
    var isUploadingVisible by remember { mutableStateOf(false) } // 控制上传按钮的显示

    // 保存更新后的信息
    fun saveUserInfo() {
        if (isValidPhoneNumber(newPhoneNumber) && isValidEmail(newEmail)) {
            username = newUsername
            phoneNumber = newPhoneNumber
            email = newEmail
            isEditing = false
        } else {
            // 显示格式错误信息
            // 可以通过 Snackbar 或 Toast 来提示错误
            // For simplicity, let's just show a Toast or Snackbar in actual code
        }
    }

    // 用户信息显示及修改界面
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start, // 左对齐
            verticalArrangement = Arrangement.Top
        ) {
            // 头像与上传按钮在同一行显示
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // 头像显示
                if (avatarUrl.isEmpty()) {
                    // 默认头像
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                            .clickable { isUploadingVisible = !isUploadingVisible }, // 点击头像切换上传按钮显示
                        tint = Color.White
                    )
                } else {
                    // 使用头像 URL
                    Image(
                        painter = rememberImagePainter(avatarUrl),
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                            .clickable { isUploadingVisible = !isUploadingVisible }, // 点击头像切换上传按钮显示
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 上传头像的按钮
                if (isUploadingVisible || avatarUrl.isEmpty()) {
                    IconButton(
                        onClick = { /* 执行头像上传逻辑 */ },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Gray, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add, // 使用+图标
                            contentDescription = "Upload Avatar",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            // 标题
//            Text(
//                text = "用户信息",
//                style = TextStyle(
//                    fontSize = 32.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(bottom = 20.dp)
//            )

            // 如果处于编辑模式，显示编辑框
            if (isEditing) {
                UserInputField(
                    value = newUsername,
                    label = "用户名",
                    onValueChange = { newUsername = it }
                )
                Spacer(modifier = Modifier.height(16.dp)) // 每条信息之间的间隔

                UserInputField(
                    value = newPhoneNumber,
                    label = "手机号",
                    onValueChange = { newPhoneNumber = it }
                )
                Spacer(modifier = Modifier.height(16.dp))

                UserInputField(
                    value = newEmail,
                    label = "邮箱",
                    onValueChange = { newEmail = it }
                )
                Spacer(modifier = Modifier.height(20.dp))

                // 保存和取消按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { saveUserInfo() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81D4FA)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("保存", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { isEditing = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("取消", color = Color.White)
                    }
                }
            } else {
                // 显示当前用户信息
                UserInfoDisplay(label = "当前用户名：$username")
                Spacer(modifier = Modifier.height(16.dp)) // 每条信息之间的间隔

                UserInfoDisplay(label = "当前手机号：$phoneNumber")
                Spacer(modifier = Modifier.height(16.dp))

                UserInfoDisplay(label = "当前邮箱：$email")

                // 编辑按钮
                Spacer(modifier = Modifier.height(80.dp))

                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        , // 将按钮推到底部
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81D4FA)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("编辑信息", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun UserInfoDisplay(label: String) {
    Text(
        text = label,
        style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputField(value: String, label: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF81D4FA),
            unfocusedBorderColor = Color(0xFFB0BEC5),
        )
    )
}

// 验证手机号格式
fun isValidPhoneNumber(phone: String): Boolean {
    return phone.matches(Regex("^\\d{3}-\\d{3}-\\d{4}$"))
}

// 验证邮箱格式
fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Preview
@Composable
fun UserScreen() {
    UserProfileScreen()
}
