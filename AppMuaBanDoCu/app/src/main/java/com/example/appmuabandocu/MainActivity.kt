package com.example.appmuabandocu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.appmuabandocu.core.navigation.MyAppNavigation
import com.example.appmuabandocu.ui.theme.AppMuaBanDoCuTheme

class MainActivity : ComponentActivity() {

//    private val googleAuthUiClient by lazy {
//        GoogleAuthUiClient(
//            context = applicationContext,
//            oneTapClient = Identity.getSignInClient(applicationContext)
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppMuaBanDoCuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    MyAppNavigation()
                }
            }
        }
    }
}

//            AppMuaBanDoCuTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MyAppNavigation(modifier = Modifier.padding(innerPadding), googleAuthUiClient)
//                }
//            }

