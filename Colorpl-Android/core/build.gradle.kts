import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.gradle.plugin)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.google.services)
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

android {
    namespace = "com.colorpl"
    compileSdk = 34
    val naverMapClientId = getApiKey("NAVER_MAP_CLIENT_ID")
    defaultConfig {
        applicationId = "com.colorpl"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "NAVER_MAP_CLIENT_ID", naverMapClientId)
        buildConfigField("String", "TMAP_APP_KEY", getApiKey("TMAP_APP_KEY"))
        buildConfigField("String", "BASE_URL", getApiKey("BASE_URL"))
        buildConfigField("String", "BOOT_PAY_KEY", getApiKey("BOOT_PAY_KEY"))
        buildConfigField("String", "OPEN_API_KEY", getApiKey("OPEN_API_KEY"))
        buildConfigField("String", "BASE_URL", getApiKey("BASE_URL"))
        buildConfigField("String", "DATASTORE_NAME", getApiKey("DATASTORE_NAME"))
        manifestPlaceholders["NAVER_MAP_CLIENT_ID"] = naverMapClientId
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
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    // module
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.work.runtime.ktx)

    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.work)


    // Firebase
    implementation(libs.firebase.cloud.messaging)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.kotlinxSerialization)
    implementation(libs.okhttp)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Maps
    implementation(libs.play.services.location)
    implementation(libs.naver.maps)

    // Log
    implementation(libs.timber)

    // BootPay
    implementation(libs.bootpay)

    // DataStore
    implementation(libs.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)
}