package com.example.android_social_media.fragment;;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android_social_media.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordFragment extends Fragment {

    private ImageView imageViewBack;
    private EditText edtEmail;
    private Button btnXacNhan;

    int code;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    String EmailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clickListener();
    }

    private void init (View view){
        imageViewBack = view.findViewById(R.id.imageViewBack);
        edtEmail = view.findViewById(R.id.editTextEmail);
        btnXacNhan = view.findViewById(R.id.btnXacNhan);
    }

    private void clickListener(){
        //Xử lý quay về trang login
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        // Xử lý xác nhận email quên mật khẩu
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = edtEmail.getText().toString().trim();
                // Lấy giá trị mật khẩu mới từ người dùng

                if (userEmail.isEmpty() || !userEmail.matches(EmailRegex)){
                    edtEmail.setError("Email bạn nhập không hợp lệ!");
                    return;
                }

                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                usersRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //Sang trang SendOTPFragment
                                SendOTPFragment sendOTPFragment = new SendOTPFragment();
                                //Bundle Chứa dữ liệu
                                Bundle bundle = new Bundle();
                                bundle.putString("email", userEmail);
                                sendOTPFragment.setArguments(bundle);
                                //Sang trang SendOTPFragment
                                FragmentManager manager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction trans = manager.beginTransaction();
                                trans.replace(R.id.fragment_container, sendOTPFragment);
                                trans.addToBackStack(null);
                                trans.commit();
                            } else {
                                // Email không tồn tại trong cơ sở dữ liệu
                                Toast.makeText(getContext(), "Địa chỉ email không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }
        });//end btnxac nhan
    }//End clickListener()
}