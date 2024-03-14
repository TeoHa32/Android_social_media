package com.example.android_social_media.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

<<<<<<< HEAD
import android.text.InputType;
=======
>>>>>>> d0b49551c786c4bb0db174274e11d895c839f91d
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
=======
import com.example.android_social_media.MainActivity;
>>>>>>> d0b49551c786c4bb0db174274e11d895c839f91d
import com.example.android_social_media.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private EditText txtName, txtEmail, txtPhoneNumber, txtUsername, txtPassword, txtConfirm;
    private TextView txtLogin;
    private DatePicker dateOfBirth;
    private RadioButton rb_nam, rb_nu;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private ImageView hide, show;


//    Check Regex
    String EmailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    String SDTRegex = "^(03[2-9]|05[6-9]|07[0|6-9]|08[1-9]|09[0-4|6-9])[0-9]{7}$";

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clickListener();
    }

    private void init(View view){
        txtName = view.findViewById(R.id.txtHoTen);
        txtEmail = view.findViewById(R.id.txtEmail);
        dateOfBirth = view.findViewById(R.id.dateTime);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        txtUsername = view.findViewById(R.id.txtTDN);
        txtPassword = view.findViewById(R.id.txtMK);
        txtConfirm = view.findViewById(R.id.txtConfirm);
        txtLogin = view.findViewById(R.id.txtLogin);
        rb_nam = view.findViewById(R.id.rbMale);
        rb_nu = view.findViewById(R.id.rbFemale);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        progressBar = view.findViewById(R.id.progressBar);
        hide = view.findViewById(R.id.hide);
        show = view.findViewById(R.id.show);

        auth = FirebaseAuth.getInstance();
    }

    private void clickListener() {

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, loginFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SignUpFragment", "SignUp button clicked.");
                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();

                //Xử lý ngày sinh.
                int day = dateOfBirth.getDayOfMonth();
                int month = dateOfBirth.getMonth() + 1;
                int year = dateOfBirth.getYear();
                String dob = day + "/" + month + "/" + year;

                //Xử lý giới tính
                String gender = "";
                if(rb_nam.isChecked()){
                    gender = "nam";
                }
                else if(rb_nu.isChecked()){
                    gender ="nu";
                }

                String phoneNumber = txtPhoneNumber.getText().toString();
                String userName = txtUsername.getText().toString();
                String passWord = txtPassword.getText().toString();
                String pwConfirm = txtConfirm.getText().toString();

                if(name.isEmpty()){
                    txtName.setError("Vui lòng nhập tên!");
                    return;
                }
                if(!email.matches(EmailRegex)){
                    txtEmail.setError("Email không hợp lệ, vui lòng nhập lại!");
                    return;
                }
                else if(!phoneNumber.matches(SDTRegex)){
                    txtPhoneNumber.setError("Số điện thoại không đúng, vui lòng nhập lại!");
                    return;
                }
                else if(userName.isEmpty()){
                    txtUsername.setError("Vui lòng nhập tên đăng nhập!");
                    return;
                }
                else if(passWord.equals("") || passWord.length() < 6){
                    txtPassword.setError("Mật khẩu phải có tối thiểu 6 ký tự!");
                    return;
                }
                else if(!pwConfirm.equals(passWord) || pwConfirm.isEmpty()){
                    txtConfirm.setError("Mật khẩu bạn nhập không khớp, vui lòng nhập lại!");
                    txtPassword.setText("");
                    txtConfirm.setText("");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                createAcc(name, email, dob, gender, phoneNumber, userName, passWord, pwConfirm);

            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide.setVisibility(View.VISIBLE);
                show.setVisibility(View.GONE);
                txtConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show.setVisibility(View.VISIBLE);
                hide.setVisibility(View.GONE);
                txtConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
    }

    public void createAcc(String name,String email, String dob, String gender, String phoneNumber, String username, String password, String pwConfirm){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>()  {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            upLoadUser(user, name, email, dob, gender, phoneNumber, username, password);

                        }
                        else{
                            Exception e = task.getException();
                            if(e instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getContext(),"Email đã tồn tại, vui lòng chọn email khác để đăng ký!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                Log.d("Error:", task.getException().getMessage());
                            }

                        }
                    }
                });

    }

    private void upLoadUser(FirebaseUser user, String name, String email, String dob, String gender, String phoneNumber, String username, String password){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String key = "users";
        Map<String, Object> updates = new HashMap<>();
        updates.put("UserID", user.getUid());
        updates.put("name", name);
        updates.put("email", email);
        updates.put("profileImage", "");
        updates.put("dob", dob);
        updates.put("gender", gender);
        updates.put("phoneNumber", phoneNumber);
        updates.put("username", username);
        updates.put("password", password);

        db.child(key).child(user.getUid()).setValue(updates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Không update được dữ liệu!", Toast.LENGTH_LONG).show();
                } else {
                    //Nếu đăng ký thành công sẽ chuyển đến trang cá nhân
                    startActivity(new Intent(requireContext(), trangcanhan.class));
                    Toast.makeText(getContext(), "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}