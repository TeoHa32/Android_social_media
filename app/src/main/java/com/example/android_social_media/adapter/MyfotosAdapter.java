package com.example.android_social_media.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_social_media.R;

public class MyfotosAdapter {
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView post_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_image = itemView.findViewById(R.id.post_image);
        }
    }
}
