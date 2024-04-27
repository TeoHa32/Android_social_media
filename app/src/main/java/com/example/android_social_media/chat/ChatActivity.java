package com.example.android_social_media.chat;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;
import com.example.android_social_media.adapter.ChatAdapter;
import com.example.android_social_media.model.ChatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    FirebaseUser user;
    CircleImageView imageView;
    TextView name, status;
    EditText chatET;
    ImageView sendBtn, btnBack;
    RecyclerView recyclerView;

    ChatAdapter adapter;
    List<ChatModel> list;

    String chatID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        loadUserData();
        loadMessages();

        sendBtn.setOnClickListener(v -> {
            list.clear();
            String message = chatET.getText().toString().trim();
            if (message.isEmpty()) {
                return;
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages").child(chatID);
            String messageID = reference.push().getKey(); // Tạo một key duy nhất cho mỗi tin nhắn

            // Tạo đối tượng SimpleDateFormat với định dạng mong muốn
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            // Lấy thời gian hiện tại
            String currentTime = sdf.format(new Date());

            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("id", messageID);
            messageMap.put("message", message);
            messageMap.put("senderID", user.getUid());
            messageMap.put("time", currentTime); // Sử dụng thời gian đã được định dạng


            reference.child("message").child(messageID).setValue(messageMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                chatET.setText("");
                            } else {
                                Toast.makeText(ChatActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    void init(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        imageView = findViewById(R.id.profileImage);
        name = findViewById(R.id.txtName);
        chatET = findViewById(R.id.chatET);
        sendBtn = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);

        recyclerView = findViewById(R.id.recyclerViewChat);

        list = new ArrayList<>();

        adapter = new ChatAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

    }

    void loadUserData() {

        String oppositeUID = getIntent().getStringExtra("uid");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(oppositeUID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String profileImage = snapshot.child("profileImage").getValue(String.class);
                    Log.d("ảnh: ", profileImage);

                    // Load image using Picasso or any other library
                    Picasso.get().load(profileImage).into(imageView);

                    String userName = snapshot.child("username").getValue(String.class);
                    name.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("loadUser", "Không thể đọc được dữ liệu.", error.toException());
                // Handle error
            }
        });
    }

    void loadMessages(){
        chatID = getIntent().getStringExtra("id");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Messages")
                .child(chatID)
                .child("message");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatModel lastChat = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String messageId = dataSnapshot.child("id").getValue(String.class);
                    String message = dataSnapshot.child("message").getValue(String.class);
                    String senderID = dataSnapshot.child("senderID").getValue(String.class);
                    String timeString = dataSnapshot.child("time").getValue(String.class);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date time = dateFormat.parse(timeString);

                        ChatModel model = new ChatModel(messageId, message, time, senderID);
                        list.add(model);

                        // So sánh thời gian của tin nhắn với thời gian của lastMessage hiện tại
                        if (lastChat == null || time.after(lastChat.getTime())) {
                            lastChat = model;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // Cập nhật lastMessage mới trong cơ sở dữ liệu Firebase
                if (lastChat != null) {
                    DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(chatID);
                    chatRef.child("lastMessage").setValue(lastChat.getMessage());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String currentTimeISO8601 = sdf.format(lastChat.getTime());

                    chatRef.child("time").setValue(currentTimeISO8601);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Không thể đọc được dữ liệu.", error.toException());
            }
        });

    }

}