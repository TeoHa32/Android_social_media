package com.example.android_social_media.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android_social_media.R;
import com.example.android_social_media.adapter.StoriesAdapter;
import com.example.android_social_media.adapter.postAdapter;
import com.example.android_social_media.chat.ChatUsersActivity;
import com.example.android_social_media.model.StoriesModel;
import com.example.android_social_media.model.post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class homepageFragment extends Fragment {

    private ImageView imgNewPost;
    private RecyclerView rcvPost, storiesRecylerView;
    private postAdapter postAdapter;
    private ImageView btnChat;
    FirebaseUser user;
    StoriesAdapter storiesAdapter;
    List<StoriesModel> storiesModelList;
    private List<String> followingList;


    public homepageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        rcvPost = view.findViewById(R.id.rcv_post);
        postAdapter = new postAdapter(getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvPost.setLayoutManager(linearLayoutManager1);
        postAdapter.setData(getListPost());
        rcvPost.setAdapter(postAdapter);

        init(view);
        setOnClickListener();
        checkFollowing();

        return view;
    }

    private void init(View view) {
//        imgNewPost = view.findViewById(R.id.img_new_post);
        btnChat = view.findViewById(R.id.btnChat);

        storiesRecylerView = view.findViewById(R.id.storiesRecyclerView);
        storiesRecylerView.setHasFixedSize(true);
        storiesRecylerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        storiesModelList = new ArrayList<>();
        storiesAdapter = new StoriesAdapter(getContext(), storiesModelList);
        storiesRecylerView.setAdapter(storiesAdapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }


    private void setOnClickListener() {
//        imgNewPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImgPostFragment();
//            }
//        });

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


    private List<post> getListPost() {
        List<post> list = new ArrayList<>();
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "ha", R.drawable.heart, R.drawable.comment, R.drawable.share));
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "Trinh", R.drawable.heart, R.drawable.comment, R.drawable.share));
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "ha", R.drawable.heart, R.drawable.comment, R.drawable.share));
        return list;
    }

    private void checkFollowing(){
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getValue(String.class));
                    Log.d("phần tử của following List: ", snapshot.getValue(String.class));
                }

//                readPosts();
                readStory();
                return 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Xem story của nguời mình theo dõi
    private void readStory(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long timeCurrent = System.currentTimeMillis();
                storiesModelList.clear();
                storiesModelList.add(new StoriesModel("",0,0,"",
                        FirebaseAuth.getInstance().getCurrentUser().getUid()));

                for(String id : followingList){
                    int countStory = 0;
                    StoriesModel story = null;
                    for(DataSnapshot data : snapshot.child(id).getChildren()){
                        story = data.getValue(StoriesModel.class);
                        if(timeCurrent > story.getStartTime() && timeCurrent < story.getEndTime()){
                            countStory++;
                        }
                    }

                    if(countStory > 0){
                        storiesModelList.add(story);
                    }
                }
                storiesAdapter.notifyDataSetChanged();
                return timeCurrent;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
