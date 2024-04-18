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

        // Khởi tạo biến user
        user = FirebaseAuth.getInstance().getCurrentUser();

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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        DataSnapshot uidSnapshot = userSnapshot.child("uid");
                        for (DataSnapshot uidChildSnapshot : uidSnapshot.getChildren()) {
                            // Truy cập vào giá trị của uidChildSnapshot và chuyển đổi sang chuỗi
                            String uid = uidChildSnapshot.getValue().toString();
                            Log.d("UID?", uid); // Log uid ra
                            if (uid != null && uid.equals(user.getUid())) {
                                // Nếu user có trong mảng uid của nút hiện tại
                                chatUserModel model = userSnapshot.getValue(chatUserModel.class);
                                list.add(model);
                                break;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("fetchUserData", "No data found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("fetchUserData", "Listen failed", databaseError.toException());
            }
        });
    }

    void clickListener(){
        adapter.OnStartChat(new chatuserAdapter.OnStartChat() {
            @Override
            public void clicked(int position, List<String> uids, String chatID) {
                String oppositeUID = null;
                if (uids != null && uids.size() >= 2) { // Kiểm tra xem danh sách có ít nhất 2 phần tử hay không
                    if (uids.get(0).equalsIgnoreCase(user.getUid())) {
                        oppositeUID = uids.get(1);
                    } else {
                        oppositeUID = uids.get(0);
                    }
                } else {
                    Log.e("clickListener", "Danh sách UID không hợp lệ!");
                }

                if (oppositeUID != null) {
                    Intent intent = new Intent(ChatUsersActivity.this, ChatActivity.class);
                    intent.putExtra("uid", oppositeUID);
                    intent.putExtra("id", chatID);
                    startActivity(intent);
                } else {
                    Log.e("clickListener", "Không thể lấy được oppositeUID!");
                }
            }
        });
    }

}