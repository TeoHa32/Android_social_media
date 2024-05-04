package com.example.android_social_media.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_social_media.PostActivity;
import com.example.android_social_media.R;
import com.example.android_social_media.StoryActivity;
import com.example.android_social_media.adapter.MyfotosAdapter;
import com.example.android_social_media.model.StoriesModel;
import com.example.android_social_media.model.post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class profileFragment extends Fragment {

    Button btnInfoProfile;
    String username;
    String url;

    String uid,key;
    TextView follow, following,postcount, txtTieuSu;

    ImageView btnHome, btnSearch, btnLogout, btnAddPost,btnSave, myphoto;
    private RecyclerView recyclerView;
    MyfotosAdapter myfotosAdapter;
    List<post> postList;
    private List<String> mySaves;
    RecyclerView recycler_save;
    MyfotosAdapter myfotosAdapter_save;
    List<post> postListSave;

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageView img = view.findViewById(R.id.imageView5);
        TextView txtUsername = view.findViewById(R.id.txtUsername);
        TextView txtUsernameProfile = view.findViewById(R.id.usernameProfile);
        postcount = view.findViewById(R.id.textView4);
        txtTieuSu = view.findViewById(R.id.tieusu);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (bundle != null) {
            key = bundle.getString("key");
            url = bundle.getString("img");
            username = bundle.getString("username");

            key = bundle.getString("key");

            if(key.equals("")){
                key = user.getUid();
                Log.d("id người dùng:",key);
            }
            else{
                key = bundle.getString("key");
                Log.d("current user:",key);
            }

            txtUsername.setText(username);
            txtUsernameProfile.setText(username);
            if (url != null && !url.isEmpty()) {
                Picasso.get().load(url).into(img);
            } else {
                img.setImageResource(R.drawable.ic_profile);
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(key).child("tieusu");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String tieusu = snapshot.getValue(String.class);
                        txtTieuSu.setText(tieusu);
                    }
                    else{
                        txtTieuSu.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (user != null) {

            uid = user.getUid();

            uid = user.getUid();
            Log.d("co uid", uid);
            getFollowerCount();
        } else {
            Log.d("ko co uid", "khong");
            // Xử lý trường hợp người dùng chưa đăng nhập
        }

        follow = view.findViewById(R.id.textView5);
        following = view.findViewById(R.id.textView6);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clickListener();
        myFotos();
        mysaves();
    }

    private void init(View view) {
        btnInfoProfile = view.findViewById(R.id.btnInfoProfile);
        btnHome = view.findViewById(R.id.btnHome);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAddPost = view.findViewById(R.id.btnAddPost);
        btnSave = view.findViewById(R.id.theodoi);
        myphoto = view.findViewById(R.id.baiviet);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        myfotosAdapter = new MyfotosAdapter(getContext(),postList);
        recyclerView.setAdapter(myfotosAdapter);

        recycler_save = view.findViewById(R.id.recyclerViewSave);
        recycler_save.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerSave =new GridLayoutManager(getContext(),3);
        recycler_save.setLayoutManager(linearLayoutManagerSave);
        postListSave = new ArrayList<>();
        myfotosAdapter_save = new MyfotosAdapter(getContext(),postListSave);
        recycler_save.setAdapter(myfotosAdapter_save);
        recyclerView.setVisibility(view.VISIBLE);
        recycler_save.setVisibility(view.GONE);
    }
    private void clickListener() {
        btnInfoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoProfileFragment infoProfileFragment = new InfoProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                infoProfileFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, infoProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chuyển đến trang chủ
                homepageFragment homepageFragment = new homepageFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, homepageFragment);
                fragmentTransaction.addToBackStack(null); // Add to back stack if needed
                fragmentTransaction.commit();
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
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment login = new LoginFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, login);
                fragmentTransaction.addToBackStack(null); // Add to back stack if needed
                fragmentTransaction.commit();
            }
        });
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);
            }
        });

        myphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                recycler_save.setVisibility(View.GONE);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recycler_save.setVisibility(View.VISIBLE);
            }
        });

    }
    private void getFollowerCount() {
        if(key != null){
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(key);
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long followerCount = dataSnapshot.child("follower").getChildrenCount();
                    long followingCount = dataSnapshot.child("following").getChildrenCount();
                    // long followerCount = dataSnapshot.getChildrenCount();
                    // Sử dụng followerCount ở đây, ví dụ:
                    Log.d("co hien thi khong ", String.valueOf(followingCount));
                    // Gọi một phương thức hoặc thực hiện một hành động khác với followerCount ở đây nếu cần
                    //Log.d("dem so luong", String.valueOf(followingCount));

                    follow.setText(String.valueOf(followerCount));
                    following.setText(String.valueOf(followingCount));
//                    return followerCount;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý khi có lỗi xảy ra trong quá trình truy vấn dữ liệu
                }
            });
        }

    }

//    private void getFollowerCount() {
//        if(key != null){
//            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(key);
//            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    long followerCount = dataSnapshot.child("follower").getChildrenCount();
//                    long followingCount = dataSnapshot.child("following").getChildrenCount();
//
//                    Log.d("co hien thi khong ", String.valueOf(followingCount));
//                    // Gọi một phương thức hoặc thực hiện một hành động khác với followerCount ở đây nếu cần
//                    //Log.d("dem so luong", String.valueOf(followingCount));
//
//                    follow.setText(String.valueOf(followerCount));
//                   following.setText(String.valueOf(followingCount));
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Xử lý khi có lỗi xảy ra trong quá trình truy vấn dữ liệu
//                }
//            });
//        }
//
//    }
private void myFotos(){
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
            postList.clear();
            for (DataSnapshot snapshot : datasnapshot.getChildren()){
                post p = snapshot.getValue(post.class);
                if(p.getPublisher().equals(key)){
                    postList.add(p);
                }
            }
            Collections.reverse(postList);
            Long a = (long) postList.size();
            postcount.setText(String.valueOf(a));
            myfotosAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
    private void mysaves(){
        mySaves = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves")
                .child(key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        mySaves.add(dataSnapshot.getKey());

                }
                readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readSaves(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        Log.d("TAGke", "readSaves: "+key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postListSave.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    post p = dataSnapshot.getValue(post.class);
                    for(String id: mySaves){

                        Log.d("TAGIDpost", "onDataChange: "+p.getPostId());
                        if(p.getPostId().equals(id)){
                            postListSave.add(p);
                        }
                    }
                }
                myfotosAdapter_save.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}