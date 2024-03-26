package com.example.android_social_media.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android_social_media.R;

public class profileFragment extends Fragment {

    Button btnInfoProfile;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clickListener();
    }

    private void init(View view) {
        btnInfoProfile = view.findViewById(R.id.btnInfoProfile);
    }
    private void clickListener() {
        btnInfoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoProfileFragment infoProfileFragment = new InfoProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, infoProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}