package com.example.android_social_media.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_social_media.R;
import com.example.android_social_media.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ImageViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private FirebaseUser firebaseUser;
    private boolean isFragment;

    public UserAdapter(Context context, List<User> users, boolean isFragment ) {
        mContext = context;
        mUsers = users;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public UserAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ImageViewHolder holder, final int position) {

        final User user = mUsers.get(position);

        holder.btn_follow.setVisibility(View.VISIBLE);
        isFollowing(user.getUserID(), holder.btn_follow);

        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getName());

        String profileImage = user.getProfileImage();

        if (profileImage.equals("")) {
            Glide.with(mContext).load(R.drawable.ic_profile).into(holder.image_profile);
        } else {
            Glide.with(mContext).load(profileImage).into(holder.image_profile);
        }


        if (user.getUserID().equals(firebaseUser.getUid())){
            holder.btn_follow.setVisibility(View.GONE);
        }

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String currentUserID = firebaseUser.getUid();
                final String targetUserID = user.getUserID();

                DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(currentUserID).child("following");

                DatabaseReference followerRef = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(targetUserID).child("follower");

                followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isFollowing = false;
                        for (DataSnapshot followingChild : dataSnapshot.getChildren()) {
                            String followingID = followingChild.getValue(String.class);
                            if (followingID.equals(targetUserID)) {
                                isFollowing = true;
                                followingChild.getRef().removeValue();
                                // Remove from follower list of target user
                                followerRef.orderByValue().equalTo(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            childSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("UserAdapter", "Error removing follower: " + error.getMessage());
                                    }
                                });
                                break;
                            }
                        }
                        if (!isFollowing) {
                            //thông báo cho người kia là mình follow người ta
                            addNotification(targetUserID);
                            followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // Kiểm tra xem có dữ liệu nào không
                                    if (snapshot.exists()) {
                                        // Lặp qua tất cả các child của snapshot để tìm node cuối cùng
                                        DataSnapshot lastChild = null;
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            lastChild = childSnapshot;
                                        }

                                        // Kiểm tra xem lastChild đã được tìm thấy chưa
                                        if (lastChild != null) {
                                            // Lấy key của node cuối cùng
                                            String lastKey = lastChild.getKey();
                                            // Thực hiện thêm follower với key là lastKey + 1 (chuyển key sang kiểu số nguyên trước khi tăng)
                                            long newKey = Long.parseLong(lastKey) + 1;
                                            followingRef.child(String.valueOf(newKey)).setValue(targetUserID);
                                        } else {
                                            // Trường hợp không có child nào trong snapshot
                                            // Có thể làm gì đó ở đây nếu cần
                                        }
                                    } else {
                                        // Trường hợp không có dữ liệu trong snapshot
                                        // Có thể làm gì đó ở đây nếu cần
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("UserAdapter", "Error adding follower: " + error.getMessage());
                                }
                            });

                            followerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // Kiểm tra xem có dữ liệu nào không
                                    if (snapshot.exists()) {
                                        // Lặp qua tất cả các child của snapshot để tìm node cuối cùng
                                        DataSnapshot lastChild = null;
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            lastChild = childSnapshot;
                                        }

                                        // Kiểm tra xem lastChild đã được tìm thấy chưa
                                        if (lastChild != null) {
                                            // Lấy key của node cuối cùng
                                            String lastKey = lastChild.getKey();
                                            // Thực hiện thêm follower với key là lastKey + 1 (chuyển key sang kiểu số nguyên trước khi tăng)
                                            long newKey = Long.parseLong(lastKey) + 1;
                                            followerRef.child(String.valueOf(newKey)).setValue(currentUserID);
                                        } else {
                                            // Trường hợp không có child nào trong snapshot
                                            // Có thể làm gì đó ở đây nếu cần
                                        }
                                    } else {
                                        // Trường hợp không có dữ liệu trong snapshot
                                        // Có thể làm gì đó ở đây nếu cần
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("UserAdapter", "Error adding follower: " + error.getMessage());
                                }
                            });


//                            followerRef.child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(currentUserID);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("UserAdapter", "Follow/Unfollow operation cancelled: " + databaseError.getMessage());
                    }
                });
            }
        });

    }

    private void isFollowing(final String userId, final Button button) {
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(firebaseUser.getUid()).child("following");
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isFollowing = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String followingId = snapshot.getValue(String.class);
                    if (followingId != null && followingId.equals(userId)) {
                        isFollowing = true;
                        break;
                    }
                }
                if (isFollowing) {
                    button.setText("Đang theo dõi");
                } else {
                    button.setText("Theo dõi");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("UserAdapter", "isFollowing operation cancelled: " + databaseError.getMessage());
            }
        });
    }

    private void addNotification(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", firebaseUser.getUid());
        hashMap.put("text", " bắt đầu theo dõi bạn.");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView fullname;
        CircleImageView image_profile;
        Button btn_follow;

        public ImageViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            image_profile = itemView.findViewById(R.id.image_profile);
            btn_follow = itemView.findViewById(R.id.btn_follow);
        }
    }
}
