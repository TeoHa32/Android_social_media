package com.example.android_social_media;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.android_social_media.fragment.LoginFragment;
import com.example.android_social_media.fragment.profile;
import com.example.android_social_media.model.StoriesModel;
import com.example.android_social_media.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    int counter = 0;
    long pressTime = 0L;
    long limit = 500L;

    StoriesProgressView storiesProgressView;
    ImageView image, story_photo;
    TextView story_username;

    LinearLayout r_seen;
    TextView seen_number;
    ImageView story_delete;
    List<String> images;
    List<String> storyids;
    String userid;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story);

        storiesProgressView = findViewById(R.id.stories);
        image = findViewById(R.id.image);
        story_photo = findViewById(R.id.story_photo);
        story_username = findViewById(R.id.story_username);

        r_seen = findViewById(R.id.r_seen);
        seen_number = findViewById(R.id.seen_number);
        story_delete = findViewById(R.id.story_delete);

        r_seen.setVisibility(View.GONE);
        story_delete.setVisibility(View.GONE);

        userid = getIntent().getStringExtra("userid");

        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            r_seen.setVisibility(View.VISIBLE);
            story_delete.setVisibility(View.VISIBLE);
        }

        getStStories(userid);
        userInfo(userid);

        View reverse = findViewById(R.id.reverse);

        //Tua lại story
        reverse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        //Bỏ qua story
        View skip = findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);

        r_seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Nhấn vào icon xem tin sẽ xem được thông tin người nào đã xem
                Intent intent = new Intent(StoryActivity.this, FollowerActivity.class);
                intent.putExtra("id", userid);
                intent.putExtra("storyid", storyids.get(counter));
                intent.putExtra("title", "Lượt xem");
                startActivity(intent);

            }
        });

        story_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
                        .child(userid).child(storyids.get(counter));
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(StoryActivity.this, "Tin đã được xóa!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onNext() {
        Glide.with(getApplicationContext()).load(images.get(++counter)).into(image);

        addView(storyids.get(counter));
        seenNumber(storyids.get(counter));
    }

    @Override
    public void onPrev() {
        if((counter - 1) < 0) return;
        Glide.with(getApplicationContext()).load(images.get(--counter)).into(image);

        seenNumber(storyids.get(counter));
    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        storiesProgressView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        storiesProgressView.resume();
        super.onResume();
    }

    private  void getStStories(String userid){
        images = new ArrayList<>();
        storyids = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                images.clear();
                storyids.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    StoriesModel story = data.getValue(StoriesModel.class);
                    long timeCurrent = System.currentTimeMillis();
                    if(timeCurrent > story.getStartTime() && timeCurrent < story.getEndTime()){
                        images.add(story.getImageUrl());
                        storyids.add(story.getStoryId());
                    }
                }

                storiesProgressView.setStoriesCount(images.size());
                storiesProgressView.setStoryDuration(5000L);
                storiesProgressView.setStoriesListener(StoryActivity.this);

                Log.d("counter? ", String.valueOf(counter));
                storiesProgressView.startStories(counter);

                Glide.with(getApplicationContext()).load(images.get(counter)).into(image);

                addView(storyids.get(counter));
                seenNumber(storyids.get(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfo(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getProfileImage()).into(story_photo);
                story_username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addView(String storyid){
        FirebaseDatabase.getInstance().getReference("Story").child(userid)
                .child(storyid).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
    }

    private void seenNumber(String storyid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
                .child(userid).child(storyid).child("views");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seen_number.setText(""+snapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}