package com.example.android_social_media.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_social_media.R;
import com.example.android_social_media.model.SearchImageModel;
import com.example.android_social_media.model.SearchUser;
import com.example.android_social_media.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.units.qual.C;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchImageAdapter extends  RecyclerView.Adapter<SearchImageAdapter.SearchImageHolder>{



    private List<SearchImageModel> list;

    public SearchImageAdapter(List<SearchImageModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SearchImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_image_item, parent,false);
        return new SearchImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchImageHolder holder, int position) {
        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getPostImage())
                .timeout(6500)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SearchImageHolder extends RecyclerView.ViewHolder{


        private ImageView imageView;
        private TextView id;
        public SearchImageHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
