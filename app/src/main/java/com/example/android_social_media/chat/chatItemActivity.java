
//package com.example.android_social_media.chat;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.android_social_media.R;
//import com.example.android_social_media.adapter.chatuserAdapter;
//import com.example.android_social_media.model.chatUserModel;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class chatItemActivity extends AppCompatActivity {
//    chatuserAdapter adapter;
//    List<chatUserModel> list;
//    FirebaseUser user;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_chat_users);
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
//        init();
//        fetchUserData();
//        clickListenner();
//    }
//    void init(){
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        list = new ArrayList<>();
//        adapter = new chatuserAdapter(this, list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//        user = FirebaseAuth.getInstance().getCurrentUser();
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    void  fetchUserData(){
//        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");
//        reference.whereArrayContains("uid", user.getUid())
//                .addSnapshotListener((value, error) -> {
//                    if(error != null) return;
//                    if(value.isEmpty()) return;
//                    for(QueryDocumentSnapshot snapshot : value){
//                        chatUserModel model = snapshot.toObject(chatUserModel.class);
//                        list.add(model);
//                    }
//                    adapter.notifyDataSetChanged();
//
//
//                });
//    }
//    void clickListenner(){
//        adapter.OnStartChat(new chatuserAdapter.OnStartChat() {
//            @Override
//            public void clicked(int position, List<String> uids) {
//                String oppositeUID;
//                if(uids.get(0).equalsIgnoreCase(user.getUid())){
//                    oppositeUID = uids.get(0);
//                }
//                else {
//                    oppositeUID = uids.get(1);
//                }
//                Intent intent = new Intent(chatItemActivity.this, ChatActivity.class);
//                intent.putExtra("uid",oppositeUID);
//                startActivity(intent);
//
//            }
//        });
//
//    }
//}

package com.example.android_social_media.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;
import com.example.android_social_media.adapter.chatuserAdapter;
import com.example.android_social_media.model.chatUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class chatItemActivity extends AppCompatActivity {
    chatuserAdapter adapter;
    List<chatUserModel> list;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_users);
        init();
        fetchUserData();
    }
    void init(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new chatuserAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @SuppressLint("NotifyDataSetChanged")
    void  fetchUserData(){
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");
        reference.whereArrayContains("uid", user.getUid())
                .addSnapshotListener((value, error) -> {
                    if(error != null) return;
                    if(value.isEmpty()) return;
                    for(QueryDocumentSnapshot snapshot : value){
                        chatUserModel model = snapshot.toObject(chatUserModel.class);
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();

                });
    }
}
