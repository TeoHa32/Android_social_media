package com.example.android_social_media.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android_social_media.PostActivity;
import com.example.android_social_media.R;
import com.example.android_social_media.adapter.SearchAdapter;
import com.example.android_social_media.adapter.SearchImageAdapter;
import com.example.android_social_media.model.SearchImageModel;
import com.example.android_social_media.model.SearchUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    EditText edtSearch;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<SearchImageModel> list;
    SearchImageAdapter adapter;
    ImageView btnHome, btnSearch, btnNewPost, btnProfile;
    String username, img;
    String key ="";

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        clickListener();

        loadImage();
    }

    private void clickListener() {
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResultFragment searchResultFragment = new SearchResultFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, searchResultFragment);
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

        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewPostActivity();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserData();
            }
        });

    }

    private void init(View view){
        searchView = view.findViewById(R.id.searchView);
        btnHome = view.findViewById(R.id.btnHome);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnNewPost = view.findViewById(R.id.btnNewPost);
        btnProfile = view.findViewById(R.id.btnProfile);
        edtSearch = view.findViewById(R.id.edtSearch);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);


        list = new ArrayList<>();
        adapter = new SearchImageAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    private void loadImage() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SearchImageModel searchImage = snapshot.getValue(SearchImageModel.class);
                    list.add(searchImage);
//                    Toast.makeText(getContext(), "Đã xảy ra lỗi khi truy vấn dữ liệu!" + list, Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void startNewPostActivity(){
        Intent intent = new Intent(getActivity(), PostActivity.class);
        startActivity(intent);
    }

    public void showUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username = snapshot.child("username").getValue(String.class);
                    img = snapshot.child("profileImage").getValue(String.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("img", img);

                    // Gọi phương thức xử lý hoạt động khác và truyền Bundle
                    processUserData(bundle);
                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi!", Toast.LENGTH_SHORT).show();
            }
        });
    } //end show data user

    private void processUserData(Bundle userDataBundle) {
        String username = userDataBundle.getString("username");
        String img = userDataBundle.getString("img");

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("img", img);
        if(key != null){
            bundle.putString("key", key);
        }

        profileFragment profileFragment = new profileFragment();
        profileFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, profileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}