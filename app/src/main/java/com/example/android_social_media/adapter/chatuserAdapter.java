package com.example.android_social_media.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;
import com.example.android_social_media.chat.ChatActivity;
import com.example.android_social_media.model.chatUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ChatUserHolder holder, int position) {
        fetchImageUrl(list.get(position).getUid(), holder);

        holder.time.setText(calculateTime(list.get(position).getTime()));

        holder.lastMessage.setText(list.get(position).getLastMessage());

        holder.itemView.setOnClickListener(v -> {
            if (startChat != null) {
                startChat.clicked(position, list.get(position).getUid(), list.get(position).getId());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String calculateTime(String timeString) {
        if (timeString != null && !timeString.isEmpty()) {
            try {
                // Định dạng đối tượng DateTimeFormatter phù hợp với chuỗi thời gian
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                LocalDateTime dateTime = LocalDateTime.parse(timeString, formatter);

                // Tính toán khoảng thời gian giữa thời điểm nhắn tin và thời điểm hiện tại
                LocalDateTime currentTime = LocalDateTime.now();
                Duration duration = Duration.between(dateTime, currentTime);

                // Chuyển đổi khoảng thời gian thành dạng "Just now", "X minutes ago",...
                long seconds = duration.getSeconds();
                if (seconds < 60) {
                    return "Just now";
                } else if (seconds < 3600) {
                    long minutes = seconds / 60;
                    return minutes + " minutes ago";
                } else if (seconds < 86400) {
                    long hours = seconds / 3600;
                    return hours + " hours ago";
                } else {
                    long days = seconds / 86400;
                    return days + " days ago";
                }
            } catch (Exception e) {
                Log.e("DateTimeParseException", "Error parsing time: " + e.getMessage());
                return "Unknown Time";
            }
        } else {
            return "Unknown Time";
        }
    }

    void fetchImageUrl(List<String> uids, ChatUserHolder holder) {
        if (uids == null || uids.size() < 2) {
            Log.e("fetchImageUrl", "list UID rỗng!");
            return;
        }

        String oppositeUID;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if (!uids.get(0).equalsIgnoreCase(user.getUid())) {
            oppositeUID = uids.get(0);
        } else {
            oppositeUID = uids.get(1);
        }

        Log.d("Opposite_ID: ", oppositeUID);

        if (oppositeUID == null) {
            Log.e("fetchImageUrl", "UID Opposite null!");
            return;
        }

        FirebaseDatabase.getInstance().getReference().child("users").child(oppositeUID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String imageUrl = dataSnapshot.child("profileImage").getValue(String.class);
                            String name = dataSnapshot.child("name").getValue(String.class);

                            if (imageUrl != null) {
                                Picasso.get().load(imageUrl).into(holder.imageView);
                            }
                            if (name != null) {
                                holder.name.setText(name);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
//            count = itemView.findViewById(R.id.messageCountTV);
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
