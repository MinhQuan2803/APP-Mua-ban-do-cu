package com.example.appmuabandocu.feature_auth

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmuabandocu.R
import com.example.appmuabandocu.data.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AuthViewModel: ViewModel() {
    private val authRepository = AuthRepository()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(authRepository.isUserLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()


    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    fun setEmail(email: String) {
        _email.value = email
    }
    fun setPassword(password: String) {
        _password.value = password
    }

    fun setFullName(fullName: String) {
        _fullName.value = fullName
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun getCurrentUser(): String? {
        return authRepository.getCurrentUserId()
    }

    fun signInWithEmailPassword(context: Context) {
        if (email.value.isEmpty() || password.value.isEmpty()) {
            Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            _isLoggedIn.value = true
            try {
                authRepository.signInWithEmailPassword(email.value, password.value)
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            finally {
                _isLoggedIn.value = false
            }

        }
    }

    fun registerWithEmailPassword(context: Context) {
        if (email.value.isEmpty() || password.value.isEmpty() || fullName.value.isEmpty()) {
            Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            try {
                // Đăng ký với Firebase Authentication
                authRepository.registerWithEmailPassword(email.value, password.value)

                // Lấy userId mới đăng ký
                val userId = authRepository.getCurrentUserId()
                if (userId != null) {
                    // Tạo dữ liệu người dùng
                    val userInfo = User(
                        uid = userId,
                        fullName = fullName.value,
                        email = email.value,
                        phoneNumber = phoneNumber.value,
                        createdAt = System.currentTimeMillis()
                    )

                    // Lưu vào Firestore và cập nhật displayName
                    authRepository.updateUserProfile(userInfo)
                    Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Đăng ký thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getGoogleSignInOptions(context: Context): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(context.getString(R.string.web_client_id))
            .build()
    }

    fun signInWithGoogle(account: GoogleSignInAccount?, context: Context) {
        viewModelScope.launch {
            _isLoggedIn.value = true
            try {
                if (account == null) {
                    Toast.makeText(context, "Google sign-in failed: Account is null", Toast.LENGTH_SHORT).show()
                    throw Exception("Google sign-in failed: Account is null")
                }
                val idToken = account.idToken ?: throw Exception("Google sign-in failed: ID token is null")
                authRepository.signWithGoogle(idToken)
                Toast.makeText(context, "Google sign-in successful", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                _isLoggedIn.value = false
            }
        }
    }

    fun signOut(context: Context) {
        authRepository.signOut()

        val googleSignInOptions = getGoogleSignInOptions(context)
        val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
        googleSignInClient.signOut()

        _isLoggedIn.value = false
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }


}