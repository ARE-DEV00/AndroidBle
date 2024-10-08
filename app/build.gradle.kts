plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

val versionMajor = 1 // 0~9
val versionMinor = 0 // 0~99
val versionPatch = 0 // 0~99
val versionHotfix = 0 // 0~99

val versionCodeFinal =
    versionMajor * 10_000_000 + versionMinor * 100_000 + versionPatch * 1000 + versionHotfix
val versionNameFinal = "$versionMajor.$versionMinor.$versionPatch"


android {
    namespace = "kr.co.are.androidble"
    compileSdk = 34

    defaultConfig {
        applicationId = "kr.co.are.androidble"
        minSdk = 31
        targetSdk = 34
        versionCode = versionCodeFinal
        versionName = versionNameFinal

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)


    //Navigation
    implementation(libs.navigation.compose)

    //Navigation
    implementation(libs.navigation.compose)

    //Logger
    implementation(libs.timber)

    //Zxing
    implementation(libs.camerax.core)
    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.view)

    implementation(libs.zxing.core)
    implementation(libs.zxing.android.embedded)

    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    //Preferences
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.preferences.core)

    //Moshi
    implementation(libs.moshi.kotlin)

    //accompanist-swiperefresh
    implementation(libs.accompanist.swiperefresh)

    //chart-vico
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m2)
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.core)
    implementation(libs.vico.views)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}