package com.example.android_social_media.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;
import com.example.android_social_media.model.chatUserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatuserAdapter extends RecyclerView.Adapter<chatuserAdapter.ChatUserHolder> {
    Activity context;
    List<chatUserModel> list;

    public chatuserAdapter(Activity context, List<chatUserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_item,parent, false);

        return new ChatUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserHolder holder, int position) {
        //fetchImageUrl(list.get(position));
       // Glide.with(context.getApplicationContext()).load();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ChatUserHolder extends RecyclerView.ViewHolder {
        CircleImageView  imageView;
        TextView name,status,time,count;

        public ChatUserHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImage);
            status = itemView.findViewById(R.id.txtStatus);
            time = itemView.findViewById(R.id.timeTV);
            count = itemView.findViewById(R.id.messageCountTV);
            name = itemView.findViewById(R.id.nameTV);

        }
    }
}
