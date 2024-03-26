package com.example.android_social_media.fragment;

import android.os.Bundle;

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

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView txtUsername = view.findViewById(R.id.txtUsername);
        TextView txtUsernameProfile = view.findViewById(R.id.usernameProfile);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String username = bundle.getString("username");
            txtUsername.setText(username);
            txtUsernameProfile.setText(username);
        }

        return view;

    }
}