package com.example.android_social_media.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android_social_media.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class InfoProfileFragment extends Fragment {
    EditText edtTieuSu;
    TextView pfName, pfGender, pfDob, pfPhone, pfGmail, pfUsername, pfPassword;
    TextView titleName, titleUsername, txtChangeImg;
    Button btnEdit;
    ImageView imageViewBack;
    CircleImageView imgProfile;
    ProgressBar progressBar;

    String username, img;
    String key ="";

    boolean isConfirmed = false;;


    public InfoProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        showUserData();
        clickListener();
        getUserInfo();
    }


    private void init(View view) {
        pfName = view.findViewById(R.id.pfName);
        pfGender = view.findViewById(R.id.pfGender);
        pfDob = view.findViewById(R.id.pfDob);
        pfPhone = view.findViewById(R.id.pfPhone);
        pfGmail = view.findViewById(R.id.pfGmail);
        pfUsername = view.findViewById(R.id.pfUsername);
        pfPassword = view.findViewById(R.id.pfPassword);
        titleName = view.findViewById(R.id.titleName);
        titleUsername = view.findViewById(R.id.titleUsername);
        btnEdit = view.findViewById(R.id.btnEdit);
        imageViewBack = view.findViewById(R.id.imageViewBack);
        imgProfile = view.findViewById(R.id.imgProfile);
        txtChangeImg = view.findViewById(R.id.txtChangeImg);
        progressBar = view.findViewById(R.id.progressBar);
        edtTieuSu = view.findViewById(R.id.edtTieuSu);
    }

    public void showUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nameUser = snapshot.child("name").getValue(String.class);
                    String genderUser = snapshot.child("gender").getValue(String.class);
                    String dobUser = snapshot.child("dob").getValue(String.class);
                    String phoneUser = snapshot.child("phoneNumber").getValue(String.class);
                    String gmailUser = snapshot.child("email").getValue(String.class);
                    String usernameUser = snapshot.child("username").getValue(String.class);
                    String passwordUser = snapshot.child("password").getValue(String.class);
                    String tieuSuUser = snapshot.child("tieusu").getValue(String.class);

                    username = snapshot.child("username").getValue(String.class);
                    img = snapshot.child("profileImage").getValue(String.class);

                    // Hiển thị dữ liệu lên TextView
                    titleName.setText(nameUser);
                    titleUsername.setText(usernameUser);
                    pfName.setText(nameUser);
                    if (genderUser.equals("nam")) {
                        pfGender.setText("Nam");
                    } else {
                        pfGender.setText("Nữ");
                    }
                    pfDob.setText(dobUser);
                    pfPhone.setText(phoneUser);
                    pfGmail.setText(gmailUser);
                    pfUsername.setText(usernameUser);
                    pfPassword.setText(passwordUser);
                    edtTieuSu.setText(tieuSuUser);
                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
//                return 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi!", Toast.LENGTH_SHORT).show();
            }
        });
    } //end show data user

    private void clickListener() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileFragment editProfileFragment = new EditProfileFragment();

                Bundle bundle = new Bundle();
                bundle.putString("name", pfName.getText().toString());
                bundle.putString("gender", pfGender.getText().toString());
                bundle.putString("dob", pfDob.getText().toString());
                bundle.putString("phone", pfPhone.getText().toString());
                bundle.putString("email", pfGmail.getText().toString());
                bundle.putString("username", pfUsername.getText().toString());
                bundle.putString("password", pfPassword.getText().toString());
                editProfileFragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, editProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });//end btnEdtit

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Log.d("Username ne", img);
                bundle.putString("username", username);
                bundle.putString("img", img);
                if(key != null){
                    bundle.putString("key", key);
                }

                profileFragment profileFragment = new profileFragment();
                profileFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, profileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });//end imageViewBack


        txtChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeImageFragment changeImageFragment = new ChangeImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                changeImageFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, changeImageFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });//end

        edtTieuSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConfirmed) {
                    // Hiển thị dialog chỉ khi mật khẩu chưa được xác nhận
                    showDialog(Gravity.CENTER);
                }
            }
        });

    }
    private void getUserInfo() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("profileImage").getValue().toString();
                Log.d("IMAGE", image);
                if (image != null && !image.isEmpty()) {
                    Picasso.get().load(image).into(imgProfile);
                } else {
                    //Ảnh đại diện mặc định khi user không có ảnh đại diện
                    imgProfile.setImageResource(R.drawable.ic_profile);
                }
//                return 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Lỗi khi truy xuất dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(int gravity){
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_tieusu);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        EditText edtTieuSuNow = dialog.findViewById(R.id.edtTieuSuNow);
        Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);
        Button btnHuy= dialog.findViewById(R.id.btnHuy);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
                String enteredTieuSu = edtTieuSuNow.getText().toString();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            userRef.child("tieusu").setValue(enteredTieuSu);
                            dialog.dismiss();
                            Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Hiển thị thông báo lỗi hoặc thực hiện các hành động phù hợp
                            Toast.makeText(requireContext(), "Người dùng không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.show();
    }
}



