package com.example.android_social_media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.example.android_social_media.fragment.SignUpFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcv;
    private UserAdapter userAdapter;
    private RecyclerView rcv_post;
    private postAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hompage);
        //-------------------------------------
        rcv = findViewById(R.id.rcv_id);
        userAdapter = new UserAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        rcv.setLayoutManager(linearLayoutManager);
        userAdapter.setData(getListUser());
        rcv.setAdapter(userAdapter);
        rcv_post = findViewById(R.id.rcv_post);
        postAdapter = new postAdapter(this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rcv_post.setLayoutManager(linearLayoutManager1);
        postAdapter.setData(getListPost());
        rcv_post.setAdapter(postAdapter);
        //Khởi tạo ban đầu sẽ ở trang đăng ký.

//        SignUpFragment signUpFragment = new SignUpFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, signUpFragment);
//        fragmentTransaction.addToBackStack(null); // Add to back stack if needed
//        fragmentTransaction.commit();
//        //đổi màu
//        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
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