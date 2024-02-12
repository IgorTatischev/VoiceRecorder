plugins {
    id(Dependencies.Plugins.android_application)
    id(Dependencies.Plugins.kotlin_android)
    id(Dependencies.Plugins.kapt)
}

android {
    namespace = Dependencies.Application.appId
    compileSdk = Dependencies.Versions.compileSDK

    defaultConfig {
        applicationId = Dependencies.Application.appId
        minSdk = Dependencies.Versions.minsdk
        targetSdk = Dependencies.Versions.targetsdk
        versionCode = Dependencies.Application.version_code
        versionName = Dependencies.Application.version_name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = Dependencies.Application.releaseMinify
            isShrinkResources = Dependencies.Application.releaseMinify

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            isMinifyEnabled = Dependencies.Application.debugMinify
            isShrinkResources = Dependencies.Application.debugMinify

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Dependencies.Java.java_compile
        targetCompatibility = Dependencies.Java.java_compile
    }
    kotlinOptions {
        jvmTarget = Dependencies.Java.java_versions
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.kotlinCompiler
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(Dependencies.Compose.composeBoom))
    implementation(Dependencies.Compose.toolingPreview)
    implementation(Dependencies.Dependencies.android_core_ktx)
    implementation(Dependencies.Dependencies.lifecycle_runtime)
    implementation(Dependencies.Dependencies.material)
    implementation(Dependencies.Compose.activityCompose)
    implementation(Dependencies.Compose.composeUi)
    implementation(Dependencies.Compose.composeUiGraphics)
    implementation(Dependencies.Compose.composeFoundation)
    implementation(Dependencies.Compose.material3)
    implementation(Dependencies.Compose.compose_permissions)
    implementation(Dependencies.Dependencies.window)
    implementation(Dependencies.Compose.fullSetIconsCompose)

    //koin
    implementation(Dependencies.Dependencies.koin_android)
    implementation(Dependencies.Dependencies.koin_compose)
}