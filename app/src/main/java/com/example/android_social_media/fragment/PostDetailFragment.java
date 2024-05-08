package com.example.android_social_media.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;
import com.example.android_social_media.adapter.postAdapter;
import com.example.android_social_media.model.ChatModel;
import com.example.android_social_media.model.post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostDetailFragment extends Fragment {
    String postid;
    private RecyclerView recyclerView;
    private postAdapter PostAdapter;
    private List<post> postList;
    private ImageView btn_delete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
//        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
//        postid = preferences.getString("postid","none");
//        recyclerView = view.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//        postList = new ArrayList<>();
//        PostAdapter = new postAdapter(getContext(),postList);
//        recyclerView.setAdapter(PostAdapter);
//        readPost();
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        postid = prefs.getString("postid", "none");
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        PostAdapter = new postAdapter(getContext(), postList);
        recyclerView.setAdapter(PostAdapter);

        btn_delete = view.findViewById(R.id.btn_delete);


        readPost();
        return view;
    }

    private void readPost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                post p= dataSnapshot.getValue(post.class);
                postList.add(p);
                PostAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(),"Xóa thành công", Toast.LENGTH_SHORT).show();

                                String key = FirebaseAuth.getInstance().getUid();
                                Log.d("userid?", key);

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(key);

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Log.d("vo kh?", "dc");
                                        String username = snapshot.child("username").getValue(String.class);
                                        String profileImage = snapshot.child("profileImage").getValue(String.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("key", FirebaseAuth.getInstance().getUid());
                                        bundle.putString("img", profileImage);
                                        bundle.putString("username", username);
                                        profileFragment profileFragment = new profileFragment();
                                        profileFragment.setArguments(bundle);
                                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_container, profileFragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Xóa thất bại", Toast.LENGTH_SHORT).show();
                                profileFragment profileFragment = new profileFragment();
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, profileFragment);
                                fragmentTransaction.addToBackStack(null); // Add to back stack if needed
                                fragmentTransaction.commit();
                            }
                        });

            }
        });
    }

}