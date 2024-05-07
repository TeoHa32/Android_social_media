package com.example.android_social_media.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_social_media.R;
import com.example.android_social_media.fragment.PostDetailFragment;
import com.example.android_social_media.fragment.profileFragment;
import com.example.android_social_media.model.NotificationModel;
import com.example.android_social_media.model.User;
import com.example.android_social_media.model.post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ImageViewHolder> {

    String key ="";

    private Context mContext;
    private List<NotificationModel> list;

    public NotificationAdapter(Context context, List<NotificationModel> list){
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.ImageViewHolder holder, final int position) {

        final NotificationModel notification = list.get(position);

        holder.text.setText(notification.getText());

        getUserInfo(holder.image_profile, holder.username, notification.getUserId());

        String isPost = notification.getIsPost();
        if (isPost != null) {
            if (isPost.equals("true")) {
                holder.post_image.setVisibility(View.VISIBLE);
                getPostImage(holder.post_image, notification.getPostId());
            } else if (isPost.equals("false")) {
                holder.post_image.setVisibility(View.GONE);
            } else {
                holder.post_image.setVisibility(View.GONE);
            }
        } else {
            // Xử lý trường hợp notification.getIsPost() trả về null
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notification.getIsPost().equals("true")) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("postId", notification.getPostId());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new PostDetailFragment()).commit();
                } else {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference usersRef = database.getReference("users").child(notification.getUserId());
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Lấy thông tin username và img từ dataSnapshot
                                String username = dataSnapshot.child("username").getValue(String.class);
                                String img = dataSnapshot.child("profileImage").getValue(String.class);

                                // Truyền thông tin qua Bundle
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                bundle.putString("img", img);
                                bundle.putString("key", key);

                                // Tạo fragment mới và truyền Bundle vào
                                profileFragment fragment = new profileFragment();
                                fragment.setArguments(bundle);

                                // Thay thế fragment hiện tại bằng fragment mới
                                FragmentTransaction fragmentTransaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, fragment);
                                fragmentTransaction.commit();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý lỗi nếu cần thiết
                        }
                    });

//                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
//                    editor.putString("userId", notification.getUserId());
//                    editor.apply();
//                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            new profileFragment()).commit();
                }
            }
        });
    }
    //
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile, post_image;
        public TextView username, text;

        public ImageViewHolder(View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.comment);
        }
    }

    private void getUserInfo(final ImageView imageView, final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(publisherid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getProfileImage()).placeholder(R.drawable.ic_profile).into(imageView);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPostImage(final ImageView post_image, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(postId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post Post = dataSnapshot.getValue(post.class);
                if (Post != null) {
                    Glide.with(mContext).load(Post.getPostImage()).into(post_image);
                } else {
                    // Handle the case where the post object is null
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}