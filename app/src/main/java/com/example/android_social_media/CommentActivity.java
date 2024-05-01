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

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addComment.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this, "Ban chưa nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
                }
                else{
                    addComment();
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
        addComment.setText("");
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
}