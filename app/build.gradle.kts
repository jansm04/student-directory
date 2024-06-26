plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.jan.studentdirectory"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jan.studentdirectory"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("com.squareup.okhttp3:logging-interceptor:3.8.0")
    implementation("com.squareup.okhttp3:okhttp:3.8.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}