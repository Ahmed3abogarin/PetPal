import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)

    // should use KSP instead of Kapt
//    id("kotlin-kapt")

    // Hilt
    alias(libs.plugins.hilt)

    // Firebase
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.vtol.petpal"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.vtol.petpal"
        minSdk = 26
        targetSdk = 36
        versionCode = 10
        versionName = "1.4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        val mapsApiKey = project.findProperty("mapsApiKey") as String?
            ?: throw GradleException("mapsApiKey is missing in gradle.properties")

        buildConfigField(
            "String",
            "MAPS_API_KEY",
            "\"$mapsApiKey\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

//androidComponents {
//    onVariants { variant ->
//        variant.outputs.forEach { output ->
//            val versionName = variant.versionName.orNull ?: "unknown"
//            val versionCode = variant.versionCode.get()
//
//            output.outputFileName.set(
//                "PetPal-v${versionName}-code${versionCode}.aab"
//            )
//        }
//    }
//}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // bom => Bill of Materials
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // navigation
    implementation(libs.androidx.navigation.compose)

    // Coil
    implementation(libs.coil.compose)

    // Google maps dependencies
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.places)


    // icons
    implementation(libs.androidx.compose.material.icons.extended)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)


    // Vico library - chart library
    implementation(libs.compose.m3)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Calendar
    implementation(libs.kizitonwose)


    // firebase auth
    implementation(libs.firebase.auth.ktx)


    // data store
    implementation(libs.datastore.preferences)

    // Timber for logging
    implementation(libs.timber)


    // Lottie animation
    implementation(libs.lottie.compose)


    // Facebook SDK
    implementation(libs.facebook.android.sdk)

    // Image compression library
    implementation(libs.compressor)

    // Image Crop library
    implementation("com.github.yalantis:ucrop:2.2.11")

    // Work Manager
    implementation("androidx.work:work-runtime-ktx:2.11.2")


}