package com.example.android_social_media;

import android.os.Bundle;
<<<<<<< HEAD
=======
import android.provider.Contacts;
import android.widget.Toast;
>>>>>>> a8eda07d6cb67d8a57616bca177c05e4d2c4c047

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
<<<<<<< HEAD
import com.example.android_social_media.model.User;
import com.example.android_social_media.adapter.UserAdapter;
import com.example.android_social_media.model.post;
import com.example.android_social_media.adapter.postAdapter;
import com.example.android_social_media.fragment.homepageFragment;
import java.util.ArrayList;
=======

import com.example.android_social_media.fragment.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
>>>>>>> a8eda07d6cb67d8a57616bca177c05e4d2c4c047
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homepageFragment fragment = new homepageFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        //-------------------------------------
        // Write a message to the database
<<<<<<< HEAD
//        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        String key = "Messages";
//        Map<String, Object> updates = new HashMap<>();
//        updates.put("ID", id);
//        updates.put("message", "Test Message");
//        // Get the current time
//        LocalTime currentTime = LocalTime.now();
=======
>>>>>>> a8eda07d6cb67d8a57616bca177c05e4d2c4c047
//
//        // Lấy ID của người dùng hiện tại
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        String currentUserID = currentUser != null ? currentUser.getUid() : null;
//        DatabaseReference messageRef = rootRef.child("Message").push();
//        String messageID = messageRef.getKey(); //
//
//
<<<<<<< HEAD
//        db.child(key).child(id).setValue(updates, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                if(error != null){
//                    Toast.makeText(getApplicationContext(),"Không update được dữ liệu!", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//

        //Khởi tạo ban đầu sẽ ở trang đăng nhập.
//        LoginFragment loginFragment = new LoginFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, loginFragment);
//        fragmentTransaction.addToBackStack(null); // Add to back stack if needed
//        fragmentTransaction.commit();
//        //đổi màu
//        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
    }
=======
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

>>>>>>> a8eda07d6cb67d8a57616bca177c05e4d2c4c047
}