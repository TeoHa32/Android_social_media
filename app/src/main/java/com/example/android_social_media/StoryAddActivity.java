package com.example.android_social_media;

import android.app.Activity;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gowtham.library.utils.CompressOption;
import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimType;
import com.gowtham.library.utils.TrimVideo;

import java.util.HashMap;
import java.util.Map;

public class StoryAddActivity extends AppCompatActivity {

    VideoView videoView;
    private static final int SELECT_VIDEO = 101;

    FirebaseUser user;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK &&
                    result.getData() != null) {
                Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));
                videoView.setVideoURI(uri);
                videoView.start();

//                uploadVideoToStorage(uri);

            } else{
                Toast.makeText(this, "Data is null!", Toast.LENGTH_SHORT).show();
                finish();
            }

        });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story_add);

        init();

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, SELECT_VIDEO);
    }

    void uploadVideoToStorage(Uri uri){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Stories");
        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){

                    assert task.getResult() != null;
                    task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uploadVideoDataIntoDatabase(String.valueOf(uri));
                        }
                    });
                }
                else{
                    String error = task.getException().getMessage();
                    Toast.makeText(StoryAddActivity.this,"Error: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void uploadVideoDataIntoDatabase(String url){

        // Lấy tham chiếu đến Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Tạo một id mới cho dữ liệu trong Realtime Database
        String id = databaseReference.child("users").child(user.getUid()).child("Stories").push().getKey();

        // Tạo một map chứa dữ liệu cần thêm vào Realtime Database
        Map<String, Object> map = new HashMap<>();
        map.put("videoUrl", url);
        map.put("id", id);
        map.put("name", user.getDisplayName());
        map.put("uid", user.getUid());

        // Thêm dữ liệu vào Realtime Database
        databaseReference.child("users").child(user.getUid()).child("Stories").child(id).setValue(map);

    }

    void init(){
        videoView = findViewById(R.id.videoView);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == SELECT_VIDEO){
            Uri uri = data.getData();
            TrimVideo.activity(String.valueOf(uri))
                    .setCompressOption(new CompressOption())
                    .setTrimType(TrimType.MIN_MAX_DURATION)
                    .setMinToMax(5,30)
                    .setHideSeekBar(true)
                    .start(this,startForResult);
        }
    }

}