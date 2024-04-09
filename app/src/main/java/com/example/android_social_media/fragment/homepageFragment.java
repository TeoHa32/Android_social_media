package com.example.android_social_media.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_social_media.R;
import com.example.android_social_media.adapter.UserAdapter;
import com.example.android_social_media.adapter.postAdapter;
import com.example.android_social_media.model.User;
import com.example.android_social_media.model.post;

import java.util.ArrayList;
import java.util.List;
public class homepageFragment extends Fragment {
    private RecyclerView rcv;
    private UserAdapter userAdapter;
    private RecyclerView rcv_post;
    private com.example.android_social_media.adapter.postAdapter postAdapter;

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public homepageFragment() {
        // Required empty public constructor
    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment homepageFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static homepageFragment newInstance(String param1, String param2) {
//        homepageFragment fragment = new homepageFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        rcv = view.findViewById(R.id.rcv_id);
        userAdapter = new UserAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rcv.setLayoutManager(linearLayoutManager);
        userAdapter.setData(getListUser());
        rcv.setAdapter(userAdapter);
        rcv_post = view.findViewById(R.id.rcv_post);
        postAdapter = new postAdapter(getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        rcv_post.setLayoutManager(linearLayoutManager1);
        postAdapter.setData(getListPost());
        rcv_post.setAdapter(postAdapter);
        return view;
    }
    public List<User> getListUser(){
        List<User> list = new ArrayList<>();
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        list.add(new User("ha",R.drawable.ic_launcher_background));
        return list;
    }
    public List<post> getListPost(){
        List<post> list = new ArrayList<>();
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "ha",R.drawable.heart,R.drawable.comment, R.drawable.share));
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "Trinh",R.drawable.heart,R.drawable.comment, R.drawable.share));
        list.add(new post(R.drawable.ic_launcher_background, R.drawable.post_mew, "ha",R.drawable.heart,R.drawable.comment, R.drawable.share));
        return list;
    }
}