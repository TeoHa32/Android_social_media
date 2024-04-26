package com.example.android_social_media;

import android.os.Bundle;

import android.provider.Contacts;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android_social_media.fragment.profile;
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

//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        String currentUserID = currentUser != null ? currentUser.getUid() : null;
//        DatabaseReference messageRef = rootRef.child("Message").push();
//        String messageID = messageRef.getKey(); //
//
//
//        // Tạo một hashmap để đại diện cho dữ liệu tin nhắn
//        HashMap<String, Object> messageData = new HashMap<>();
//        messageData.put("id", messageID);
//        messageData.put("message", "Test");
//        messageData.put("time", "02:40:00");
//        messageData.put("senderID", "ghuiijko");
//
//        // Tạo một hashmap để đại diện cho dữ liệu của user
//        HashMap<String, Object> userData = new HashMap<>();
//        userData.put("id", currentUserID);
//        userData.put("uid", Arrays.asList("asdf", "sdfgh"));
//        userData.put("time", "02:50:00");
//        userData.put("lastMessage", "final");
//        userData.put("message", messageData);
//
//        // Đẩy dữ liệu của user lên Firebase Realtime Database
//        rootRef.child("Messages").child(messageID).setValue(userData);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference storyRef = database.getReference("Follow");
//
//        List<String> followingList = new ArrayList<>();
//        followingList.add("4X1AusHwrVQQf9yLSUixMCZnLhY2");
//        followingList.add("PVz33IummMfEXX6594CKN0nCJhC3");
////
////// Đẩy dữ liệu của user lên Firebase Realtime Database với key tự động// Tạo key tự động cho user
//        storyRef.child("vYEYezjxu8QT5HgpCRgcxuEOsUs1").child("following").setValue(followingList);




        //Khởi tạo ban đầu sẽ ở trang đăng nhập.
        //LoginFragment loginFragment = new LoginFragment();
        profile p = new profile();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.fragment_container, loginFragment);
        fragmentTransaction.replace(R.id.fragment_container, p);
        fragmentTransaction.addToBackStack(null); // Add to back stack if needed
        fragmentTransaction.commit();

//       FirebaseDatabase database = FirebaseDatabase.getInstance();
//       DatabaseReference usersRef = database.getReference("users").child("PVz33IummMfEXX6594CKN0nCJhC3");

       //String userId = usersRef.push().getKey(); // Tạo một key ngẫu nhiên cho user

        // Thêm user vào Firebase Realtime Database
//        usersRef.child("UserID").setValue("jWuNI400Q4eV8oy8DuKJuIyFaf53");
//        usersRef.child("dob").setValue("01/01/2000");
//        usersRef.child("email").setValue("nguyenha1234592@gmail.com");
//        usersRef.child("gender").setValue("nu");
//        usersRef.child("name").setValue("Nguyễn Thị Thu Hà");
//        usersRef.child("password").setValue("123456");
//        usersRef.child("phoneNumber").setValue("0987654321");
//        usersRef.child("profileImage").setValue("https://lh3.googleusercontent.com/a/ACg8ocIEA_G0FuFflvPyzWN0vfUnVubXNftoNsh3LD6A_Heyfjo=s96-c");
//        usersRef.child("status").setValue("say hi");
//        usersRef.child("username").setValue("thuha");
//
//        // Thêm followers
//        usersRef.child("follower").child("0").setValue("jukhx1A154W2ERD0nJ9AG3LtyMI2");
//        usersRef.child("follower").child("1").setValue("4X1AusHwrVQQf9yLSUixMCZnLhY2");
 //           usersRef.child("following").child("0").setValue("jukhx1A154W2ERD0nJ9AG3LtyMI2");

//        profile pro = new profile();
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, pro);
//        fragmentTransaction.addToBackStack(null); // Add to back stack if needed
//        fragmentTransaction.commit();

        //đổi màu
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
    }

}