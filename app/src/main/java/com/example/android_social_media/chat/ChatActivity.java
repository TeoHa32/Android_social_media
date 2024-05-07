package com.example.android_social_media.chat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;
import com.example.android_social_media.adapter.ChatAdapter;
import com.example.android_social_media.model.ChatModel;
import com.example.android_social_media.model.User;
import com.google.android.gms.common.util.AndroidUtilsLight;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    //String
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
//            messageMap.put("senderID",getIntent().getStringExtra("userid"));
//            Log.d("Người gửi là: ",getIntent().getStringExtra("userid"));

            if (getIntent() != null && getIntent().hasExtra("userid")) {
                messageMap.put("senderID",getIntent().getStringExtra("userid"));
                // Xử lý khi userId tồn tại trong Intent
            } else {
                messageMap.put("senderID", user.getUid());
                // Xử lý khi userId không tồn tại trong Intent
            }
//            messageMap.put("senderID",getIntent().getStringExtra("userid"));
            messageMap.put("time", currentTime); // Sử dụng thời gian đã được định dạng


            reference.child("message").child(messageID).setValue(messageMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                chatET.setText("");
                                sendNotification(message,"ha");
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
                    if (profileImage != null && !profileImage.isEmpty()) {
                        // Sử dụng Picasso để tải hình ảnh từ URL và hiển thị nó trong ImageView
                        //Picasso.get().load(url).into(img);
                        Picasso.get().load(profileImage).into(imageView);

                    } else {
                        // Nếu imageUrl là null hoặc rỗng, bạn có thể thực hiện các xử lý khác tùy thuộc vào yêu cầu của bạn
                        // Ví dụ: Hiển thị một hình ảnh mặc định hoặc hiển thị một tin nhắn lỗi
                        imageView.setImageResource(R.drawable.ic_profile);
                    }
                    // Load image using Picasso or any other library

                    String userName = snapshot.child("username").getValue(String.class);
                    name.setText(userName);
                }
//                return 0;
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
        Log.d("ChatID?", chatID);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Messages")
                .child(chatID)
                .child("message");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
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
                for ( ChatModel item : list) {
                    Log.d("list",item.getMessage());
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
//                return 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Không thể đọc được dữ liệu.", error.toException());
            }
        });

    }

   // thông báo
        void sendNotification(String message,String name) {
        String oppositeUID = getIntent().getStringExtra("uid");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(oppositeUID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User u = snapshot.getValue(User.class);
                    try{
                        JSONObject jsonObject = new JSONObject();
                        JSONObject notificationObject = new JSONObject();
                        notificationObject.put("title",name);
                        notificationObject.put("body",message);
                        JSONObject dataObject = new JSONObject();
                        dataObject.put("userId", user.getUid());
                        jsonObject.put("notification",notificationObject);
                        jsonObject.put("data",dataObject);
                        Log.d("TAG",getIntent().getStringExtra("uid"));
//                        jsonObject.put("to","c0cJxYMlTI2Cuy8sopKfsp:APA91bFUL4diRMuMbgHLtP2IODs9Za9P_IWwJJTKjG7eqB8ecQQxA4meA1Wm4DcFuYmnm_PtbI4qwO6b2H8nop2er3d_vqkS4bomeiTtk0og1rP21QTVqZMUyIeo_pFO17KxUGmf65ti");
                        jsonObject.put("to",u.getToken());
                        callApi(jsonObject);
                    }catch (Exception e){
                        System.out.println(e);
                    }

//                return 0;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("loadUser", "Không thể đọc được dữ liệu.", error.toException());
                // Handle error
            }
        });

    }
    void callApi(JSONObject jsonObject){
      MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer AAAAUim2Eso:APA91bGDcN23BvYdB7gr5KyePONZDmenUFi1qk8VidnQmc5ENhAnsWhBzEjqYxBFtZu0QgdPr7m9zCiP1Rh9pVMQaAP4AYvG4sEJ8V0fbrRAGCUcEec8QVCwUYiOmvb3-AJvoifv3qQq")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("Thất bại", "thất bại");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Đã nhận phản hồi thành công từ API
                    String responseBody = response.body().string();
                    Log.d("thành công", responseBody);
                } else {
                    // Gọi API không thành công

                }Log.d("thành công", "onResponse: không thành công");
            }
        });
    }

}