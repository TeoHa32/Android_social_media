package com.example.android_social_media.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;
import com.example.android_social_media.adapter.MyfotosAdapter;
import com.example.android_social_media.chat.ChatActivity;
import com.example.android_social_media.model.ChatModel;
import com.example.android_social_media.model.post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.jakewharton.threetenabp.AndroidThreeTen;


import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class profile extends Fragment {
    private TextView nameTv, toolbarNameTv,statusTv,followersCountTv,followingCountTv,postCountTv;
    private CircleImageView profileImage;
    private Button flbt,chatButton;
    private Button followBtn;
    private RecyclerView recyclerView;
    MyfotosAdapter myfotosAdapter;
    List<post> postList;

    List<ChatModel> chat;

    ImageButton list;
    String key;
    ImageView bthome,btinfo,btsearch;
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
//        AndroidThreeTen.init(getActivity());
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


//        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
//        messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Lấy danh sách tất cả các key
//                List<String> messageKeys = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    messageKeys.add(snapshot.getKey());
//                }
//
//                // Loại bỏ key đầu tiên từ danh sách (nếu có ít nhất một key)
//                if (messageKeys.size() > 1) {
//                    messageKeys.remove(0);
//                }
//
//                // Xóa tất cả các key còn lại
//                for (String key : messageKeys) {
//                    messagesRef.child(key).removeValue();
//                }
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý khi truy vấn bị hủy
//            }
//        });

        Bundle bundle = getArguments();
        UserID = bundle.getString("UserID");

        // Load dữ liệu từ Firebase Realtime Database và hiển thị lên giao diện
        loadUserDataFromFirebase();
        getFollowerCount();

        checkfollowing();
        myFotos();

//        checkfollowing();

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
//        chatButton.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View v) {
//
//                AndroidThreeTen.init(getActivity());
//
////                AndroidThreeTen.init(getActivity());
//                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                String tk = currentUser.getUid();
//
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                String iduser1 = tk;
//                List<String> uids = new ArrayList<>();
//                String iduser2 = UserID;
//                DatabaseReference msgRef = database.getReference("Messages");
//                msgRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            for(DataSnapshot data : snapshot.getChildren()){
//                                // Lấy giá trị của trường "uid"
//                                Iterable<DataSnapshot> uidSnapshots = data.child("uid").getChildren();
//
//                                for (DataSnapshot uidSnapshot : uidSnapshots) {
//                                    String uid1 = uidSnapshot.getValue(String.class);
//                                    if (uid1 != null) {
//                                        uids.add(uid1);
//
//                                    }
//                                }
//                                }
//                            if(iduser1.equals(uids.get(0) )&& iduser2.equals(uids.get(1))){
//
//                                //  Log.d("co tat ca", "2 thu");
//                            }
//                            else{
//                              //  DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages");
//                                String messageID = msgRef.push().getKey(); // Tạo một key duy nhất cho mỗi tin nhắn
//
//                                // Tạo đối tượng SimpleDateFormat với định dạng mong muốn
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
//                                // Lấy thời gian hiện tại
//                                String currentTime = sdf.format(new Date());
//                                msgRef.child(messageID).child("uid").child("0").setValue(iduser1);
//                                msgRef.child(messageID).child("uid").child("1").setValue(iduser2);
//                                //followingRef.child(UserID).child("").child(dem2).setValue(uid);
////                                Map<String, Object> messageMap = new HashMap<>();
////                                messageMap.put("id", messageID);
////                                messageMap.put("uid", Arrays.asList(iduser1, iduser2));
//
//                               // msgRef.child(messageID).setValue(messageMap);
//                                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                                intent.putExtra("uid", iduser2);
//                                intent.putExtra("userid", iduser1);
//                                intent.putExtra("id", messageID);
//                                startActivity(intent);
//                                Log.d("dddddddđ","fffffffff");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        // Xử lý lỗi nếu có
//                    }
//                });
//                //String id = msgRef.push().getKey();
////
////                LocalDateTime currentTime = null;
////                currentTime = LocalDateTime.now();
////
////                // Định dạng thời gian
////                DateTimeFormatter formatter = null;
////                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
////                String  formattedTime = currentTime.format(formatter);
////                HashMap<String, Object> userData = new HashMap<>();
////                userData.put("uid", Arrays.asList(iduser1, iduser2));
////                userData.put("time", formattedTime );
////                userData.put("lastMessage", "hello");
////                userData.put("id", id);
////                msgRef.child(id).setValue(userData);
////                Intent intent = new Intent(getActivity(), ChatActivity.class);
////                intent.putExtra("uid", iduser2);
////                intent.putExtra("userid", iduser1);
////                intent.putExtra("id", id);
////                startActivity(intent);
//
//            }
//        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                addmessage();


//                AndroidThreeTen.init(getActivity());

//                AndroidThreeTen.init(getActivity());
//
//                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                String tk = currentUser.getUid();
//
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                String iduser1 = tk;
//                List<String> uids = new ArrayList<>();
//                String iduser2 = UserID;
//                DatabaseReference msgRef = database.getReference("Messages");
//
//                // Kiểm tra xem cuộc trò chuyện đã tồn tại hay chưa
//                // Nếu không tồn tại, thực hiện tạo tin nhắn mới
//                // Nếu đã tồn tại, chuyển sang màn hình ChatActivity
//                boolean conversationExists = false;
//                msgRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            // Thực hiện kiểm tra xem cuộc trò chuyện đã tồn tại
//                            for (DataSnapshot data : snapshot.getChildren()) {
//                                Iterable<DataSnapshot> uidSnapshots = data.child("uid").getChildren();
//
//                                for (DataSnapshot uidSnapshot : uidSnapshots) {
//                                    String uid1 = uidSnapshot.getValue(String.class);
//                                    if (uid1 != null) {
//                                        uids.add(uid1);
//
//                                    }
//                                }
//                            }
//                            addmess(uids,iduser1,iduser2);
//                            return;
//
//                            // Kiểm tra xem cuộc trò chuyện đã tồn tại
//
//                        }
//                    }
//
//
//
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });



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

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFotos();
            }
        });

        bthome.setOnClickListener(new View.OnClickListener() {
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

        btinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
                key = uid;
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                                String username = dataSnapshot.child("username").getValue(String.class);
                                String profileImage = dataSnapshot.child("profileImage").getValue(String.class);
                                if (username != null) {
                                    navigateToProfile(username, profileImage);
                                }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        btsearch.setOnClickListener(new View.OnClickListener() {
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
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        myfotosAdapter = new MyfotosAdapter(getContext(),postList);
        recyclerView.setAdapter(myfotosAdapter);
        list = view.findViewById(R.id.my_list);
        postCountTv = view.findViewById(R.id.postCountTv);
        bthome = view.findViewById(R.id.btnHome);
        btinfo = view.findViewById(R.id.btnProfile);
        btsearch = view.findViewById(R.id.btnSearch);
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
                    nameTv.setText(username);
                    statusTv.setText(status);
                    toolbarNameTv.setText(toolbarusername);
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
            }
        });
    }

