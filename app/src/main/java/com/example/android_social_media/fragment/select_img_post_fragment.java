package com.example.android_social_media.fragment;
import android.Manifest;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.android_social_media.R;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class select_img_post_fragment extends Fragment {
    Button btn_search;
    Button btPick;
    RecyclerView recyclerView;
    ArrayList<Uri> arrayList = new ArrayList<>();
    public select_img_post_fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_select_img_post_fragment, container, false);

    return view;
    }
//    public void onClickListener(){
//        btPick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] strings = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
//                if(EasyPermissions.hasPermissions(getContext(), strings)){
//                    imagePicker();
//                }
//                else {
//                    EasyPermissions.requestPermissions(
//                            this,
//                            "App need access to your CAMERA and STORAGE",
//                            100,
//                            strings
//                    );
//                }
//            }
//        });
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    public void imagePicker(){
//        FilePickerBuilder.getInstance()
//                .setActivityTitle("Select image")
//                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN,3)
//                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN,4)
//                .setMaxCount(4)
//                .setSelectedFiles(arrayList)
//                .pickPhoto(this);
//    }
    public void init(View view){
        btPick = view.findViewById(R.id.btn_select_post);
        recyclerView = view.findViewById(R.id.recycler_view);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
//        onClickListener();
    }
}