import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // parcelize
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

// 1. Properties 객체 생성 방식
val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.example.movieismylife"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.movieismylife"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "API_KEY", getApiKey("api_key"))
        buildConfigField("String", "MAPS_API_KEY",getApiKey("MAPS_API_KEY"))
        resValue ("string", "maps_api_key", "${project.findProperty("MAPS_API_KEY".toString()) ?: "fail"}")

        multiDexEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.appcompat:appcompat-resources:1.4.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Retrofit2
//    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.0.0")
//    implementation("com.squareup.okhttp3:okhttp:4.10.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //RxJava
    implementation("io.reactivex.rxjava2:rxjava:2.2.3")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.8.2")

    //Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")

    //implementation(libs.androidx.runtime.livedata)
    implementation("androidx.compose.runtime:runtime-livedata:1.5.0")

    // coil(For image load)
    implementation("io.coil-kt:coil-compose:2.2.2")

    // icon
    implementation("androidx.compose.material:material:1.5.0")
    implementation("androidx.compose.material3:material3:1.0.0")

    implementation("com.google.accompanist:accompanist-navigation-animation:0.36.0")

    //Google Map Api
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.maps.android:maps-compose:2.11.4")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation(libs.places)

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Youtube Android Player API
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.1")
}

configurations.all {
    resolutionStrategy {
        force("androidx.appcompat:appcompat:1.4.2")
        force("androidx.appcompat:appcompat-resources:1.4.2")
    }
}