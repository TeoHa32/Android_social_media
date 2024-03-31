package com.example.android_social_media;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class postAdapter  extends  RecyclerView.Adapter<postAdapter.PostViewHolder>{
    private Context mcontext;
    private List<post> listPost;
    public void setData(List<post> list){
        this.listPost=list;
        notifyDataSetChanged();
    }
    public postAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        post    post = listPost.get(position);
        if(post == null) return ;
        holder.imgUser.setImageResource(post.getId_userImg_post());
        holder.tvName.setText(post.getName());
        holder.imgPost.setImageResource(post.getId_image_post());
        holder.heart.setImageResource(post.getId_heart());
        holder.comment.setImageResource(post.getId_comment());
        holder.share.setImageResource(post.getId_share());
    }

    @Override
    public int getItemCount() {
        if(listPost !=null) return listPost.size();
        return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgUser;
        private TextView tvName;
        private ImageView imgPost;
        private ImageView heart;
        private ImageView comment;
        private ImageView share;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
        imgUser = itemView.findViewById(R.id.img_user_post);
        tvName = itemView.findViewById(R.id.tv_name_post);
        imgPost = itemView.findViewById(R.id.image_post);
        heart = itemView.findViewById(R.id.heart);
        comment = itemView.findViewById(R.id.comment);
        share = itemView.findViewById(R.id.share);
        }
    }
}
