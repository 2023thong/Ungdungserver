plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "gmail.com.ass"
    compileSdk = 33

    defaultConfig {
        applicationId = "gmail.com.ass"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.squareup.retrofit2:retrofit:2.3.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.3.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:3.4.1")


    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))


    implementation("com.google.firebase:firebase-auth-ktx")


    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation ("com.android.volley:volley:1.2.1")

    implementation ("com.google.android.material:material:1.6.0-alpha03")
    implementation ("de.hdodenhof:circleimageview:3.1.0")












}