//    private void checkfollowing(){
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = currentUser.getUid();
//        String username = UserID;
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("following");
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.exists()) {
//                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                        String followerID = childSnapshot.getValue(String.class);
//                        if(followerID.equals(username)){
//                            flbt.setText("Unfollow");
//                            break;
//                        }
//                    }
//                }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void checkfollowing(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        String username = UserID;
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("following");
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
        long dem = 0;
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference()
                .child("users");
        followingRef.child(uid).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //long followerCount = dataSnapshot.child(UserID).child("follower").getChildrenCount();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId1 = (String) userSnapshot.getValue();
                        if (userId1.equals(userIdToRemove)) {
                            // Xóa người dùng khỏi mảng
                            userSnapshot.getRef().removeValue();
                            break;
                        }
                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

        followingRef.child(userIdToRemove).child("follower").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long followerCount = dataSnapshot.getChildrenCount();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId1 = (String) userSnapshot.getValue();
                        if (userId1.equals(uid)) {
                            // Xóa người dùng khỏi mảng
                            userSnapshot.getRef().removeValue();
                            Log.d("xoa duoc 2", String.valueOf(followerCount));
                            followerCount = followerCount -1;
                            followersCountTv.setText(String.valueOf(followerCount));


                            break;
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
        String demgt1;
        List <String> dem = new ArrayList<>();
        List <String> demflw = new ArrayList<>();
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference()
                .child("users");

        if(!dem.isEmpty()){
            Log.d("gia tri cuoi cung", dem.get(0));

        }

        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot data = dataSnapshot.child(uid).child("following");
                    for (DataSnapshot userSnapshot : data.getChildren()) {

                        String demgt = userSnapshot.getKey();
                        dem.add(demgt);

                    }

                    DataSnapshot data1 = dataSnapshot.child(UserID).child("follower");
                    for (DataSnapshot userSnapshot : data1.getChildren()) {
                        String demgt = userSnapshot.getKey();
                        demflw.add(demgt);
                    }
                    if(!demflw.isEmpty()){
                        Log.d("gia tri cuoi cung1", demflw.get(demflw.size()-1));
                    }

                    long followingCount = dataSnapshot.child(uid).child("following").getChildrenCount();
                    long followerCount = dataSnapshot.child(UserID).child("follower").getChildrenCount();
                    Log.d("Chao moij nguoi", String.valueOf(followingCount));

                    String dem1 = "";
                    String dem2 = "";
                    if(dem.isEmpty()){
                        if(demflw.isEmpty()){
                            dem2 = "0";
                        }
                        else {
                            long b = Long.parseLong(demflw.get(demflw.size()-1))+1;

                            dem2 = String.valueOf(b);

                        }
                         dem1 =  "0";
                        dem1 =  String.valueOf(dem1);
                    }
                    else if(demflw.isEmpty()){
                        if (dem.isEmpty()){
                            dem1 =  "0";
                            dem1 =  String.valueOf(dem1);
                        }
                        else{
                            long a = Long.parseLong(dem.get(dem.size()-1))+1;
                            dem1 =  String.valueOf(a);

                        }
                        dem2 = "0";
                       dem2 = String.valueOf(dem2);
                    }
                    else {
                        long a = Long.parseLong(dem.get(dem.size()-1))+1;
                        long b = Long.parseLong(demflw.get(demflw.size()-1))+1;

                        dem1 =  String.valueOf(a);
                        dem2 = String.valueOf(b);

                    }
                    Log.d("dem1",dem1);
                    Log.d("dem2",dem2);
                    followingRef.child(uid).child("following").child(dem1).setValue(userIdToAdd);
                    followingRef.child(UserID).child("follower").child(dem2).setValue(uid);


//                    followingRef.child(uid).child("following").setValue(userIdToAdd);
//                    followingRef.child(UserID).child("follower").setValue(uid);
                    String count1 = String.valueOf(followersCountTv.getText());
                    long number = Long.parseLong(count1)+1;

//                    followingCount = followingCount + 1;
                    followersCountTv.setText(String.valueOf(number));


                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }

    private void addmessage(){

        AndroidThreeTen.init(getActivity());

//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = currentUser.getUid();
//        String userIdToAdd = UserID;
//        DatabaseReference messageSnapshot = FirebaseDatabase.getInstance().getReference().child("Messages");
//
//        messageSnapshot.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    String messageID = messageSnapshot.getKey();
//
//                }
//            }
//
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý khi có lỗi xảy ra
//            }
//        });
      //  DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("example");
//        AndroidThreeTen.init(getActivity());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String tk = currentUser.getUid();

        String iduser1 = tk;
        List<String> uids = new ArrayList<>();
        String iduser2 = UserID;
//        DatabaseReference msgRef = database.getReference("Messages");
        DatabaseReference msgRef = FirebaseDatabase.getInstance().getReference().child("Messages");
//
//// Thực hiện lắng nghe sự kiện chỉ một lần để lấy dữ liệu
        msgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Thực hiện xử lý dữ liệu lấy về từ cơ sở dữ liệu
//                String data = snapshot.getValue(String.class);
//                Log.d("Data from Firebase", data);
//                d
                if(snapshot.exists()){
                   // ChatModel c = new ChatModel();
                    // Thực hiện kiểm tra xem cuộc trò chuyện đã tồn tại
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String d = snapshot.getKey();
                        ChatModel c = new ChatModel();
                        c.setId(d);
                        Iterable<DataSnapshot> uidSnapshots = data.child("uid").getChildren();

                        for (DataSnapshot uidSnapshot : uidSnapshots) {
                            String uid1 = uidSnapshot.getValue(String.class);
                            if (uid1 != null) {
                                uids.add(uid1);

                            }
                        }
                        c.getUid(uids);
                        chat.add(c);
                    }
                    if(!uids.isEmpty()){
                        Log.d("tk 1",uids.get(2));
                        Log.d("tk 1",iduser1);
                        Log.d("tk 2",uids.get(3));
                        Log.d("tk 2",iduser2);
                        Log.d("dieu kien ds", String.valueOf(iduser1.equals(uids.get(0)) && iduser2.equals(uids.get(1))));
                        for(int i = 0; i< uids.size();i++){
                            if( i %2 == 0){
                                if (iduser1.equals(uids.get(i)) && iduser2.equals(uids.get(i+1))) {
                                    // conversationExists = true;
                                   // Log.d("cai gi","ko");
                                    break;
                                }

                            }
                            else if(i == uids.size()-1){
                                String messageID = msgRef.push().getKey(); // Tạo một key duy nhất cho mỗi tin nhắn
                                Log.d("cai gi",messageID);
                                // Tạo dữ liệu tin nhắn mới
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                                String currentTime = sdf.format(new Date());
                                msgRef.child(messageID).child("id").setValue(messageID);
                                msgRef.child(messageID).child("uid").child("0").setValue(iduser1);
                                msgRef.child(messageID).child("uid").child("1").setValue(iduser2);

                                // Chuyển sang màn hình ChatActivity
                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                intent.putExtra("uid", iduser2);
                                intent.putExtra("userid", iduser1);
                                intent.putExtra("id", messageID);
                                startActivity(intent);
                            }
                        }
//                        if (iduser1.equals(uids.get(0)) && iduser2.equals(uids.get(1))) {
//                            // conversationExists = true;
//                            Log.d("cai gi","ko");
//                        }

                        // Nếu cuộc trò chuyện chưa tồn tại, thực hiện tạo tin nhắn mới
//                        else {
//                            String messageID = msgRef.push().getKey(); // Tạo một key duy nhất cho mỗi tin nhắn
//                            Log.d("cai gi",messageID);
//                            // Tạo dữ liệu tin nhắn mới
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
//                            String currentTime = sdf.format(new Date());
//                            msgRef.child(messageID).child("id").setValue(messageID);
//                            msgRef.child(messageID).child("uid").child("0").setValue(iduser1);
//                            msgRef.child(messageID).child("uid").child("1").setValue(iduser2);
//
//                            // Chuyển sang màn hình ChatActivity
//                            Intent intent = new Intent(getActivity(), ChatActivity.class);
//                            intent.putExtra("uid", iduser2);
//                            intent.putExtra("userid", iduser1);
//                            intent.putExtra("id", messageID);
//                            startActivity(intent);
//                        }
                    }

                    //addmess(uids,iduser1,iduser2);
                    Log.d("Message", uids.get(0));

                    // Kiểm tra xem cuộc trò chuyện đã tồn tại
                    //Log.d("stt",uids.get(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi truy vấn bị hủy
            }
        });

