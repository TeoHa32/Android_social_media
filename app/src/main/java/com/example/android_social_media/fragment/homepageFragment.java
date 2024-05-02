package com.example.android_social_media.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.PostActivity;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class homepageFragment extends Fragment {
    private ImageView imgNewPost;
    private RecyclerView rcvPost, storiesRecylerView;
    private postAdapter postAdapter;
    private List<post> postList;
    private ImageView btnChat, btnSearch, btnProfile, btnNotify;
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
        rcvPost.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rcvPost.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<post>();
        postAdapter = new postAdapter(getContext(), postList);
        rcvPost.setAdapter(postAdapter);
        init(view);
        setOnClickListener();
        checkFollowing();

        return view;
    }
    private void init(View view) {
        imgNewPost = view.findViewById(R.id.img_new_post);

        btnChat = view.findViewById(R.id.btnChat);
        btnSearch = view.findViewById(R.id.btn_search);
        btnProfile = view.findViewById(R.id.btnProfile);
        btnNotify = view.findViewById(R.id.btnNotify);

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


        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatActivity();
            }
        });
        imgNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewPostActivity();

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, searchFragment);
                fragmentTransaction.addToBackStack(null); // Add to back stack if needed
                fragmentTransaction.commit();
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(user.getUid());
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String username = snapshot.child("username").getValue(String.class);
                            String imageProfile = snapshot.child("profileImage").getValue(String.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("username", username);
                            bundle.putString("key", user.getUid());
                            bundle.putString("img", imageProfile);

                            profileFragment profile = new profileFragment();
                            profile.setArguments(bundle);
                            if (!isStateSaved()) { // Check if fragment state is saved
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, profile);
                                fragmentTransaction.addToBackStack(null); // Add to back stack if needed
                                fragmentTransaction.commit();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationFragment notificationFragment = new NotificationFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, notificationFragment);
                fragmentTransaction.addToBackStack(null); // Add to back stack if needed
                fragmentTransaction.commit();
            }
        });

    }
    private void startNewPostActivity(){
        Intent intent = new Intent(getActivity(), PostActivity.class);
        startActivity(intent);
    }


    private void startChatActivity() {
        Intent intent = new Intent(getActivity(), ChatUsersActivity.class);
        startActivity(intent);
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
                readPost();
                readStory();
//                return 0;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void readPost(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot datasnapshot: snapshot.getChildren()){
                    post post = datasnapshot.getValue(post.class);
                    Log.d("TAG1", post.getPublisher());
                    for(String id : followingList){
                        Log.d("TAG2", id);
                        if(post.getPublisher().equals(id)){
                            postList.add(post);
                        }
                    }
                }
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    post post = dataSnapshot.getValue(post.class);
                    if(post.getPublisher().equals(Uid)){
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
//                return timeCurrent;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
