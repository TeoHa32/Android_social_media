package com.example.android_social_media;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.android_social_media.fragment.LoginFragment;
import com.example.android_social_media.fragment.SignUpFragment;

public class FragmentReplaceActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragment_replace);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        frameLayout = findViewById(R.id.frameLayout);
        setFragment(new LoginFragment());
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if(fragment instanceof SignUpFragment ){
            trans.addToBackStack(null);
        }
        trans.replace(frameLayout.getId(), fragment);
        trans.commit();
    }
}