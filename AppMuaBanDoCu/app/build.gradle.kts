plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.appmuabandocu"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appmuabandocu"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("com.cloudinary:kotlin-url-gen:1.7.0")
    implementation ("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.googleid)
    implementation (libs.ui)
    implementation (libs.ui.tooling.preview)
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.navigation.compose)
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("androidx.navigation:navigation-ui:2.8.9")
    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation ("androidx.compose.material3:material3:1.2.0")
    implementation ("androidx.navigation:navigation-compose:2.7.5")

    implementation (libs.firebase.database)// Add this line for Realtime Database
    implementation ("com.google.firebase:firebase-auth:21.1.0")
    implementation ("com.google.firebase:firebase-appcheck-playintegrity:17.1.2") // Or the latest version

    debugImplementation (libs.firebase.appcheck.debug)

    implementation (libs.firebase.database.ktx)

    implementation ("androidx.compose.material:material-icons-core:1.5.0")
    implementation ("androidx.compose.material:material-icons-extended:1.5.0")


    implementation("androidx.compose.material:material:1.8.2")

    //lấy api nè
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Firebase restore
    implementation(libs.google.firebase.firestore.ktx)

    implementation(libs.gms.play.services.auth) // Example for Auth
    implementation(libs.play.services.location)

    // Room Database - Để lưu trữ dữ liệu offline
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    // WorkManager - để đồng bộ dữ liệu khi có kết nối
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Gson - để chuyển đổi List<String> trong Room
    implementation("com.google.code.gson:gson:2.10.1")

    // Để sử dụng Canvas và các hiệu ứng đồ họa nâng cao
    implementation ("androidx.compose.foundation:foundation:1.8.2")

    // Để sử dụng các hiệu ứng animation
    implementation ("androidx.compose.animation:animation:1.8.2")
}