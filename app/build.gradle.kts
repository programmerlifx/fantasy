plugins { id("com.android.application") }

android {
    namespace = "com.pariaman.fantasy"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.pariaman.fantasy"
        minSdk = 23
        targetSdk = 28
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
