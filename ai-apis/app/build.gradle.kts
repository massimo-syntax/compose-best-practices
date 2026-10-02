import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // firebase ai logic
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.aiapis"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.aiapis"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

//    2 files found with path 'META-INF/INDEX.LIST' from inputs:
//    - com.google.auth:google-auth-library-oauth2-http:1.33.0/google-auth-library-oauth2-http-1.33.0.jar
//    - com.google.auth:google-auth-library-credentials:1.33.0/google-auth-library-credentials-1.33.0.jar
//    Adding a packaging block may help, please refer to
    packaging{
        resources.excludes.add("META-INF/*")
    }


}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // ML KIT GENAI-PROMPT
    // not supported for this device, very often
    implementation("com.google.mlkit:genai-prompt:1.0.0-beta4")

    // OPEN AI SDK
    // import Kotlin API client BOM
    implementation(platform("com.aallam.openai:openai-client-bom:4.1.0"))
    implementation("com.aallam.openai:openai-client")
    //runtimeOnly("io.ktor:ktor-client-okhttp")

    // OPEN AI CLIENT SDK - USE ALSO OTHER URLS
    // error: OpenAi.create(key: url:) not found
    // implementation("com.tddworks:openai-client-jvm:0.2.3")

    // GEN AI - REMOTE API
    implementation("com.google.genai:google-genai-kotlin:1.3.0")


    // FIREBASE AI - THE ONLY ONE WORKING
    // !! requires plugin!!
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:34.19.0"))

    // Add the dependencies for the Firebase AI Logic and App Check libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-ai")
    implementation("com.google.firebase:firebase-appcheck-debug")


}