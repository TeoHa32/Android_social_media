package com.example.android_social_media.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_social_media.CommentActivity;
import com.example.android_social_media.R;
import com.example.android_social_media.fragment.PostDetailFragment;
import com.example.android_social_media.fragment.profile;
import com.example.android_social_media.fragment.profileFragment;
import com.example.android_social_media.model.User;
import com.example.android_social_media.model.post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        String a = firebaseUser.getUid();
        Log.d("id ne",firebaseUser.getUid());
        post post = mPost.get(position);
        Log.d("tao lao", String.valueOf(post.getPostImage()));
        if (post != null && post.getPostImage() != null) {
            // Load ảnh với Glide
            Glide.with(mcontext).load(post.getPostImage()).into(holder.post_image);
        }
        //Glide.with(mcontext).load(post.getPostImage()).into(holder.post_image);
        if(post.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }
        else{
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());

        }

        holder.datetime.setText(post.getDatetime());


        publisherInfo(holder.image_profile, holder.username, holder.publisher, post.getPublisher());
        isSave(post.getPostId(),  holder.save);
        isLike(post.getPostId(), holder.like);
        getComment(post.getPostId(), holder.comments);
        nrLikes(holder.likes, post.getPostId());

        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mcontext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getPublisher());
                editor.apply();

                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profileFragment()).commit();

            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mcontext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getPublisher());
                editor.apply();

                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profileFragment()).commit();

            }
        });

        holder.publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mcontext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getPublisher());
                editor.apply();

                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profileFragment()).commit();

            }
        });

        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mcontext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",post.getPostId());
                editor.apply();

                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostDetailFragment()).commit();

            }
        });


        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mcontext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getPublisher());
                editor.apply();

                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profile()).commit();

            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mcontext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getPublisher());
                editor.apply();

                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profile()).commit();

            }
        });

        holder.publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mcontext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getPublisher());
                editor.apply();

                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profile()).commit();

            }
        });

        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mcontext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",post.getPostId());
                editor.apply();

                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostDetailFragment()).commit();

            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.save.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostId()).setValue(true);
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostId()).removeValue();
                }
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Posts").child(post.getPostId());
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                post p = snapshot.getValue(post.class);
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child(p.getPublisher());
                                reference1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            User u = snapshot.getValue(User.class);
                                            Log.d("token like", u.getToken());
                                            sendNotification(u.getToken());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    addNotifications(post.getPublisher(), post.getPostId());

                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("comment", "onClick:1 ");
                Intent intent = new Intent(mcontext, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("publisherId", post.getPublisher());
                Log.d("commentButton", post.getPublisher());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("publisherId", post.getPublisher());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mPost.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile, post_image, comment , save,like;
        public TextView username, likes, publisher, description, comments, datetime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.username);
            like = itemView.findViewById(R.id.like);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.description);
            comments = itemView.findViewById(R.id.comments);
            datetime=itemView.findViewById(R.id.datetime);


        }
    }
    private void getComment(String postId, TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.setText(snapshot.getChildrenCount()+" Bình luận");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void isSave(String postId, ImageView imageview){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saves")
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postId).exists()){
                    imageview.setImageResource(R.drawable.ic_saved);
                    imageview.setTag("saved");
                }
                else{
                    imageview.setImageResource(R.drawable.ic_save);
                    imageview.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLike(String postId, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_like_red);
                    imageView.setTag("liked");
                }
                else{
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void nrLikes(TextView likes, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+" Lượt thích");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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

    private void addNotifications(String userId, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userId);

        if (!userId.equals(firebaseUser.getUid())) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userId", firebaseUser.getUid());
            hashMap.put("text", " đã thích bài viết của bạn!");
            hashMap.put("postId", postId);
            hashMap.put("isPost", "true");

            reference.push().setValue(hashMap);
        }
    }
    void sendNotification(String token) {
        Log.d("TAGcomment", token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User u = snapshot.getValue(User.class);
                    try{
                        JSONObject jsonObject = new JSONObject();
                        JSONObject notificationObject = new JSONObject();
                        notificationObject.put("title","Thông báo");
                        notificationObject.put("body",u.getName()+" đã thích bài viết của bạn");
                        JSONObject dataObject = new JSONObject();
                        dataObject.put("userId",firebaseUser.getUid());
                        jsonObject.put("notification",notificationObject);
                        jsonObject.put("data",dataObject);

//                        jsonObject.put("to","c0cJxYMlTI2Cuy8sopKfsp:APA91bFUL4diRMuMbgHLtP2IODs9Za9P_IWwJJTKjG7eqB8ecQQxA4meA1Wm4DcFuYmnm_PtbI4qwO6b2H8nop2er3d_vqkS4bomeiTtk0og1rP21QTVqZMUyIeo_pFO17KxUGmf65ti");
                        jsonObject.put("to",token);
                        callApi(jsonObject);
                    }catch (Exception e){
                        System.out.println(e);
                    }

//                return 0;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("loadUser", "Không thể đọc được dữ liệu.", error.toException());
                // Handle error
            }
        });

    }
    void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer AAAAUim2Eso:APA91bGDcN23BvYdB7gr5KyePONZDmenUFi1qk8VidnQmc5ENhAnsWhBzEjqYxBFtZu0QgdPr7m9zCiP1Rh9pVMQaAP4AYvG4sEJ8V0fbrRAGCUcEec8QVCwUYiOmvb3-AJvoifv3qQq")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("Thất bại", "thất bại");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Đã nhận phản hồi thành công từ API
                    String responseBody = response.body().string();
                    Log.d("thành công 1", responseBody);
                } else {
                    // Gọi API không thành công

                }Log.d("thành công", "onResponse: không thành công");
            }
        });
    }

}
