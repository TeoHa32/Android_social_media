package com.example.android_social_media.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ImageViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean isFragment;
    private FirebaseUser firebaseUser;


    public UserAdapter(Context context, List<User> users, boolean isFragment) {
        mContext = context;
        mUsers = users;
        this.isFragment = isFragment;
    }


    @NonNull
    @Override
    public UserAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ImageViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);

        holder.btn_follow.setVisibility(View.VISIBLE);
        isFollowing(user.getUserID(), holder.btn_follow);

        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getName());
        Glide.with(mContext).load(user.getProfileImage()).into(holder.image_profile);

        if (user.getUserID().equals(firebaseUser.getUid())){
            holder.btn_follow.setVisibility(View.GONE);
        }

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String currentUserID = firebaseUser.getUid();
                final String targetUserID = user.getUserID();

                // Kiểm tra xem người dùng hiện tại đã theo dõi người dùng đích hay chưa
                DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(currentUserID).child("following");
                followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot followingChild : dataSnapshot.getChildren()) {
                                String followingID = followingChild.getValue(String.class);
                                if (followingID.equals(targetUserID)) {
                                    // Người dùng hiện tại đã theo dõi người dùng đích, do đó hủy theo dõi
                                    unfollowUser(currentUserID, targetUserID);
                                } else {
                                    // Người dùng hiện tại chưa theo dõi người dùng đích, do đó theo dõi
                                    followUser(currentUserID, targetUserID);
                                }
                            }

                        } else {
                            // Trường following không tồn tại, do đó người dùng chưa theo dõi bất kỳ ai
                            // Thêm người dùng đích vào danh sách theo dõi
                            followUser(currentUserID, targetUserID);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
            }
        });
    }


    private void followUser(String currentUserID, String targetUserID) {

        // Thêm người dùng đích vào danh sách following của người dùng hiện tại
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUserID).child("following");

        // Tính toán số thứ tự tiếp theo trong danh sách following
        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long followingCount = dataSnapshot.getChildrenCount();

                // Thêm người dùng đích vào danh sách following của người dùng hiện tại
                followingRef.child(String.valueOf(followingCount)).setValue(targetUserID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

        // Thêm người dùng hiện tại vào danh sách follower của người dùng đích
        DatabaseReference followerRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(targetUserID).child("follower");

        // Tính toán số thứ tự tiếp theo trong danh sách following
        followerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long followerCount = dataSnapshot.getChildrenCount();

                // Thêm người dùng đích vào danh sách following của người dùng hiện tại
                followerRef.child(String.valueOf(followerCount)).setValue(currentUserID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

        // Thêm thông báo
//        addNotification(targetUserID);
    }

    private void unfollowUser(String currentUserID, String targetUserID) {
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUserID).child("following");

        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String followingId = snapshot.getValue(String.class);
                    if (followingId != null && followingId.equals(targetUserID)) {
                        snapshot.getRef().removeValue();
                        break; // Thoát khỏi vòng lặp sau khi xóa thành công
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

        // Xóa người dùng hiện tại khỏi danh sách followers của người dùng đích
        DatabaseReference followerRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(targetUserID).child("follower");

        followerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String followingId = snapshot.getValue(String.class);
                    if (followingId != null && followingId.equals(currentUserID)) {
                        snapshot.getRef().removeValue();
                        break; // Thoát khỏi vòng lặp sau khi xóa thành công
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }


//    private void addNotification(String userid){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("userid", firebaseUser.getUid());
//        hashMap.put("text", "started following you");
//        hashMap.put("postid", "");
//        hashMap.put("ispost", false);
//
//        reference.push().setValue(hashMap);
//    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView fullname;
        public CircleImageView image_profile;
        public Button btn_follow;
        public View viewToHide;

        public ImageViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            image_profile = itemView.findViewById(R.id.image_profile);
            btn_follow = itemView.findViewById(R.id.btn_follow);
            viewToHide = itemView.findViewById(R.id.fragment_container);
        }
    }

    private void isFollowing(final String userid, final Button button){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> followingList = dataSnapshot.getChildren();
                boolean isFollowing = false;
                for (DataSnapshot snapshot : followingList) {
                    String followingId = snapshot.getValue(String.class);
                    if (followingId != null && followingId.equals(userid)) {
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
                // Xử lý khi có lỗi xảy ra
            }
        });
    }


}
