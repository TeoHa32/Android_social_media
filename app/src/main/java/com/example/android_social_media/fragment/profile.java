package com.example.android_social_media.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;
import com.example.android_social_media.chat.ChatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.jakewharton.threetenabp.AndroidThreeTen;

//import com.jakewharton.threetenabp.AndroidThreeTen;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class profile extends Fragment {
    private TextView nameTv, toolbarNameTv,statusTv,followersCountTv,followingCountTv,postCountTv;
    private CircleImageView profileImage;
    private Button flbt,chatButton;
    private Button followBtn;
    private RecyclerView recyclerView;

    private long cout;

    private long count;

    private String UserID;

    public profile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AndroidThreeTen.init(getActivity());
//        AndroidThreeTen.init(getActivity());
        return inflater.inflate(R.layout.profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        //View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Khởi tạo các thành phần giao diện và gán giá trị
        init(view);

        Bundle bundle = getArguments();
        UserID = bundle.getString("UserID");

        // Load dữ liệu từ Firebase Realtime Database và hiển thị lên giao diện
        loadUserDataFromFirebase();
        getFollowerCount();
        checkfollowing();

        LinearLayout linearLayout = view.findViewById(R.id.followersClick);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("nhan","roi ne");
                // Xử lý sự kiện click tại đây
                //Toast.makeText(getActivity(), "LinearLayout clicked!", Toast.LENGTH_SHORT).show();
            }
        });



        String buttonText = chatButton.getText().toString();
        chatButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                AndroidThreeTen.init(getActivity());

//                AndroidThreeTen.init(getActivity());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String iduser1 = "vYEYezjxu8QT5HgpCRgcxuEOsUs1";
                String iduser2 = "PVz33IummMfEXX6594CKN0nCJhC3";
                DatabaseReference msgRef = database.getReference("Messages");
                String id = msgRef.push().getKey();
                LocalDateTime currentTime = null;
                currentTime = LocalDateTime.now();

                // Định dạng thời gian
                DateTimeFormatter formatter = null;
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                String  formattedTime = currentTime.format(formatter);


                // Tạo một hashmap để đại diện cho dữ liệu của user
                HashMap<String, Object> userData = new HashMap<>();
                userData.put("uid", Arrays.asList(iduser1, iduser2));
                userData.put("time", formattedTime );
                userData.put("lastMessage", "hello");
                userData.put("id", id);

//              Đẩy dữ liệu của user lên Firebase Realtime Database với key tự động// Tạo key tự động cho user
                msgRef.child(id).setValue(userData);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("uid", iduser2);
                intent.putExtra("userid", iduser1);
                intent.putExtra("id", id);
                startActivity(intent);
//                Intent intent = new Intent(ChatUsersActivity.this, ChatActivity.class);
//                intent.putExtra("uid", oppositeUID);

               // Log.d("nhan chat","roi ne");

                // Log.d("nhan chat","roi ne");
            }
        });

        flbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flbt.getText().equals("Follow")){
                    addfollow();
                    flbt.setText("Unfollow");
                } else {
                    unfollow();
                    flbt.setText("Follow");
                }
            }
        });

        // return view;
    }



    private void init(View view){

        //Toolbar toolbar = view.findViewById(R.id.toolbar);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        assert getActivity() != null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        nameTv = view.findViewById(R.id.nameTv);
        statusTv = view.findViewById(R.id.statusTV);
        toolbarNameTv = view.findViewById(R.id.toolbarNameTV);
        profileImage = view.findViewById(R.id.profileImage);
        followersCountTv = view.findViewById(R.id.followersCountTv);
        toolbarNameTv = view.findViewById(R.id.toolbarNameTV);
        followingCountTv = view.findViewById(R.id.followingCountTv);
        flbt = view.findViewById(R.id.followBtn);
        chatButton = view.findViewById(R.id.chatBtn);

//        postCountTv = view.findViewById(R.id.postCountTv);
//        profileImage = view.findViewById(R.id.profileImage);
//        followBtn = view.findViewById(R.id.followBtn);
//        recyclerView = view.findViewById(R.id.recyclerView);
//        countLayout = view.findViewById(R.id.countLayout);
//        editProfileBtn = view.findViewById(R.id.edit_profileImage);
//        startChatBtn = view.findViewById(R.id.startChatBtn);
//
//
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
    }

    private void loadUserDataFromFirebase() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(UserID);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String status = dataSnapshot.child("status").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImage").getValue(String.class);
                    String toolbarusername = dataSnapshot.child("username").getValue(String.class);
                    // int followersCount = follow();

                    //    String followingCount = dataSnapshot.child("profileImage").getValue(String.class);
                    //          String postCount = dataSnapshot.child("profileImage").getValue(String.class);
