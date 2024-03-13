package com.example.android_social_media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.example.android_social_media.fragment.SignUpFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btnClick = (Button) findViewById(R.id.btn1);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClick.setVisibility(View.GONE);
                // Add SignUpFragment
                SignUpFragment signUpFragment = new SignUpFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, signUpFragment);
                fragmentTransaction.addToBackStack(null); // Add to back stack if needed
                fragmentTransaction.commit();
            }
        });


    }

}