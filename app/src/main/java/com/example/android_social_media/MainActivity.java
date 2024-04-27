package com.example.android_social_media;

import android.os.Bundle;
import android.provider.Contacts;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.fragment.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Write a message to the database
//
//        // Lấy ID của người dùng hiện tại
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference storyRef = database.getReference("users");
//
//        List<String> followingList = new ArrayList<>();
//        followingList.add("4X1AusHwrVQQf9yLSUixMCZnLhY2");
//        followingList.add("PVz33IummMfEXX6594CKN0nCJhC3");
////
////// Đẩy dữ liệu của user lên Firebase Realtime Database với key tự động// Tạo key tự động cho user
//        storyRef.child("vYEYezjxu8QT5HgpCRgcxuEOsUs1").child("following").setValue(followingList);




        //Khởi tạo ban đầu sẽ ở trang đăng nhập.
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, loginFragment);
        fragmentTransaction.addToBackStack(null); // Add to back stack if needed
        fragmentTransaction.commit();
        //đổi màu
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
    }

}