package com.example.android_social_media;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.fragment.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcv;
    private UserAdapter userAdapter;
    private RecyclerView rcv_post;
    private postAdapter postAdapter;
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
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String key = "Messages";
        Map<String, Object> updates = new HashMap<>();
        updates.put("ID", id);
//        updates.put("message", "Test Message");
//        // Get the current time
//        LocalTime currentTime = LocalTime.now();
//
//        // Define a custom format
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//        // Format the current time
//        String formattedTime = currentTime.format(formatter);
//        updates.put("time",formattedTime);
//        UUID uuid = UUID.randomUUID();
//        updates.put("senderID", uuid.toString());
//
        db.child(key).child(id).setValue(updates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null){
                    Toast.makeText(getApplicationContext(),"Không update được dữ liệu!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                }
            }
        });

//        rcv = findViewById(R.id.rcv_id);
//        userAdapter = new UserAdapter(this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
//        rcv.setLayoutManager(linearLayoutManager);
//        userAdapter.setData(getListUser());
//        rcv.setAdapter(userAdapter);
//        rcv_post = findViewById(R.id.rcv_post);
//        postAdapter = new postAdapter(this);
//        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
//        rcv_post.setLayoutManager(linearLayoutManager1);
//        postAdapter.setData(getListPost());
//        rcv_post.setAdapter(postAdapter);


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
    public List<User> getListUser(){
        List<User> list = new ArrayList<>();
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        return list;
    }
    public List<post> getListPost(){
        List<post> list = new ArrayList<>();
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "ha",R.drawable.heart,R.drawable.comment, R.drawable.share));
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "Trinh",R.drawable.heart,R.drawable.comment, R.drawable.share));
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "ha",R.drawable.heart,R.drawable.comment, R.drawable.share));
        return list;
    }

}