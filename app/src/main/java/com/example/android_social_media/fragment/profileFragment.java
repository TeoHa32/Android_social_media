package com.example.android_social_media.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android_social_media.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    TextView txtUsername, usernameProfile;


    public profileFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clickListener();
    }

    public void init(View view){
        txtUsername = view.findViewById(R.id.txtUsername);
        usernameProfile = view.findViewById(R.id.usernameProfile);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String username = bundle.getString("username");
            if (username != null) {
                txtUsername.setText(username);
                usernameProfile.setText(username);
            }
        }
    }

    public void clickListener(){

    }
}