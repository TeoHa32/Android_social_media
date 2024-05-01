package com.example.android_social_media.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import com.example.android_social_media.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Properties;
import java.util.Random;



public class SendOTPFragment extends Fragment {
    private Button btnSendOTP, btnXacNhan;
    int code;
    String userEmail;
    private EditText edt1, edt2, edt3, edt4;
    private ImageView imageViewBack;
    public SendOTPFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_o_t_p, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clickListener();
    }

    private void init (View view){

        btnSendOTP = view.findViewById(R.id.btnSendOTP);
        btnXacNhan = view.findViewById(R.id.btnXacNhan);
        edt1 = view.findViewById(R.id.edt1);
        edt2 = view.findViewById(R.id.edt2);
        edt3 = view.findViewById(R.id.edt3);
        edt4 = view.findViewById(R.id.edt4);
        imageViewBack = view.findViewById(R.id.imageViewBack);

    }

    private void clickListener(){
        //Xử lý quay về trang nhập gmail quên mk
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        edt1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // Nếu người dùng nhấn nút Delete trong ô đầu
                    edt1.getText().clear();
                } else if (!edt1.getText().toString().isEmpty()) {
                    // Nếu người dùng nhập ký tự vào ô đầu
                    edt2.requestFocus();
                }
                return false;
            }
        });

        edt2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // Nếu người dùng nhấn nút Delete trong ô đầu
                    edt2.getText().clear();
                    edt1.requestFocus();
                } else if (!edt2.getText().toString().isEmpty()) {
                    // Nếu người dùng nhập ký tự vào ô đầu
                    edt3.requestFocus();
                }
                return false;
            }
        });

        edt3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // Nếu người dùng nhấn nút Delete trong ô đầu
                    edt3.getText().clear();
                    edt2.requestFocus();
                } else if (!edt3.getText().toString().isEmpty()) {
                    // Nếu người dùng nhập ký tự vào ô đầu
                    edt4.requestFocus();
                }
                return false;
            }
        });

        edt4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // Nếu người dùng nhấn nút Delete trong ô đầu
                    edt4.getText().clear();
                    edt3.requestFocus();
                }
                return false;
            }
        });

        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Khởi tạo mã code otp
                    Random random = new Random();
                    code = random.nextInt(8999)+1000;

                    //Nhận dữ liệu từ bundle
                    Bundle bundle = getArguments();
                    userEmail = bundle.getString("email");

                    String senderEmail = "lekimngan22102002@gmail.com";
                    String passwordSenderEmail = "tcegnrugrvdqofqv";
                    String host = "smtp.gmail.com";

                    Properties properties =System.getProperties();
                    properties.put("mail.smtp.host", host);
                    properties.put("mail.smtp.socketFactory.port", "465");
                    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.port", "465");

                    javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(senderEmail, passwordSenderEmail);
                        }
                    });

                    MimeMessage mineMessage = new MimeMessage(session);
                    mineMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
                    mineMessage.setSubject("Mã OTP của bạn là: " + code);
                    mineMessage.setText(" Hi " + userEmail + ", \n\nChúng tôi nhận được yêu cầu đặt lại mật khẩu của bạn.\n\nHãy nhập mã để đặt lại mật khẩu: " + code);
                    Toast.makeText(getActivity(), "Mã OTP đã được gửi tới " + userEmail +". Vui lòng chờ trong vài phút!", Toast.LENGTH_SHORT).show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Transport.send(mineMessage);
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                    thread.start();

                    } catch (AddressException e) {
                        throw new RuntimeException(e);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                ;
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeInput = edt1.getText().toString() + edt2.getText().toString() +
                        edt3.getText().toString() + edt4.getText().toString();
                if (codeInput.equals(String.valueOf(code))){
                    ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                    //Bundle Chứa dữ liệu
                    Bundle bundle = new Bundle();
                    bundle.putString("email", userEmail);
                    resetPasswordFragment.setArguments(bundle);
                    //Sang trang ResetPasswordFragment
                    FragmentManager manager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();
                    trans.replace(R.id.fragment_container, resetPasswordFragment);
                    trans.addToBackStack(null);
                    trans.commit();
                } else {
                    Toast.makeText(getContext(), "Mã OTP không đúng!", Toast.LENGTH_SHORT).show();
                }

            }
        }); //end btnXacNhan
    }

}