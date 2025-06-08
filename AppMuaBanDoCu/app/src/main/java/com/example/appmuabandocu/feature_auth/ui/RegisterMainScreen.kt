import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmuabandocu.feature_auth.AuthViewModel

@Composable
fun RegisterMainScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
    ) {
    // Biến để lưu thông tin đăng ký
    val email by authViewModel.email.collectAsState()
    val fullName by authViewModel.fullName.collectAsState()
    val phoneNumber by authViewModel.phoneNumber.collectAsState()
    val password by authViewModel.password.collectAsState()

    // Thêm biến confirmPassword để xác nhận mật khẩu
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Lấy context để hiển thị Toast
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = "Logo",
            )

            Text(
                text = "Đăng ký tài khoản",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Blue_text,
            )

            // Họ và tên
            OutlinedTextField(
                value = fullName,
                onValueChange = { authViewModel.setFullName(it) },
                label = { Text(text = "Họ và tên") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Blue_text,
                    unfocusedIndicatorColor = Blue_text,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Số điện thoại
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { authViewModel.setPhoneNumber(it)},
                label = { Text(text = "Số điện thoại") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Blue_text,
                    unfocusedIndicatorColor = Blue_text,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { authViewModel.setEmail(it) },
                label = { Text(text = "Email") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Blue_text,
                    unfocusedIndicatorColor = Blue_text,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Mật khẩu và xác nhận mật khẩu
            OutlinedTextField(
                value = password,
                onValueChange = { authViewModel.setPassword(it) },
                label = { Text(text = "Mật khẩu") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Filled.VisibilityOff
                            else
                                Icons.Filled.Visibility,
                            contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Blue_text,
                    unfocusedIndicatorColor = Blue_text,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "Xác nhận mật khẩu") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible)
                                Icons.Filled.VisibilityOff
                            else
                                Icons.Filled.Visibility,
                            contentDescription = if (confirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Blue_text,
                    unfocusedIndicatorColor = Blue_text,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Sử dụng ViewModel để đăng ký thay vì gọi Firebase trực tiếp
                    authViewModel.registerWithEmailPassword(context)
                    // Chuyển về màn hình đăng nhập khi đăng ký thành công
                    navController.navigate("login_screen")
                },
                modifier = Modifier.width(250.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue_text, contentColor = Color.White)
            ) {
                Text(text = "Đăng ký")
            }

            TextButton(onClick = { navController.navigate("login_screen") }) {
                Text(text = "Đã có tài khoản? Đăng nhập ngay")
            }
        }
    }
}