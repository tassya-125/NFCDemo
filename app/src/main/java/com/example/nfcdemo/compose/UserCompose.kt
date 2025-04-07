package com.example.nfcdemo.compose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nfcdemo.R
import com.example.nfcdemo.compose.components.LogOutConfirmDialog
import com.example.nfcdemo.compose.components.ResetPasswordDialog
import com.example.nfcdemo.compose.components.SecurityActionsSection
import com.example.nfcdemo.model.User
import com.example.nfcdemo.util.OSSUtil
import com.example.nfcdemo.util.ToastUtil
import com.example.nfcdemo.util.UserManager
import com.example.nfcdemo.viewmodel.AuthViewModel
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(onLogOut:()->Unit) {
    val context = LocalContext.current
    val authViewModel :AuthViewModel = viewModel()
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    val user : User? = UserManager.getCurrentUser()
    var username by remember { mutableStateOf(user?.username) }
    var phoneNumber by remember { mutableStateOf(user?.phoneNumber) }
    var email by remember { mutableStateOf(user?.email) }
    var avatarUri by remember { mutableStateOf<Uri?>( user?.image?.let{ Uri.parse(it) } ) }
    var isUploading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()  // 获取协程作用域
    var showResetPasswordDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }


    fun uploadImage(context: Context, uri: Uri) {
        isUploading = true  // 开始上传时显示上传状态
        coroutineScope.launch {
            val uploadedUrl = OSSUtil.uploadImageToOss(uri, context)  // 调用上传工具类
            if (uploadedUrl != null) {
                avatarUri = Uri.parse(uploadedUrl) // 更新头像为已上传的图片 URL
                user?.let {
                    it.image=uploadedUrl
                    authViewModel.save(it)
                    ToastUtil.show(context,"修改成功",ToastUtil.SUCCESS)
                }
            }else{
                ToastUtil.show(context,"修改失败",ToastUtil.ERROR)
            }
            isUploading = false
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uploadImage(context, it)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val savedUri = saveImageToGallery(context, it)
            savedUri?.let { uploadImage(context, it) }  // 保存并上传
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.CAMERA] == true -> showImageSourceDialog = true
            else -> {/* 处理权限拒绝 */}
        }
    }

    fun checkPermissions() {
        val requiredPermissions = arrayListOf(Manifest.permission.CAMERA)
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (requiredPermissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }) {
            showImageSourceDialog = true
        } else {
            permissionLauncher.launch(requiredPermissions.toTypedArray())
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.profile_title)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 头像部分
            ProfileAvatarSection(
                avatarUri = avatarUri,
                onEditClick = { checkPermissions() },
                isUploading = isUploading
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 信息卡片
            ProfileInfoCard(
                isEditing = isEditing,
                username = username?:"",
                phoneNumber = phoneNumber?:"",
                email = email?:"",
                onUsernameChange = {newUserName ->
                    username=newUserName
                    user?.let{
                        it.username=newUserName
                    }
                },
                onPhoneChange = { newPhone ->
                    phoneNumber=newPhone
                    user?.let {
                        it.phoneNumber = newPhone
                    }
                },
                onEmailChange = { newEmail ->
                    email = newEmail
                    user?.let{
                        it.email=newEmail
                    }
                },
                onEditToggle = {
                    isEditing = !isEditing
                },
                onSave = {
                    isEditing = !isEditing
                    user?.let{
                        authViewModel.save(it)
                        ToastUtil.show(context,"修改成功",ToastUtil.SUCCESS)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            SecurityActionsSection(
                onResetPassword = { showResetPasswordDialog=true },
                onLogout = { showLogoutConfirmDialog =true}
            )
        }

        if (showResetPasswordDialog) {
            ResetPasswordDialog(
                onDismiss = { showResetPasswordDialog = false },
                onConfirm = { newPassword ->
                    // 调用修改密码API
                    coroutineScope.launch {
                        user?.password=newPassword
                        user?.let{
                            authViewModel.save(it)
                            ToastUtil.show(context,"修改成功",ToastUtil.SUCCESS)
                        }
                    }
                }
            )
        }

// 退出确认对话框
        if (showLogoutConfirmDialog) {
            LogOutConfirmDialog(onDismiss = {showLogoutConfirmDialog = false}, onCancle = {showLogoutConfirmDialog = false},onLogOut)
        }

        // 图片来源对话框
        if (showImageSourceDialog) {
            ImageSourceSelectionDialog(
                onDismiss = { showImageSourceDialog = false },
                onGallerySelect = { galleryLauncher.launch("image/*") },
                onCameraSelect = { cameraLauncher.launch(null) }
            )
        }
    }
}

@Composable
private fun ProfileAvatarSection(
    avatarUri: Uri?,
    onEditClick: () -> Unit,
    isUploading: Boolean
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.size(160.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onEditClick)
        ) {
            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (avatarUri != null) {
                AsyncImage(
                    model = avatarUri,
                    contentDescription = stringResource(R.string.profile_avatar_desc),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
private fun ProfileInfoCard(
    isEditing: Boolean,
    username: String,
    phoneNumber: String,
    email: String,
    onUsernameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onEditToggle: () -> Unit,
    onSave: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isEditing) {
                ProfileEditableFields(
                    username = username,
                    phoneNumber = phoneNumber,
                    email = email,
                    onUsernameChange = onUsernameChange,
                    onPhoneChange = onPhoneChange,
                    onEmailChange = onEmailChange
                )
                ProfileActionButtons(
                    onSave = onSave,
                    onCancel =  onEditToggle
                )
            } else {
                ProfileInfoDisplay(
                    username = username,
                    phoneNumber = phoneNumber,
                    email = email
                )
                Button(
                    onClick =  onEditToggle ,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.edit_profile),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileEditableFields(
    username: String,
    phoneNumber: String,
    email: String,
    onUsernameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text(stringResource(R.string.username)) },
            leadingIcon = { Icon(Icons.Outlined.Person, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneChange,
            label = { Text(stringResource(R.string.phone)) },
            leadingIcon = { Icon(Icons.Outlined.Phone, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email)) },
            leadingIcon = { Icon(Icons.Outlined.Email, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun ProfileActionButtons(
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(R.string.cancel))
        }

        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
private fun ProfileInfoDisplay(
    username: String,
    phoneNumber: String,
    email: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        InfoListItem(
            icon = Icons.Outlined.Person,
            label = stringResource(R.string.username),
            value = username
        )

        Divider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            thickness = 1.dp
        )

        InfoListItem(
            icon = Icons.Outlined.Phone,
            label = stringResource(R.string.phone),
            value = phoneNumber
        )

        Divider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            thickness = 1.dp
        )

        InfoListItem(
            icon = Icons.Outlined.Email,
            label = stringResource(R.string.email),
            value = email
        )
    }
}

@Composable
private fun InfoListItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ImageSourceSelectionDialog(
    onDismiss: () -> Unit,
    onGallerySelect: () -> Unit,
    onCameraSelect: () -> Unit,
) {
    AlertDialog(
        shape = RectangleShape,
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.CheckCircle, null) },
        title = { Text(stringResource(R.string.choose_image_source)) },
        text = { Text(stringResource(R.string.select_image_source_prompt)) },
        confirmButton = {
            TextButton(onClick = {
                onGallerySelect()
                onDismiss()
            }) {
                Text(stringResource(R.string.gallery))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onCameraSelect()
                onDismiss()
            }) {
                Text(stringResource(R.string.camera))
            }
        }
    )
}

private fun saveImageToGallery(context: Context, bitmap: Bitmap): Uri? {
    return MediaStore.Images.Media.insertImage(
        context.contentResolver,
        bitmap,
        "Profile_${System.currentTimeMillis()}",
        "User profile picture"
    )?.let { Uri.parse(it) }
}


