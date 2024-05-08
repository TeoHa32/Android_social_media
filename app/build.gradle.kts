plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
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
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    //Thư viện dùng để tạo giao diện
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.jakewharton.threetenabp:threetenabp:1.3.1")
    implementation("androidx.appcompat:appcompat:1.6.1")

    //Các thư viện để sử dụng dịch vụ trên firebase

    //Thư viện dùng để sử dụng database trên firebase
    implementation("com.google.firebase:firebase-database:20.3.1")
    //Thư viện dùng để sử dụng authentication trên firebase
    implementation("com.google.firebase:firebase-auth")
    //Thư viện dùng để xác thực đăng nhập bằng google
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    //Thư viện dùng để lưu trữ ảnh trên firebase
    implementation("com.google.firebase:firebase-storage:20.3.0")
    //Thư viện dùng để tạo thông báo
    implementation("com.google.firebase:firebase-messaging:23.4.1")


    //thư viện xử lý ảnh
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //Thư viện xử lý chức năng story
    //Thư viện dùng làm tính năng story
    implementation("com.github.shts:StoriesProgressView:3.0.0")
    //thư viện dùng để cắt ảnh đăng story
    implementation("com.theartofdev.edmodo:android-image-cropper:2.7.+")

    //Thư viện dùng để tạo thông báo
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Thư viện xử lý mail cho chức năng quên mật khẩu
    implementation(files("libs/activation.jar"))
    implementation(files("libs/additionnal.jar"))
    implementation(files("libs/mail.jar"))
    implementation(files("libs/mail-5.1.2.jar"))

    //thư viện hỗ trợ quản lý quyền truy cập trong ứng dụng Android một cách dễ dàng
    implementation ("pub.devrel:easypermissions:3.0.0")

    // thư viện được sử dụng để chọn và tải tệp trong ứng dụng Android.
    implementation ("com.droidninja:filepicker:2.2.5")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.3")

    implementation ("com.karumi:dexter:6.2.2")

    //Thư viện dùng làm tính năng story
    implementation("com.github.shts:StoriesProgressView:3.0.0")

    //thư viện dùng để cắt ảnh đăng story
    implementation("com.theartofdev.edmodo:android-image-cropper:2.7.+")

    //thư viện xử lý ảnh
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //thư viện đùng để call api
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}
