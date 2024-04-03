package com.example.android_social_media.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_social_media.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class LoginFragment extends Fragment {

    EditText txtUsername, txtPassword;
    TextView txtSignUp, txtForgotPassword;
    Button btnLogin;
    ImageView btnSignInGG;

    //đăng nhập bằng google
    private GoogleSignInClient client;

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Khởi tạo GoogleSignInOptions
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        // Khởi tạo GoogleSignInClient
        client = GoogleSignIn.getClient(requireActivity(), options);

        // Trả về view đã inflate
        return view;
    }

//    Điều hướng đến trang cá nhân với username đã đăng nhập
    public void navigateToProfile(String username){
        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        //Nếu đăng nhập thành công sẽ chuyển đến trang cá nhân
        profileFragment profile = new profileFragment();
        profile.setArguments(bundle); // Gán Bundle cho Fragment
        FragmentManager manager = requireActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.fragment_container, profile);
        trans.addToBackStack(null);
        trans.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                // Xóa cache của Firebase Authentication
                FirebaseAuth.getInstance().signOut();
                firebaseAuthWithGoogle(acc);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String email = acct.getEmail();
                        String displayName = acct.getDisplayName();
                        String profileImage = null;

                        if (acct.getPhotoUrl() != null) {
                            profileImage = acct.getPhotoUrl().toString();
                        }

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                        checkIfEmailExists(email, displayName, userId,profileImage, databaseReference);
                    } else {
                        Toast.makeText(getContext(), "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkIfEmailExists(String email, String displayName, String userId,String profileImage, DatabaseReference databaseReference) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String username = userSnapshot.child("username").getValue(String.class);
                        if (username != null) {
                            navigateToProfile(username);
                            Toast.makeText(getContext(), "Đăng nhập bằng Google thành công!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                } else {
                    // Email does not exist, create new user profile
                    createUserProfile(email, displayName, userId, profileImage, databaseReference);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });
    }


    private void createUserProfile(String email, String displayName, String userId, String profileImage, DatabaseReference databaseReference) {
        String newKey = databaseReference.push().getKey();
        String username;

        if (userId.length() > 10) {
            username = userId.substring(0, 10); // Take the first 10 characters
        } else {
            username = newKey;
        }
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("UserID", userId);
        userData.put("dob", "01/01/2000");
        userData.put("email", email);
        userData.put("gender", "nu");
        userData.put("name", displayName);
        userData.put("password", "123456");
        userData.put("phoneNumber", "");
        userData.put("profileImage", profileImage);
        userData.put("username", username);

        // Save new user profile to Firebase
        databaseReference.child(newKey).setValue(userData)
                .addOnSuccessListener(aVoid -> {
                    checkAcc(username, "123456");
                    navigateToProfile(username);
                    Toast.makeText(getContext(), "Đăng nhập bằng Google thành công!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occur during the write operation
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("FirebaseError", "Error writing user profile to Firebase", e);
                });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clickListener();
    }

    public void init(View view){
        txtSignUp = view.findViewById(R.id.txtSignUp);
        btnLogin = view.findViewById(R.id.btnLogin);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtPassword = view.findViewById(R.id.txtPassword);
        btnSignInGG = view.findViewById(R.id.btnSignInGG);
        txtForgotPassword = view.findViewById(R.id.txtForgotPassword);
    }

    public void clickListener(){
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpFragment signUpFragment = new SignUpFragment();
                FragmentManager manager = requireActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.fragment_container, signUpFragment);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                if(username.isEmpty()){
                    txtUsername.setError("Trường này không được để trống!");
                    return;
                }
                else if(password.isEmpty()){
                    txtPassword.setError("Trường này không được để trống!");
                    return;
                }
                checkAcc(username, password);
            }
        });

        btnSignInGG.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = client.getSignInIntent();
               startActivityForResult(intent, 1234);
           }
       });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                FragmentManager manager = requireActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.fragment_container, forgotPasswordFragment);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }

    public void checkAcc(String username, String password){
        db.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Lấy thông tin tài khoản từ mỗi nút con
                        String dbUsername = userSnapshot.child("username").getValue(String.class);
                        String dbPassword = userSnapshot.child("password").getValue(String.class);

                        // Kiểm tra xem username và password có khớp với dữ liệu từ Firebase không
                        if (dbUsername != null && dbPassword != null  && dbUsername.equals(username) && dbPassword.equals(password)) {

                            navigateToProfile(dbUsername);
                            Toast.makeText(getContext(),"Đăng nhập thành công!",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    Toast.makeText(getContext(),"Tài khoản không tồn tại!",Toast.LENGTH_LONG).show();
                    txtUsername.setText("");
                    txtPassword.setText("");
                }
                else{
                    Log.d("key users: ", "No data for key users.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }
}