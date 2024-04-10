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
        //-------------------------------------
//        rcv = findViewById(R.id.rcv_id);
//        userAdapter = new UserAdapter(this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
//        rcv.setLayoutManager(linearLayoutManager);
//        userAdapter.setData(getListUser());
//        rcv.setAdapter(userAdapter);

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