package com.example.android_social_media.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_social_media.R;
import com.example.android_social_media.fragment.profile;
import com.example.android_social_media.model.SearchUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends  RecyclerView.Adapter<SearchAdapter.UserHolder>{

    private List<SearchUser> list;
    private OnUserClicked listener;
    public SearchAdapter(List<SearchUser> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameTV.setText(list.get(position).getName());
        holder.usernameTV.setText(list.get(position).getUsername());

        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_profile)
                .timeout(6500)
                .into(holder.profileImage);
        holder.layoutItemUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(view, list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void OnUserClicked (OnUserClicked listener){
        this.listener = listener;
    }

    public interface OnUserClicked {
        void onItemClicked(View view, SearchUser searchUser);
    }

     static class UserHolder extends RecyclerView.ViewHolder{
        private CircleImageView profileImage;
        private TextView nameTV, usernameTV;
        private RelativeLayout layoutItemUser;
        public UserHolder(View itemView){
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            nameTV = itemView.findViewById(R.id.nameTV);
            usernameTV = itemView.findViewById(R.id.usernameTV);
            layoutItemUser = itemView.findViewById(R.id.layoutItemUser);

        }

    }

}
