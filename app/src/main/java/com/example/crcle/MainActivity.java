package com.example.crcle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.crcle.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button Signup,Login;
    EditText number,password;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Signup=findViewById(R.id.sign_up);
        Login=findViewById(R.id.login);
        number=findViewById(R.id.login_mobile);
        password=findViewById(R.id.login_password);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Signup_activity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Number=number.getText().toString();
                String Password=password.getText().toString();
                if (Number.isEmpty()){
                    return;
                }
                else if (Password.isEmpty()){
                    return;
                }
                FirebaseAuth.getInstance().signInWithEmailAndPassword(Number,Password).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    //hashMap.put("token",task.getResult().toString());
                                    String token=task.getResult().toString();
//                                                Log.d("manish",fcm_token);
                                    Map<String,String> map=new HashMap<>();
                                    map.put("fcm_token",token);
                                    FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("fcm_token").setValue(token);

                                }
                            });
                            Intent intent=new Intent(MainActivity.this,Menu_activity.class);
                            startActivity(intent);
                        }
                    }
                });
//                mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
//                    @Override
//                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                    }
//
//                    @Override
//                    public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                    }
//
//                    @Override
//                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                        super.onCodeSent(s, forceResendingToken);
//                        Intent intent=new Intent(MainActivity.this,Otp_Activity.class);
//                        intent.putExtra("login_mobile",Number);
//                        intent.putExtra("login_password",Password);
//                        intent.putExtra("VerificationId",s);
//                        startActivity(intent);
//                    }
//                };
//                PhoneAuthOptions options =
//                        PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
//                                .setPhoneNumber("+91"+Number )       // Phone number to verify
//                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                                .setActivity(MainActivity.this)                 // Activity (for callback binding)
//                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                                .build();
//                PhoneAuthProvider.verifyPhoneNumber(options);




            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,Menu_activity.class);
            startActivity(intent);
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("fcm_token").setValue(task.getResult().toString());
                }
            });
        }
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if (task.isSuccessful()){
//                    //hashMap.put("firebase_token",task.getResult());
//                    Log.d("alok",task.getResult().toString());
//                }
//            }
//        });
    }
}