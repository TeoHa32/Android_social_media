package com.example.android_social_media.adapter;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;
import com.example.android_social_media.model.chatUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatuserAdapter extends RecyclerView.Adapter<chatuserAdapter.ChatUserHolder> {
    Activity context;
    List<chatUserModel> list;
    public OnStartChat startChat;
    FirebaseUser user;

    public chatuserAdapter() {
    }

    public chatuserAdapter(Activity context, List<chatUserModel> list) {
        this.context = context;
        this.list = list;
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }
    @NonNull
    @Override
    public ChatUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_item,parent, false);
        return new ChatUserHolder(view);
    }
<<<<<<< HEAD
=======

    private String timestampToString(long timestamp) {
        if (timestamp == 0) {
            return ""; // hoặc một giá trị mặc định khác nếu cần
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(timestamp);
        String dateString = dateFormat.format(date);
        return dateString;
    }

>>>>>>> a8eda07d6cb67d8a57616bca177c05e4d2c4c047
    @Override
    public void onBindViewHolder(@NonNull ChatUserHolder holder, int position) {
        String timestampString = list.get(position).getTime(); // Lấy thời gian dưới dạng String
        try {
            long timestamp = Long.parseLong(timestampString); // Chuyển đổi từ chuỗi sang timestamp
            holder.time.setText(timestampToString(timestamp));
        } catch (NumberFormatException e) {
            Log.e("NumberFormatException", "Cannot parse timestamp: " + timestampString);
        }
        holder.lastMessage.setText(list.get(position).getLastMessage());

        holder.itemView.setOnClickListener(v -> {
            startChat.clicked(position,  list.get(position).getUid(), list.get(position).getId());
        });
        fetchMessages(holder);
    }


//    void fetchMessages(ChatUserHolder holder) {
//        Log.d("Current Account: ", user.getUid());
//        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
//
//        messagesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
//                        String messageId = messageSnapshot.getKey();
//                        Log.d("Message ID", messageId);
//
//                        messagesRef.child(messageId).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    String idValue = dataSnapshot.child("id").getValue(String.class);
//                                    Log.d("id value is: ", idValue);
//
//                                    if (idValue.equals(user.getUid())) {
//                                        String senderID = dataSnapshot.child("message").child("senderID").getValue(String.class);
//                                        Log.d("senderID của người gửi là: ", senderID);
//
//                                        userRef.child(senderID).addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
//                                                if (userSnapshot.exists()) {
//                                                    String name = userSnapshot.child("name").getValue(String.class);
//                                                    String profileImage = userSnapshot.child("profileImage").getValue(String.class);
//                                                    Log.d("Tên người gửi: ", name);
//                                                    Log.d("avt của người gửi: ", profileImage);
//                                                    holder.name.setText(name);
//                                                    Picasso.get().load(profileImage).into(holder.imageView);
//                                                } else {
//                                                    Log.d("Firebase", "User not found");
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//                                                Log.e("Firebase", "Failed to read user data.", error.toException());
//                                            }
//                                        });
//                                    }
//                                } else {
//                                    Log.d("Firebase", "Node not found");
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                Log.e("Firebase", "Error fetching message", databaseError.toException());
//                            }
//                        });
//                    }
//                } else {
//                    Log.d("FirebaseError", "No data available");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("FirebaseError", "Failed to read value.", databaseError.toException());
//            }
//        });
//    }

    void fetchMessages(ChatUserHolder holder) {
        Log.d("Current Account: ", user.getUid());
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");

        boolean[] shouldContinueProcessing = {false}; // Biến mảng một phần tử để giữ giá trị của biến cục bộ

        messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        if (shouldContinueProcessing[0]) {
                            return; // Thoát khỏi vòng lặp nếu biến cờ là false
                        }

                        String messageId = messageSnapshot.getKey();
                        Log.d("Message ID", messageId);

                        DatabaseReference messageRef_child = messagesRef.child(messageId);
                        messageRef_child.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String idValue = dataSnapshot.child("id").getValue(String.class);
                                    Log.d("id value is: ", idValue);

                                    if (idValue.equals(user.getUid())) {
                                        shouldContinueProcessing[0] = true;
                                        String senderID = dataSnapshot.child("message").child("senderID").getValue(String.class);
                                        Log.d("senderID của người gửi là: ", senderID);

                                        if (senderID != null) {
                                            userRef.child(senderID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                                    if (userSnapshot.exists()) {
                                                        String userid = userSnapshot.child("UserID").getValue(String.class);
                                                        if (senderID.equals(userid)) {
                                                            String name = userSnapshot.child("name").getValue(String.class);
                                                            String profileImage = userSnapshot.child("profileImage").getValue(String.class);
                                                            Log.d("Tên người gửi: ", name);
                                                            Log.d("avt của người gửi: ", profileImage);
                                                            holder.name.setText(name);
                                                            Picasso.get().load(profileImage).into(holder.imageView);
                                                        }
                                                    } else {
                                                        Log.d("Firebase", "User not found");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.e("Firebase", "Failed to read user data.", error.toException());
                                                }
                                            });
                                        }
                                    }
                                    else {
                                        shouldContinueProcessing[0] = false; // Đặt biến cờ thành false nếu điều kiện không đúng
                                    }
                                } else {
                                    Log.d("Firebase", "Node not found");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Firebase", "Error fetching message", databaseError.toException());
                            }
                        });
                    }
                } else {
                    Log.d("FirebaseError", "No data available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ChatUserHolder extends RecyclerView.ViewHolder {
        CircleImageView  imageView;
        TextView name,lastMessage,time,count;

        public ChatUserHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImage);
            lastMessage = itemView.findViewById(R.id.messageTV);
            time = itemView.findViewById(R.id.timeTV);
            count = itemView.findViewById(R.id.messageCountTV);
            name = itemView.findViewById(R.id.nameTV);
        }
    }

    public interface OnStartChat{
        void clicked(int position, List<String> uids, String chatID);
    }

    public void OnStartChat(OnStartChat startChat){
        this.startChat = startChat;
    }

}
