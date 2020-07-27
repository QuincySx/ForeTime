object Versions {
    val compileSdk = 29
    val minSdk = 23
    val targetSdk = 29
    val androidx = "1.0.0"
    val androidxCollection = "1.1.0"
    val androidxCoreRuntime = "2.1.0-rc01"
    val coreKtx = "1.5.0-alpha01"
    val androidxLifecycle = "2.2.0"
    val appcompat = "1.2.0-rc02"
    val fragment = "1.3.0-alpha07"
    val recyclerview = "1.1.0"
    val cardview = "1.0.0"
    val constraintLayout = "1.1.3"
    val material = "1.1.0-alpha08"
    val work = "2.4.0"
    val sqlite = "2.1.0"
    val dagger = "2.23.2"
    val extJunit = "1.1.0"
    val glide = "4.9.0"
    val glideTransformations = "4.0.1"
    val gson = "2.8.6"
    val kotlin = "1.3.72"
    val kotlinxCoroutinesCore = "1.3.8"
    val ktlint = "0.24.0"
    val okhttp = "3.10.0"
    val retrofit = "2.6.0"
    val eventbus = "3.1.1"
    val room = "2.3.0-alpha02"
    val autoSize = "1.1.2"
    val statusBarCompat = "2.3.3"
    val supportLibrary = "28.0.0"
    val test_junit = "4.13"
    val test_espresso = "3.2.0"
    val test_runner = "1.2.0"
}

object Names {
    val applicationId = "com.smallraw.foretime.app"
    val versionName = "1.0.0"
}

object Deps {
    val test_junit = "junit:junit:${Versions.test_junit}"
    val test_runner = "androidx.test:runner:${Versions.test_runner}"
    val test_espresso = "androidx.test.espresso:espresso-core:${Versions.test_espresso}"

    // jetpack see https://developer.android.google.cn/jetpack
    // see AndroidX version update https://developer.android.google.cn/jetpack/androidx/releases
    val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val material = "com.google.android.material:material:${Versions.material}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val cardview = "androidx.cardview:cardview:${Versions.cardview}"
    val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"

    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofit_rxjava = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
}