plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.longg.gky"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.longg.gky"
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // RecyclerView for product lists
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ViewPager2 for image sliders
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // CardView for product cards
    implementation("androidx.cardview:cardview:1.0.0")

    // Fragment navigation
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // Room (persistence)
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    // Lifecycle (ViewModel / LiveData) - optional but useful
    implementation("androidx.lifecycle:lifecycle-runtime:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.1")

    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
