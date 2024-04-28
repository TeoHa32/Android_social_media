
plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.android_social_media"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.android_social_media"
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

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.firebase:firebase-firestore:24.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(files("libs/activation.jar"))
    implementation(files("libs/additionnal.jar"))
    implementation(files("libs/mail.jar"))
    implementation(files("libs/mail-5.1.2.jar"))
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    implementation ("pub.devrel:easypermissions:3.0.0")
    implementation ("com.droidninja:filepicker:2.2.5")
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.3")

    implementation ("com.google.android.material:material:1.4.0")


    implementation ("com.karumi:dexter:6.2.2")

    //Thư viện dùng làm tính năng story
    implementation("com.github.shts:StoriesProgressView:3.0.0")

    //thư viện dùng để cắt ảnh đăng story
    implementation("com.theartofdev.edmodo:android-image-cropper:2.7.+")

    //post
    implementation ("com.github.bumptech.glide:glide:4.16.0")


}
