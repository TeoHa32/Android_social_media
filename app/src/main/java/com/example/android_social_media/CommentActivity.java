package com.example.android_social_media;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.android_social_media.adapter.commentAdapter;
import com.example.android_social_media.model.User;
import com.example.android_social_media.model.comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private commentAdapter commentAdapter;
    private List<comment> commentList;
    EditText addComment;
    ImageView imageView_profile;
    TextView post;
    String postId;
    String publisherId;
    FirebaseUser firebaseUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Log.d("TAG1", "onCreate: 1");
        Toolbar toolbar = findViewById(R.id.toolbar_comment);
        Log.d("TAG2", "onCreate: 2");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addComment = findViewById(R.id.text_comment);
        imageView_profile = findViewById(R.id.img_profile_comment);
        addComment = findViewById(R.id.text_comment);
        post = findViewById(R.id.post_comment);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        postId = intent.getStringExtra("postId");
        publisherId = intent.getStringExtra("publisherId");
        recyclerView = findViewById(R.id.recyclerView_comment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<comment>();
        commentAdapter = new commentAdapter(this , commentList);
        recyclerView.setAdapter(commentAdapter);
        readComment();
        Log.d("publisher", intent.getStringExtra("publisherId"));
        String publisherId = intent.getStringExtra("publisherId");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addComment.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this, "Ban chưa nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
                }
                else{
                    addComment();
                    if(publisherId != null && !publisherId.isEmpty()){
                        Log.d("có vô", "onClick: ");
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child(publisherId);
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    User u = snapshot.getValue(User.class);
                                    Log.d("tokencomment", u.getToken());
                                    sendNotification(u.getToken());
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("loadUser", "Không thể đọc được dữ liệu.", error.toException());
                                // Handle error
                            }
                        });
                    }
                }
            }
        });
        getImage();
    }
    private void addComment(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postId);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("comment", addComment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());
        reference.push().setValue(hashMap);
        addNotifications();


        addComment.setText("");
    }

    private void addNotifications(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(publisherId);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (!publisherId.equals(firebaseUser.getUid())) {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userId", firebaseUser.getUid());
            hashMap.put("text", " bình luận bài viết của bạn: " + addComment.getText().toString());
            hashMap.put("postId", postId);
            hashMap.put("isPost", "true");

            reference.push().setValue(hashMap);
        }
    }


    private void getImage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getProfileImage()).into(imageView_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readComment(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                int i=0;
                for(DataSnapshot data : snapshot.getChildren()){
                    comment comment = data.getValue(comment.class);
                    commentList.add(comment);
                    Log.d("TAGco1m", "onDataChange: "+commentAdapter.getItemCount());

                }
                commentAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    void sendNotification(String token) {
        Log.d("TAGcomment", token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User u = snapshot.getValue(User.class);
                    try{
                        JSONObject jsonObject = new JSONObject();
                        JSONObject notificationObject = new JSONObject();
                        notificationObject.put("title","Thông báo");
                        notificationObject.put("body",u.getName()+" đã comment vào bài viết của bạn");
                        JSONObject dataObject = new JSONObject();
                        dataObject.put("userId",firebaseUser.getUid());
                        jsonObject.put("notification",notificationObject);
                        jsonObject.put("data",dataObject);

//                        jsonObject.put("to","c0cJxYMlTI2Cuy8sopKfsp:APA91bFUL4diRMuMbgHLtP2IODs9Za9P_IWwJJTKjG7eqB8ecQQxA4meA1Wm4DcFuYmnm_PtbI4qwO6b2H8nop2er3d_vqkS4bomeiTtk0og1rP21QTVqZMUyIeo_pFO17KxUGmf65ti");
                        jsonObject.put("to",token);
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
                    Log.d("thành công 1", responseBody);
                } else {
                    // Gọi API không thành công

                }Log.d("thành công", "onResponse: không thành công");
            }
        });
    }

}