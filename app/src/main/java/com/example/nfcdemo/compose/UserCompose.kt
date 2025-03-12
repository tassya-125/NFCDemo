package com.example.nfcdemo.compose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter

@Composable
fun UserProfileScreen() {
    val context = LocalContext.current

    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("John Doe") }
    var phoneNumber by remember { mutableStateOf("123-456-7890") }
    var email by remember { mutableStateOf("johndoe@example.com") }
    var isEditing by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) avatarUri = uri
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            avatarUri = saveImageToGallery(context, bitmap)
        }
    }

    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.CAMERA] == true &&
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        if (granted) {
            showDialog = true
        } else {
            Log.e("Permission", "用户拒绝权限")
        }
    }

    fun checkAndRequestPermissions() {
        val cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (cameraPermission == PackageManager.PERMISSION_GRANTED &&
            storagePermission == PackageManager.PERMISSION_GRANTED) {
            showDialog = true
        } else {
            requestPermissionsLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { checkAndRequestPermissions() }
            ) {
                if (avatarUri != null) {
                    Image(
                        painter = rememberImagePainter(avatarUri),
                        contentDescription = "User Avatar",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Avatar",
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = { checkAndRequestPermissions() },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Gray, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Upload Avatar",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        if (isEditing) {
            UserInputField(value = username, label = "用户名") { username = it }
            Spacer(modifier = Modifier.height(16.dp))

            UserInputField(value = phoneNumber, label = "手机号") { phoneNumber = it }
            Spacer(modifier = Modifier.height(16.dp))

            UserInputField(value = email, label = "邮箱") { email = it }
            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = { isEditing = false },
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
            UserInfoDisplay(label = "当前用户名：$username")
            Spacer(modifier = Modifier.height(16.dp))

            UserInfoDisplay(label = "当前手机号：$phoneNumber")
            Spacer(modifier = Modifier.height(16.dp))

            UserInfoDisplay(label = "当前邮箱：$email")

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = { isEditing = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81D4FA)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("编辑信息", color = Color.White)
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("选择头像") },
                text = { Text("请选择照片来源") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        pickImageLauncher.launch("image/*")
                    }) { Text("从相册选择") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        takePictureLauncher.launch(null)
                    }) { Text("拍照") }
                }
            )
        }
    }
}

fun saveImageToGallery(context: Context, bitmap: Bitmap): Uri? {
    val uri = MediaStore.Images.Media.insertImage(
        context.contentResolver,
        bitmap,
        "Avatar",
        "User Profile Picture"
    )
    return Uri.parse(uri)
}

@Composable
fun UserInfoDisplay(label: String) {
    Text(
        text = label,
        style = TextStyle(fontSize = 16.sp),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputField(value: String, label: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Preview
@Composable
fun PreviewUserProfileScreen() {
    UserProfileScreen()
}
