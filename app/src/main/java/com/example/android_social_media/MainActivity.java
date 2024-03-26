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
        //Khởi tạo ban đầu sẽ ở trang đăng ký.

        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, signUpFragment);
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

}