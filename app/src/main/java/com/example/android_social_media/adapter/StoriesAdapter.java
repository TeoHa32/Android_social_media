package com.example.android_social_media.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_social_media.R;
import com.example.android_social_media.StoryActivity;
import com.example.android_social_media.StoryAddActivity;
import com.example.android_social_media.model.StoriesModel;
import com.example.android_social_media.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesHolder> {

    private Context mContext;
    private List<StoriesModel> mStory;


    public StoriesAdapter(Context mContext, List<StoriesModel> mStory) {
        this.mContext = mContext;
        this.mStory = mStory;
    }

    @NonNull
    @Override
    public StoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0){
            View view = LayoutInflater.from(mContext).inflate(R.layout.add_story_item, parent, false);
            return new StoriesAdapter.StoriesHolder(view);
        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.story_item, parent, false);
            return new StoriesAdapter.StoriesHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesHolder holder, int position) {
        StoriesModel story = mStory.get(position);
        userInfo(holder, story.getUserId(),position);
        if(holder.getAdapterPosition() != 0){
            seenStory(holder, story.getUserId());
        }
        if(holder.getAdapterPosition() == 0){
            myStory(holder.add_story_text, holder.story_plus, false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition() == 0){
                    myStory(holder.add_story_text, holder.story_plus, true);
                }
                else{
                    Intent intent = new Intent(mContext, StoryActivity.class);
                    intent.putExtra("userid", story.getUserId());
                    mContext.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mStory.size();
    }

    static class  StoriesHolder extends RecyclerView.ViewHolder{

        public ImageView story_photo, story_plus, story_photo_seen;
        public TextView story_username, add_story_text;
        public StoriesHolder(@NonNull View itemView) {
            super(itemView);

            story_photo = itemView.findViewById(R.id.story_photo);
            story_plus = itemView.findViewById(R.id.story_plus);
            story_photo_seen = itemView.findViewById(R.id.story_photo_seen);
            story_username = itemView.findViewById(R.id.story_username);
            add_story_text = itemView.findViewById(R.id.add_story_text);

        }
    }

    public int getItemViewType(int position){
        if(position == 0)
            return 0;
        return position;
    }

    private void userInfo(StoriesHolder viewHolder,String userid, int pos){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                Glide.with(mContext).load(user.getProfileImage()).into(viewHolder.story_photo);
                if(pos != 0){
                    Glide.with(mContext).load(user.getProfileImage()).into(viewHolder.story_photo_seen);
                    viewHolder.story_username.setText(user.getUsername());
                }
                return 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void myStory(TextView textView, ImageView imageView, boolean click){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int count = 0;
                long currentTime = System.currentTimeMillis();
                Log.d("Current Time:", String.valueOf(currentTime));
                for(DataSnapshot data : snapshot.getChildren()){

                    String startTime = String.valueOf(data.child("startTime").getValue());
                    String endTime = String.valueOf(data.child("endTime").getValue());

                    if (startTime!= null && endTime != null) {

                        if (currentTime > Long.parseLong(startTime) && currentTime < Long.parseLong(endTime)) {
                            count++;
                        }

                    }
                }
                if(click){
                    // Xử lý sự kiện khi click
                    if(count > 0){
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Xem tin",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mContext, StoryActivity.class);
                                        intent.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        mContext.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Thêm tin",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mContext, StoryAddActivity.class);
                                        mContext.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else{
                        Intent intent = new Intent(mContext, StoryAddActivity.class);
                        mContext.startActivity(intent);
                    }
                }
                else{
                    if(count > 0){
                        textView.setText("Tin của tôi");
                        imageView.setVisibility(View.GONE);
                    }
                    else{
                        textView.setText("Thêm tin");
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
                return currentTime;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }

    private void seenStory(StoriesHolder viewHolder, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for(DataSnapshot data : snapshot.getChildren()){
                    if( !data.child("views")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .exists() && System.currentTimeMillis() < data.getValue(StoriesModel.class).getEndTime()){
                        i++;
                    }
                }

                if(i > 0){
                    viewHolder.story_photo.setVisibility(View.VISIBLE);
                    viewHolder.story_photo_seen.setVisibility(View.GONE);
                }
                else{
                    viewHolder.story_photo.setVisibility(View.GONE);
                    viewHolder.story_photo_seen.setVisibility(View.VISIBLE);
                }
                return 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
