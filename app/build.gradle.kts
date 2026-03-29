plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.debt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.debt"
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "4.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", "debt vc$versionCode v$versionName")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas".toString(),
                    "room.incremental" to "true"
                )
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

        // coil
        implementation(libs.coil.compose)

        // datastore
        implementation(libs.androidx.datastore.preferences)
        implementation(libs.androidx.datastore)

        // api
        implementation(libs.retrofit)
        implementation(libs.logging.interceptor)

        // gson
        implementation(libs.gson)
        implementation(libs.converter.gson)

        // navigation
        implementation(libs.androidx.navigation.compose)

        // reflect
        implementation(libs.kotlin.reflect)


        // room
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        implementation(libs.retrofit2.kotlin.coroutines.adapter)
        kapt(libs.androidx.room.compiler)

        // security
        implementation(libs.androidx.security.crypto)

        // paging
        implementation(libs.androidx.paging.runtime)
        implementation(libs.androidx.paging.compose)

        // koin
        implementation(libs.koin.core)
        implementation(libs.koin.android)
        implementation(libs.koin.androidx.compose)

        implementation(libs.androidx.navigation.compose)
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
    }
}