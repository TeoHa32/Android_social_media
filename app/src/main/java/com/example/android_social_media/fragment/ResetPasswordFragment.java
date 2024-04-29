package com.example.android_social_media.fragment;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android_social_media.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ResetPasswordFragment extends Fragment {
    private Button btnXacNhan, btnBackToLogin;
    private EditText edtResetPass1, edtResetPass2;
    private ImageView show, hide;
    boolean passwdVisible;
    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        clickListener();
    }

    private void init (View view){

        btnXacNhan = view.findViewById(R.id.btnXacNhan);
        btnBackToLogin = view.findViewById(R.id.btnBackToLogin);
        edtResetPass1 = view.findViewById(R.id.edtResetPass1);
        edtResetPass2 = view.findViewById(R.id.edtResetPass2);
        show = view.findViewById(R.id.show);
        hide = view.findViewById(R.id.hide);

    }

    private void clickListener(){
        edtResetPass2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= edtResetPass2.getRight() - edtResetPass2.getCompoundDrawablesRelative()[Right].getBounds().width()) {
                        int selection = edtResetPass1.getSelectionEnd();
                        if (passwdVisible) {
                            edtResetPass2.setCompoundDrawablesRelativeWithIntrinsicBounds(edtResetPass2.getCompoundDrawablesRelative()[0], null, getResources().getDrawable(R.drawable.ic_hide), null);
                            edtResetPass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwdVisible = false;
                        } else {
                            edtResetPass2.setCompoundDrawablesRelativeWithIntrinsicBounds(edtResetPass2.getCompoundDrawablesRelative()[0], null, getResources().getDrawable(R.drawable.ic_eye), null);
                            edtResetPass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwdVisible = true;
                        }
                        edtResetPass2.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String passWord = edtResetPass1.getText().toString();
                String pwConfirm = edtResetPass2.getText().toString();

                if(passWord.equals("") || passWord.length() < 6){
                    edtResetPass1.setError("Mật khẩu phải có tối thiểu 6 ký tự!");
                }
                else if(!pwConfirm.equals(passWord) || pwConfirm.isEmpty()){
                    edtResetPass2.setError("Mật khẩu bạn nhập không khớp, vui lòng nhập lại!");
                    edtResetPass1.setText("");
                    edtResetPass2.setText("");
                } else {
                    Bundle bundle = getArguments();
                    String userEmail = bundle.getString("email");



                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                    usersRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    String userId = userSnapshot.getKey();
                                    usersRef.child(userId).child("password").setValue(passWord).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Về trang Login
                                                LoginFragment loginFragment = new LoginFragment();
                                                FragmentManager manager = requireActivity().getSupportFragmentManager();
                                                FragmentTransaction trans = manager.beginTransaction();
                                                trans.replace(R.id.fragment_container, loginFragment);
                                                trans.addToBackStack(null);
                                                trans.commit();

                                                Toast.makeText(getContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(getContext(), "Có lỗi khi đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getContext(), "Địa chỉ Email không tồn tại!", Toast.LENGTH_SHORT).show();
                            }
//                            return 0;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi khi truy vấn dữ liệu
                        }
                    });
                }

            }
        });//end btnXacNhan

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Về trang Login
                LoginFragment loginFragment = new LoginFragment();
                FragmentManager manager = requireActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.fragment_container, loginFragment);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }
}