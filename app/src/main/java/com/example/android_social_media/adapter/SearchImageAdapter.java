package com.example.android_social_media.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.example.android_social_media.model.SearchImageModel;
import com.example.android_social_media.model.SearchUser;
import com.example.android_social_media.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import org.checkerframework.checker.units.qual.C;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchImageAdapter extends  RecyclerView.Adapter<SearchImageAdapter.SearchImageHolder>{


    private Context mContext;

    private List<SearchImageModel> list;

    public SearchImageAdapter(Context context, List<SearchImageModel> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_image_item, parent,false);
        return new SearchImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchImageHolder holder, int position) {
        final SearchImageModel search = list.get(position);

        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getPostImage())
                .timeout(6500)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("postid", search.getPostId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostDetailFragment()).commit();
            }
        });

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
