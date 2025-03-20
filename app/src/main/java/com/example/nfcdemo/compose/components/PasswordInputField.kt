package com.example.nfcdemo.compose.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation.Companion.None


@Composable
fun PasswordInputField(
    password: String,
    onPasswordChange: (String) -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("密码") },
        leadingIcon = { Icon(Icons.Default.Lock, null) },
        modifier = Modifier.fillMaxWidth()
    )
}