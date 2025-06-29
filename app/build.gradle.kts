plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    namespace = "com.kenttravis.pantrypal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kenttravis.pantrypal"
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
    buildFeatures{
        viewBinding=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // testing suspend function
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    // untuk membuat stub
    testImplementation("io.mockk:mockk:1.14.2")
    // testing multithread
    testImplementation("androidx.arch.core:core-testing:2.1.0")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    kapt("androidx.room:room-compiler:2.6.1")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.2")

    //scan barcode
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")

    //coil
    implementation("io.coil-kt:coil:2.4.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    //material 3
    implementation("com.google.android.material:material:1.10.0")

    // ===== COMPOSE DEPENDENCIES =====
    // Compose BOM - manages all compose library versions
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    // Core Compose libraries
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.8.2")

    // Navigation Compose (if you want to use Compose navigation)
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // ViewModel Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Optional: Compose testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Optional: Icons extended (if you need more icons)
    implementation("androidx.compose.material:material-icons-extended")

    // Optional: Coil for Compose (image loading)
    implementation("io.coil-kt:coil-compose:2.4.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}