// Các câu lệnh sau đây sẽ tiếp tục được thực hiện ngay sau khi sự kiện đã được thực hiện
    //    Log.d("Message", uids.get(0));

// Các câu lệnh khác ở đây

    }

    private void myFotos(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                postList.clear();

                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    post p = snapshot.getValue(post.class);
                    if(p.getPublisher().equals(UserID)){
                        postList.add(p);
                    }
                }
                Long a = (long) postList.size();
                postCountTv.setText(String.valueOf(a));
                Collections.reverse(postList);
                myfotosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void navigateToProfile(String username,String img){
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("img", img);

        if(key != null){
            bundle.putString("key", key);
        }
        profileFragment profile = new profileFragment();
        profile.setArguments(bundle); // Gán Bundle cho Fragment
        FragmentManager manager = requireActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.fragment_container, profile);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void  addmess(List uids,String iduser1,String iduser2){
        DatabaseReference msgRef = FirebaseDatabase.getInstance().getReference("Messages");

        //DatabaseReference msgRef = database.getReference("Messages");
        if (iduser1.equals(uids.get(0)) && iduser2.equals(uids.get(1))) {
            // conversationExists = true;
            Log.d("cai gi","ko");
        }

        // Nếu cuộc trò chuyện chưa tồn tại, thực hiện tạo tin nhắn mới
        else {
            String messageID = msgRef.push().getKey(); // Tạo một key duy nhất cho mỗi tin nhắn
            Log.d("cai gi1", iduser1);
            Log.d("cai gi2", iduser2);
            // Tạo dữ liệu tin nhắn mới
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            String currentTime = sdf.format(new Date());
            msgRef.child(messageID).child("uid").child("0").setValue(iduser1);
            msgRef.child(messageID).child("uid").child("1").setValue(iduser2);

            // Chuyển sang màn hình ChatActivity
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("uid", iduser2);
            intent.putExtra("userid", iduser1);
            intent.putExtra("id", messageID);

            startActivity(intent);
        }
    }


}