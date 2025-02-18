plugins {
    alias(libs.plugins.android.application)
    id("jacoco") // Abilita il plugin JaCoCo per la copertura del codice
}

android {
    namespace = "com.example.lupusincampus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.lupusincampus"
        minSdk = 26
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.jbcrypt)
    implementation(libs.sdk)
    implementation(libs.jackson.databind)
    // Test unitari con JUnit 4
    testImplementation(libs.junit)
    // Mockito per creare mock e stub
    testImplementation(libs.mockito.core)
    // Librerie per websocket over stomp communication
    implementation(libs.stompprotocolandroid)
    implementation (libs.rxjava)
    implementation (libs.rxandroid)
}

// Configurazione di JaCoCo per la copertura del codice
// ✅ Corretta configurazione di JaCoCo
tasks.withType<Test>().configureEach {
    finalizedBy("jacocoTestReport") // Assicura che il report venga generato dopo i test
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest") // Esegue prima i test unitari

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}