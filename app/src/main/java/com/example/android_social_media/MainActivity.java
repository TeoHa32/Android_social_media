package com.example.android_social_media;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_social_media.model.User;
import com.example.android_social_media.adapter.UserAdapter;
import com.example.android_social_media.model.post;
import com.example.android_social_media.adapter.postAdapter;
import com.example.android_social_media.fragment.homepageFragment;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homepageFragment fragment = new homepageFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        //-------------------------------------
        // Write a message to the database
//        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        String key = "Messages";
//        Map<String, Object> updates = new HashMap<>();
//        updates.put("ID", id);
//        updates.put("message", "Test Message");
//        // Get the current time
//        LocalTime currentTime = LocalTime.now();
//
//        // Define a custom format
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//        // Format the current time
//        String formattedTime = currentTime.format(formatter);
//        updates.put("time",formattedTime);
//        UUID uuid = UUID.randomUUID();
//        updates.put("senderID", uuid.toString());
//
//        db.child(key).child(id).setValue(updates, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                if(error != null){
//                    Toast.makeText(getApplicationContext(),"Không update được dữ liệu!", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//

        //Khởi tạo ban đầu sẽ ở trang đăng nhập.
//        LoginFragment loginFragment = new LoginFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, loginFragment);
//        fragmentTransaction.addToBackStack(null); // Add to back stack if needed
//        fragmentTransaction.commit();
//        //đổi màu
//        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
    }
}