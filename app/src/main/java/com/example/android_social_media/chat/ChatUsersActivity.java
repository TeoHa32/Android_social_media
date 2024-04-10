package com.example.android_social_media.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;
import com.example.android_social_media.adapter.chatuserAdapter;
import com.example.android_social_media.model.chatUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatUsersActivity extends AppCompatActivity {

    chatuserAdapter adapter;
    List<chatUserModel> list;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users);
        init();
        fetchUserData();
        clickListener();
    }

    void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new chatuserAdapter(this, list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    void fetchUserData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.d("Current Account: ", "User is null");
            return;
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("Messages");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userID = snapshot.child("UserID").getValue(String.class);
                    if (userID != null && userID.equals(user.getUid())) {
                        String name = snapshot.child("name").getValue(String.class);
                        String profileImage = snapshot.child("profileImage").getValue(String.class);
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        chatUserModel userModel = new chatUserModel();
                        userModel.setName(name);
                        userModel.setProfileIamge(profileImage);
                        list.add(userModel);

                        // Cập nhật adapter
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("Tên người dùng: ", "Không có tên");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatUsersActivity", "Failed to read user data.", error.toException());
            }
        });
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Xử lý dữ liệu từ node "Message" ở đây
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    chatUserModel model = dataSnapshot.getValue(chatUserModel.class);
                    if (model != null) {
                        list.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatUsersActivity", "Failed to read message data.", error.toException());
            }
        });
    }

    void clickListener(){
        adapter.OnStartChat(new chatuserAdapter.OnStartChat() {
            @Override
            public void clicked(int position, List<String> uids, String chatID) {
                String oppositeUID;
                if(uids.get(0).equalsIgnoreCase(user.getUid())){
                    oppositeUID = uids.get(0);
                }
                else{
                    oppositeUID = uids.get(1);
                }

                Intent intent = new Intent(ChatUsersActivity.this, ChatActivity.class);
                intent.putExtra("uid", oppositeUID);
                intent.putExtra("id", chatID);
                startActivity(intent);
            }

        });
    }
}