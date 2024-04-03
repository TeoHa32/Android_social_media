package com.example.android_social_media.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android_social_media.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditProfileFragment extends Fragment {

    EditText edtName, edtDob, edtPhone, edtEmail, edtUsername, edtPassword;
    Button btnSave;
    RadioButton rdMale, rdFeMale;
    String nameUser, genderUser, dobUser, phoneUser, emailUser, usernameUser, passwordUser;
    ImageView imageViewBack;
    boolean isPasswordConfirmed = false;;
    DatabaseReference usersRef;

    //    Check Regex
    String EmailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    String SDTRegex = "^(03[2-9]|05[6-9]|07[0|6-9]|08[1-9]|09[0-4|6-9])[0-9]{7}$";
    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        showdata();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        clickListener();
    }

    private void init(View view) {
        edtName = view.findViewById(R.id.edtName);
        edtDob = view.findViewById(R.id.edtDob);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtEmail = view.findViewById(R.id.edtMail);
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPassword = view.findViewById(R.id.edtPassword);
        btnSave = view.findViewById(R.id.btnSave);
        rdMale = view.findViewById(R.id.rdMale);
        rdFeMale = view.findViewById(R.id.rdFeMale);
        imageViewBack = view.findViewById(R.id.imageViewBack);
    }

    private void showdata() {
        // Nhận dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            nameUser = bundle.getString("name");
            genderUser = bundle.getString("gender");
            dobUser = bundle.getString("dob");
            phoneUser = bundle.getString("phone");
            emailUser = bundle.getString("email");
            usernameUser = bundle.getString("username");
            passwordUser = bundle.getString("password");
        }

        edtName.setText(nameUser);
        if (genderUser.equals("Nam")) {
            rdMale.setChecked(true);
            genderUser = "nam";
        } else {
            rdFeMale.setChecked(true);
            genderUser = "nu";
        }
        edtDob.setText(dobUser);
        edtPhone.setText(phoneUser);
        edtEmail.setText(emailUser);
        edtUsername.setText(usernameUser);
        edtPassword.setText(passwordUser);

    }

    private void clickListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAnyChange = false;

                if (isNameChange()) {
                    isAnyChange = true;
                }
                if (isDobChange()) {
                    isAnyChange = true;
                }
                if (isGenderChange()) {
                    isAnyChange = true;
                }
                if (isPhoneChange()) {
                    isAnyChange = true;
                }
                if (isUsernameChange()) {
                    isAnyChange = true;
                }
                if (isPasswordChange()) {
                    isAnyChange = true;
                }

                if (isAnyChange) {
                    // Thực hiện các thay đổi cần thiết (ví dụ: cập nhật dữ liệu trong Firebase)
                    Toast.makeText(getContext(), "Lưu thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Không có sự thay đổi!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoProfileFragment infoProfileFragment = new InfoProfileFragment();

                Bundle bundle = new Bundle();
                bundle.putString("username",usernameUser);
                infoProfileFragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, infoProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });//end imageViewBack

        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        
        edtPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordConfirmed) {
                    // Hiển thị dialog chỉ khi mật khẩu chưa được xác nhận
                    showPasswordConfirmationDialog(Gravity.CENTER);
                }
            }
        });

    }

    private void showPasswordConfirmationDialog(int gravity){
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_password);

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
        EditText edtPasswordNow = dialog.findViewById(R.id.edtPasswordNow);
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
                String enteredPassword = edtPasswordNow.getText().toString();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
                Query query = userRef.orderByChild("username").equalTo(usernameUser);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String currentPassword = userSnapshot.child("password").getValue(String.class);
                                if (enteredPassword.equals(currentPassword)) {
                                    edtPassword.setText(edtPasswordNow.getText());
                                    edtPassword.setTransformationMethod(null); // Hiển thị mật khẩu không che
                                    edtPassword.setFocusable(true); // Cho phép người dùng nhập vào edtPassword
                                    edtPassword.setFocusableInTouchMode(true);
                                    isPasswordConfirmed = true;
                                    dialog.dismiss();
                                    Toast.makeText(requireContext(), "Mật khẩu nhập chính xác!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Hiển thị thông báo lỗi hoặc thực hiện các hành động phù hợp
                                    Toast.makeText(requireContext(), "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                                    edtPasswordNow.getText().clear();
                                }
                            }
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

    private void showDatePickerDialog() {
        // Lấy ngày, tháng, năm hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Thực hiện các xử lý khi ngày tháng năm được chọn
                // Ví dụ: đặt giá trị vào EditText
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                edtDob.setText(selectedDate);
            }
        }, year, month, day);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    private boolean isNameChange(){
        String newName = edtName.getText().toString();
        if(newName.isEmpty()){
            edtName.setError("Vui lòng nhập tên!");
            return false;
        } else if (!nameUser.equals(newName)){
            usersRef.orderByChild("username").equalTo(usernameUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        usersRef.child(userId).child("name").setValue(edtName.getText().toString());
                        break; // Chỉ cần cập nhật một người dùng duy nhất
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            nameUser = newName;
            return true;
        } else {
            return false;
        }
    }

    private boolean isGenderChange(){
        String newGender = rdMale.isChecked() ? "nam" : "nu";
        if (!genderUser.equals(newGender)){
            usersRef.orderByChild("username").equalTo(usernameUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        usersRef.child(userId).child("gender").setValue(newGender);
                        break; // Chỉ cần cập nhật một người dùng duy nhất
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            genderUser = newGender;
            return true;
        } else {
            return false;
        }
    }

    private boolean isDobChange(){
        String newDob = edtDob.getText().toString();
        if (!dobUser.equals(newDob)){
            usersRef.orderByChild("username").equalTo(usernameUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        usersRef.child(userId).child("dob").setValue(edtDob.getText().toString());
                        break; // Chỉ cần cập nhật một người dùng duy nhất
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            dobUser = newDob;
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhoneChange(){
        String newPhone = edtPhone.getText().toString();
        if(!newPhone.matches(SDTRegex)){
            edtPhone.setError("Số điện thoại không đúng, vui lòng nhập lại!");
            return false;
        } else if (!phoneUser.equals(newPhone)){
            usersRef.orderByChild("username").equalTo(usernameUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        usersRef.child(userId).child("phoneNumber").setValue(edtPhone.getText().toString());
                        break; // Chỉ cần cập nhật một người dùng duy nhất
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            phoneUser = newPhone;
            return true;
        } else {
            return false;
        }
    }

    private boolean isUsernameChange(){
        String newUsername = edtUsername.getText().toString();
        if(newUsername.isEmpty()){
            edtUsername.setError("Vui lòng nhập tên đăng nhập!");
            return false;
        } else if (!usernameUser.equals(newUsername)){
            usersRef.orderByChild("username").equalTo(usernameUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        usersRef.child(userId).child("username").setValue(edtUsername.getText().toString());
                        break; // Chỉ cần cập nhật một người dùng duy nhất
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            usernameUser = newUsername;

            profileFragment profileFragment = new profileFragment();
            InfoProfileFragment infoProfileFragment = new InfoProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username",usernameUser);
            profileFragment.setArguments(bundle);
            infoProfileFragment.setArguments(bundle);


            return true;
        } else {
            return false;
        }
    }

    private boolean isPasswordChange(){
        String newPassword = edtPassword.getText().toString();
        if(newPassword.equals("") || newPassword.length() < 6){
            edtPassword.setError("Mật khẩu phải có tối thiểu 6 ký tự!");
            return false;
        } else if (!passwordUser.equals(newPassword)){
            usersRef.orderByChild("username").equalTo(usernameUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        usersRef.child(userId).child("password").setValue(edtPassword.getText().toString());
                        break; // Chỉ cần cập nhật một người dùng duy nhất
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            passwordUser = newPassword;
            return true;
        } else {
            return false;
        }
    }

}