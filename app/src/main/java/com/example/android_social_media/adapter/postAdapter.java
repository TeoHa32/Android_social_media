package com.example.android_social_media.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_social_media.R;
import com.example.android_social_media.model.User;
import com.example.android_social_media.model.post;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class postAdapter  extends RecyclerView.Adapter<postAdapter.ViewHolder>{
    public Context mcontext;
    public List<post> mPost;
    private FirebaseUser firebaseUser;

    public postAdapter(Context mcontext, List<post> mPost) {
        this.mcontext = mcontext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_post,parent, false);
        return new postAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        post post = mPost.get(position);
        Glide.with(mcontext).load(post.getPostImage()).into(holder.post_image);
        if(post.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }
        else{
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());

        }

        publisherInfo(holder.image_profile, holder.username, holder.publisher, post.getPublisher());
    }
    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile, post_image, comment , save;
        public TextView username, likes, publisher, description, comments;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.share);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.description);
            comments = itemView.findViewById(R.id.comments);
        }
    }
    private void publisherInfo(ImageView image_profile, TextView username, TextView publisher, String userId){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(mcontext).load(user.getProfileImage()).into(image_profile);
                username.setText(user.getUsername());
                publisher.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
