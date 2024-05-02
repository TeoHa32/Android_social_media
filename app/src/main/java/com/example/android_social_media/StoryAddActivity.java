package com.example.android_social_media;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class StoryAddActivity extends AppCompatActivity {

    private Uri mImageUri;
    String myUrl = "";
    private StorageTask storageTask;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story_add);

        storageReference = FirebaseStorage.getInstance().getReference("story");

        // Khởi động mở album ảnh khi Activity được tạo
        openGallery();

    }

    private void openGallery() {
        ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            mImageUri = result;
                            startImageCrop(); // Bắt đầu hoạt động cắt ảnh sau khi chọn ảnh từ album
                        } else {
                            Toast.makeText(StoryAddActivity.this, "Không có ảnh nào được chọn!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

        galleryLauncher.launch("image/*");
    }


    // Phương thức để bắt đầu hoạt động cắt ảnh sử dụng thư viện Cropper
    private void startImageCrop() {
        CropImage.activity(mImageUri)
                .setAspectRatio(9, 16)
                .start(this);
    }


    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mineTypeMap = MimeTypeMap.getSingleton();
        return mineTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void publishStory(){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Đang tải ...");
        pd.show();

        if(mImageUri != null){
            StorageReference imageReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

            storageTask = imageReference.putFile(mImageUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();
                        String myid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(myid);
                        String storyId = reference.push().getKey();
                        long timeend = System.currentTimeMillis() + 86400000; // thời gian 1 ngày
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("startTime", ServerValue.TIMESTAMP);
                        hashMap.put("imageUrl",myUrl);
                        hashMap.put("endTime",timeend);
                        hashMap.put("storyId", storyId);
                        hashMap.put("userId", myid);
                        reference.child(storyId).setValue(hashMap);
                        pd.dismiss();
                        finish();
                    }
                    else{
                        Toast.makeText(StoryAddActivity.this, "Up story thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StoryAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            Toast.makeText(StoryAddActivity.this, "Không có ảnh nào được chọn!", Toast.LENGTH_SHORT).show();
        }

    }

    // Xử lý kết quả trả về từ hoạt động cắt ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                mImageUri = result.getUri();

                if (mImageUri != null) {
                    publishStory(); // Đăng bài story sau khi cắt ảnh
                } else {
                    Toast.makeText(this, "Không có ảnh nào được chọn!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Đã xảy ra lỗi khi cắt ảnh!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Đã hủy cắt ảnh!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



}