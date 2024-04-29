package com.example.android_social_media.fragment;

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

import com.example.android_social_media.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class profileFragment extends Fragment {

    Button btnInfoProfile;
    String username;
    String url;

    String uid,key;
    TextView follow, following;

    ImageView btnHome, btnSearch;

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (bundle != null) {
            key = bundle.getString("key");
            url = bundle.getString("img");
            username = bundle.getString("username");

            key = bundle.getString("key");
            Log.d("bundle khoa tu dong",key);

            if(bundle.getString("key").equals("")){
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
                // Sử dụng Picasso để tải hình ảnh từ URL và hiển thị nó trong ImageView
                Picasso.get().load(url).into(img);
            } else {
                // Nếu imageUrl là null hoặc rỗng, bạn có thể thực hiện các xử lý khác tùy thuộc vào yêu cầu của bạn
                // Ví dụ: Hiển thị một hình ảnh mặc định hoặc hiển thị một tin nhắn lỗi
                img.setImageResource(R.drawable.ic_profile);
            }
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
    }

    private void init(View view) {
        btnInfoProfile = view.findViewById(R.id.btnInfoProfile);
        btnHome = view.findViewById(R.id.btnHome);
        btnSearch = view.findViewById(R.id.btnSearch);
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

}