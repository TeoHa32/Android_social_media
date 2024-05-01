package com.example.android_social_media.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_social_media.R;
import com.example.android_social_media.adapter.SearchAdapter;
import com.example.android_social_media.model.SearchUser;
import com.example.android_social_media.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class SearchResultFragment extends Fragment {

    TextView btnHuy;
    SearchView searchView;
    RecyclerView recyclerView;
    SearchAdapter adapter;
    String key = "";
    List<SearchUser> list;
    /*DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");*/
    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        loadUserData();

        searchUser();

        clickListener();
    }

    private void clickListener() {

        adapter.OnUserClicked(new SearchAdapter.OnUserClicked() {
            @Override
            public void onItemClicked(View view, SearchUser searchUser) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
                // Tạo Bundle để truyền UID qua ProfileFragment
                Bundle bundle = new Bundle();
                bundle.putString("username", searchUser.getUsername());
                bundle.putString("img", searchUser.getProfileImage());
                bundle.putString("UserID", searchUser.getUserID());
                if(key != null){
                    bundle.putString("key", key);
                }

                // Tạo instance của ProfileFragment và thiết lập UID
                profile profile = new profile();
                profile.setArguments(bundle);
                profileFragment profileFragment = new profileFragment();
                profileFragment.setArguments(bundle);

                if(uid.equals(searchUser.getUserID())){
                    FragmentManager manager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();
                    trans.replace(R.id.fragment_container, profileFragment);
                    trans.addToBackStack(null);
                    trans.commit();
                } else {
                    // Thực hiện chuyển Fragment
                    FragmentManager manager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();
                    trans.replace(R.id.fragment_container, profile);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void searchUser() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");

            @Override
            public boolean onQueryTextSubmit(String query) {
                String normalizedQuery = normalizeString(query); // Chuẩn hóa từ khóa tìm kiếm

                userRef.orderByChild("name")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list.clear(); // Xóa danh sách trước khi thêm kết quả mới

                                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                    if (datasnapshot.getValue() != null) {
                                        SearchUser searchUser = datasnapshot.getValue(SearchUser.class);
                                        String normalizedName = normalizeString(searchUser.getName()); // Chuẩn hóa tên người dùng

                                        if (normalizedName.contains(normalizedQuery)) {
                                            list.add(searchUser);
                                        }
                                    }
                                }

                                if (list.isEmpty()) {
                                    Toast.makeText(getContext(), "Người dùng không tồn tại!", Toast.LENGTH_SHORT).show();
                                }

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Đã xảy ra lỗi khi truy vấn dữ liệu!", Toast.LENGTH_SHORT).show();
                            }
                        });
                userRef.orderByChild("name")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list.clear(); // Xóa danh sách trước khi thêm kết quả mới

                                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                    if (datasnapshot.getValue() != null) {
                                        SearchUser searchUser = datasnapshot.getValue(SearchUser.class);
                                        String normalizedName = normalizeString(searchUser.getName()); // Chuẩn hóa tên người dùng

                                        if (normalizedName.contains(normalizedQuery)) {
                                            list.add(searchUser);
                                        }
                                    }
                                }

                                if (list.isEmpty()) {
                                    Toast.makeText(getContext(), "Người dùng không tồn tại!", Toast.LENGTH_SHORT).show();
                                }

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Đã xảy ra lỗi khi truy vấn dữ liệu!", Toast.LENGTH_SHORT).show();
                            }
                        });

                return false;
            }

            private String normalizeString(String input) {
                // Chuẩn hóa chuỗi và loại bỏ các ký tự dấu
                String normalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
                Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
                return pattern.matcher(normalizedString).replaceAll("").toLowerCase();
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý khi người dùng thay đổi nội dung của ô tìm kiếm
                // Có thể thực hiện tìm kiếm tự động khi người dùng nhập liệu
                if (newText.equals("")) {
                    loadUserData();
                } else {
                    String lowercaseQuery = newText.toLowerCase(); // Chuyển đổi từ khóa tìm kiếm sang chữ thường

                    userRef.orderByChild("name")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    list.clear(); // Xóa danh sách trước khi thêm kết quả mới

                                    for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                        if (datasnapshot.getValue() != null) {
                                            SearchUser searchUser = datasnapshot.getValue(SearchUser.class);
                                            String name = searchUser.getName().toLowerCase(); // Chuyển đổi tên người dùng sang chữ thường

                                            if (name.contains(lowercaseQuery)) {
                                                list.add(searchUser);
                                            }
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "Đã xảy ra lỗi khi truy vấn dữ liệu!", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                return false;
            }
        });
    }

    private void loadUserData() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SearchUser searchUser = snapshot.getValue(SearchUser.class);
                    list.add(searchUser);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    public void init(View view){
        btnHuy = view.findViewById(R.id.btnHuy);
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        adapter = new SearchAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}