//                    long followersCount = dataSnapshot.child("follower").getValue(Long.class);
//                    long followingCount = dataSnapshot.child("following").getValue(Long.class);
//                    long postCount = dataSnapshot.child("postCount").getValue(Long.class);

                    // Hiển thị dữ liệu lên giao diện
                    //toolbarNameTv.setText(username);
                    Log.d("co vao day khong", username);
                    nameTv.setText(username);
                    statusTv.setText(status);
                    toolbarNameTv.setText(toolbarusername);


//                    followingCountTv.setText(String.valueOf(followingCount));
//                    postCountTv.setText(String.valueOf(postCount));

                    // Load hình ảnh đại diện từ Firebase Storage và đặt hình ảnh cho CircleImageView
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Picasso.get().load(profileImageUrl).into(profileImage);
                    }
                } else Log.d("co vao day khong", "khong co");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn dữ liệu
            }
        });
    }

    private void getFollowerCount() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(UserID);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long followerCount = dataSnapshot.child("follower").getChildrenCount();
                long followingCount = dataSnapshot.child("following").getChildrenCount();

                cout = followingCount;

                count = followingCount;
                Log.d("Count " , String.valueOf(count));

                followersCountTv.setText(String.valueOf(followerCount));
                followingCountTv.setText(String.valueOf(followingCount));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn dữ liệu
            }
        });
    }

    private void checkfollowing(){
        String username = "jukhx1A154W2ERD0nJ9AG3LtyMI2"; //username của trang cá nhân người nhấn vào
        // thay thế child("users").child("-NuXjEdYF637E4qikFCB").child("follower"); thành child("users").child(username).child("following");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(UserID).child("follower");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String followerID = childSnapshot.getValue(String.class);
                        if(followerID.equals(username)){
                            flbt.setText("Unfollow");
                            break;
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void unfollow(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        String userIdToRemove = UserID;
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("following");
        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        if (userId.equals(userIdToRemove)) {
                            // Xóa người dùng khỏi mảng
                            userSnapshot.getRef().removeValue();
                            // hoặc userSnapshot.getRef().setValue(null);
                            break; // Thoát khỏi vòng lặp sau khi xóa
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

    }
    private void addfollow(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        String userIdToAdd = UserID;
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference()
                .child("users");

        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long followingCount = dataSnapshot.child(uid).child("following").getChildrenCount();
                    long followerCount = dataSnapshot.child(UserID).child("follower").getChildrenCount();
                    Log.d("Chao moij nguoi", String.valueOf(followingCount));
                    String dem =  String.valueOf(followingCount);
                    followingRef.child(uid).child("following").child(dem).setValue(userIdToAdd);
                    followingRef.child(UserID).child("follower").child(dem).setValue(userIdToAdd);
                    String count1 = String.valueOf(followersCountTv.getText());
                    long number = Long.parseLong(count1)+1;

//                    followingCount = followingCount + 1;
                    followersCountTv.setText(String.valueOf(number));
//                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                        String userId = userSnapshot.getKey();
//                        if (userId.equals(userIdToRemove)) {
//                            // Xóa người dùng khỏi mảng
//                            userSnapshot.getRef().removeValue();
//                            // hoặc userSnapshot.getRef().setValue(null);
//                            break; // Thoát khỏi vòng lặp sau khi xóa
//                        }
//                    }

//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý khi có lỗi xảy ra
//            }
//        });
//
//    }
//    private void addfollow(){
//        String userIdToAdd = "ID_of_user_to_add";
//        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference()
//                .child("users").child("current_user_id").child("following");
//
//// Kiểm tra xem người dùng đã tồn tại trong mảng chưa
//        followingRef.child(userIdToAdd).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.exists()) {
//                    // Người dùng chưa tồn tại trong mảng, thêm người dùng mới vào
//                    followingRef.child(userIdToAdd).setValue(true);
//                    // hoặc followingRef.child(userIdToAdd).setValue("any_value"); nếu cần
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý khi có lỗi xảy ra
//            }
//        });
//    }


                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }


}