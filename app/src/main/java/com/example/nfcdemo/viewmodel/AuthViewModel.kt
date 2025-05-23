// AuthViewModel.kt
package com.example.nfcdemo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfcdemo.model.User
import com.example.nfcdemo.network.data.response.AuthResponse
import com.example.nfcdemo.network.repository.AuthRepository
import com.example.nfcdemo.util.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val TAG = "USER_NET"

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class LoginSuccess(val user:User,val token: String) : AuthState()
        data class RegisterSuccess(val user: User, val token: String) : AuthState()
        class SaveSuccess:AuthState()
        object CodeSentSuccess : AuthState()
        data class Error(val message: String, val type: OperationType) : AuthState()
    }

    enum class OperationType {
        LOGIN, REGISTER, SEND_CODE,SAVE
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(identifier: String, password: String) {
        executeAuthOperation(
            operationType = OperationType.LOGIN,
            action = { repository.login(identifier, password) },
            successHandler = { response ->
                validateAuthResponse(response)?.let { (user, token) ->
                    UserManager.saveUser(user,token)
                    AuthState.LoginSuccess(user, token)
                } ?: AuthState.Error("Invalid registration data", OperationType.LOGIN)
            }
        )
    }

    fun register(identifier: String, password: String, code: String,isUsingPhone :Boolean) {
        executeAuthOperation(
            operationType = OperationType.REGISTER,
            action = { repository.register(identifier, password, code,isUsingPhone) },
            successHandler = { response ->
                validateAuthResponse(response)?.let { (user, token) ->
                    UserManager.saveUser(user,token)
                    AuthState.RegisterSuccess(user, token)
                } ?: AuthState.Error("Invalid registration data", OperationType.REGISTER)
            }
        )
    }

    fun  save(user: User){
        executeAuthOperation(
            operationType = OperationType.SAVE,
            action = { repository.save(user) },
            successHandler = { response ->
                 response?.let {
                    Log.d(TAG,"success")
                    AuthState.SaveSuccess()
                } ?: AuthState.Error("Invalid registration data", OperationType.SAVE)
            }
        )
    }

    fun sendVerificationCode(identifier: String) {
        executeAuthOperation(
            operationType = OperationType.SEND_CODE,
            action = { repository.sendVerificationCode(identifier) },
            successHandler = {
                AuthState.CodeSentSuccess
            }
        )
    }

    private inline fun <T> executeAuthOperation(
        operationType: OperationType,
        crossinline action: suspend () -> Result<T>,
        crossinline successHandler: (T) -> AuthState
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = action()
                result.fold(
                    onSuccess = { data ->
                        _authState.value = successHandler(data)
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(
                            message = parseErrorMessage(exception, operationType),
                            type = operationType
                        )
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    message = "Network error: ${e.message ?: "Unknown error"}",
                    type = operationType
                )
            }
        }
    }

    private fun validateAuthResponse(response: AuthResponse): Pair<User, String>? {
        return response.user?.let { user ->
            response.token?.takeIf { it.isNotBlank() }?.let { token ->
                user to token
            }
        }
    }

    private fun parseErrorMessage(exception: Throwable, type: OperationType): String {
        return when (exception) {
            is java.net.ConnectException -> "无法连接服务器"
            is java.net.SocketTimeoutException -> "请求超时"
            is java.net.UnknownHostException -> "网络不可用"
            else -> exception.message ?: when (type) {
                OperationType.LOGIN -> "登录失败"
                OperationType.REGISTER -> "注册失败"
                OperationType.SEND_CODE -> "验证码发送失败"
                OperationType.SAVE -> "用户信息更新失败"
            }
        }
    }
}
