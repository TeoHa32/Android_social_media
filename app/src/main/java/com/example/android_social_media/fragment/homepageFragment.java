package com.example.android_social_media.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android_social_media.R;
import com.example.android_social_media.adapter.UserAdapter;
import com.example.android_social_media.adapter.postAdapter;
import com.example.android_social_media.chat.ChatUsersActivity;
import com.example.android_social_media.model.User;
import com.example.android_social_media.model.post;

import java.util.ArrayList;
import java.util.List;

public class homepageFragment extends Fragment {
    private RecyclerView rcv;
    private UserAdapter userAdapter;
    private ImageView imgNewPost;
    private RecyclerView rcvPost;
    private postAdapter postAdapter;
    private ImageView btnChat;

    public homepageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        rcv = view.findViewById(R.id.rcv_id);
        userAdapter = new UserAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcv.setLayoutManager(linearLayoutManager);
        userAdapter.setData(getListUser());
        rcv.setAdapter(userAdapter);

        rcvPost = view.findViewById(R.id.rcv_post);
        postAdapter = new postAdapter(getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvPost.setLayoutManager(linearLayoutManager1);
        postAdapter.setData(getListPost());
        rcvPost.setAdapter(postAdapter);

        init(view);
        setOnClickListener();

        return view;
    }

    private void init(View view) {
        imgNewPost = view.findViewById(R.id.img_new_post);
        btnChat = view.findViewById(R.id.btnChat);
    }

    private void setOnClickListener() {
        imgNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImgPostFragment();
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatActivity();
            }
        });
    }

    private void selectImgPostFragment() {
        select_img_post_fragment selectImgPostFragment = new select_img_post_fragment();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, selectImgPostFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void startChatActivity() {
        Intent intent = new Intent(getActivity(), ChatUsersActivity.class);
        startActivity(intent);
    }

    private List<User> getListUser() {
        List<User> list = new ArrayList<>();
        list.add(new User("ha", R.drawable.ic_launcher_background));
        list.add(new User("ha", R.drawable.ic_launcher_background));
        list.add(new User("ha", R.drawable.ic_launcher_background));
        list.add(new User("ha", R.drawable.ic_launcher_background));
        list.add(new User("ha", R.drawable.ic_launcher_background));
        list.add(new User("ha", R.drawable.ic_launcher_background));
        list.add(new User("ha", R.drawable.ic_launcher_background));
        return list;
    }

    private List<post> getListPost() {
        List<post> list = new ArrayList<>();
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "ha", R.drawable.heart, R.drawable.comment, R.drawable.share));
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "Trinh", R.drawable.heart, R.drawable.comment, R.drawable.share));
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "ha", R.drawable.heart, R.drawable.comment, R.drawable.share));
        return list;
    }
}
