package com.example.crcle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crcle.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;

public class Otp_Activity extends AppCompatActivity {
    TextView otpnumber,sendotpagain;
    AppCompatButton verify;
    EditText Otp1,Otp2,Otp3,Otp4,Otp5,Otp6;
    FirebaseAuth auth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpnumber=findViewById(R.id.otp_number);
        verify=findViewById(R.id.otp_confirm);
        sendotpagain=findViewById(R.id.send_otp_again);
        Otp1=findViewById(R.id.otp1);
        Otp2=findViewById(R.id.otp2);
        Otp3=findViewById(R.id.otp3);
        Otp4=findViewById(R.id.otp4);
        Otp5=findViewById(R.id.otp5);
        Otp6=findViewById(R.id.otp6);
        database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference();
        String number=getIntent().getStringExtra("number");
        String name=getIntent().getStringExtra("name");
        String password=getIntent().getStringExtra("password");
        String verificationid=getIntent().getStringExtra("VerificationId");
        auth=FirebaseAuth.getInstance();
        edittextInput();
        otpnumber.setText("+91 "+number);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify.setVisibility(View.INVISIBLE);
                if (Otp1.getText().toString().isEmpty()||Otp2.getText().toString().isEmpty()||Otp3.getText().toString().isEmpty()||Otp4.getText().toString().isEmpty()||Otp5.getText().toString().isEmpty()||Otp6.getText().toString().isEmpty()){
                    Toast.makeText(Otp_Activity.this,"fill the otp",Toast.LENGTH_SHORT).show();
                }
                String otp=Otp1.getText().toString()+Otp2.getText().toString()+Otp3.getText().toString()+Otp4.getText().toString()+Otp5.getText().toString()+Otp6.getText().toString();
                PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationid,otp);
                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //User user=new User(name,number,password);
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("name",name);
                            hashMap.put("number",number);
                            hashMap.put("password",password);

                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (task.isSuccessful()){
                                        hashMap.put("firebase_token",task.getResult());
                                    }
                                }
                            });
                            //Log.d("manishs",name);
                            String id=task.getResult().getUser().getUid();
                            databaseReference.child("User").child(id).setValue(hashMap);
                            FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).set(hashMap);
                            Intent intent=new Intent(Otp_Activity.this,Menu_activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            // Update UI
                        } else {
                            verify.setVisibility(View.VISIBLE);


                        }
                    }
                });

            }
        });



    }

    private void edittextInput() {
        Otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Otp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Otp3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Otp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Otp5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Otp6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        Otp4.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }
}