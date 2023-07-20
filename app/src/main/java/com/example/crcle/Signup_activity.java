package com.example.crcle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crcle.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Signup_activity extends AppCompatActivity {
    EditText name,number,create_password,confirm_password;
    Button Signup,Signin;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseDatabase database;
    static String fcm_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name=findViewById(R.id.create_name);
        number=findViewById(R.id.signup_mobile);
        create_password=findViewById(R.id.create_password);
        confirm_password=findViewById(R.id.confirm_password);
        Signup=findViewById(R.id.signup_sign);
        Signin=findViewById(R.id.signup_login);
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference();


        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name=name.getText().toString();
                String Number=number.getText().toString();
                String Create_password=create_password.getText().toString();
                String Confirm_password=confirm_password.getText().toString();
                if (Name.isEmpty()){
                    name.setError("name is required");
                    return;
                }
                if (Number.isEmpty()){
                    number.setError("number is required");
                    return;
                }
                if (Create_password.isEmpty()){
                    create_password.setError("password is required");
                    return;
                }
                if (Confirm_password.isEmpty()){
                    confirm_password.setError("confirm password is required");
                    return;
                }
                if (!Create_password.equals(Confirm_password)){
                    Toast.makeText(Signup_activity.this,"password should be same",Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (Number.length()<10||Number.length()>10){
//                    number.setError("number is not valid");
//                    return;
//                }

                else {
                    auth.createUserWithEmailAndPassword(Number, Confirm_password)
                            .addOnCompleteListener(Signup_activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
//                                        Log.d(TAG, "createUserWithEmail:success");
//                                        FirebaseUser user = mAuth.getCurrentUser();
//                                        updateUI(user);
                                        HashMap<String,String> hashMap=new HashMap<>();
                                        String id=task.getResult().getUser().getUid();
                                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                //hashMap.put("token",task.getResult().toString());
                                                String token=task.getResult().toString();
//                                                Log.d("manish",fcm_token);
                                                User user=new User(Name,Number,Confirm_password,token);
                                                databaseReference.child("User").child(id).setValue(user);

                                            }
                                        });
                                        hashMap.put("name",Name);
                                        hashMap.put("number",Number);
                                        hashMap.put("password",Confirm_password);
                                        hashMap.put("profile","");
                                        hashMap.put("cover_img","");
                                        hashMap.put("followers","");
                                        hashMap.put("followings","");
                                        hashMap.put("bio","");

                                        //databaseReference.child("Ur").setValue(user);

                                        databaseReference.child("User").child(id).setValue(hashMap);
                                        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).set(hashMap);
                                        Intent intent=new Intent(Signup_activity.this,Menu_activity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Signup_activity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                        //Log.w(, "createUserWithEmail:failure", task.getException());
                                        Log.d("manish",task.getException().toString());
//                                        Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                                Toast.LENGTH_SHORT).show();
//                                        updateUI(null);

                                    }
                                }
                            });
                }



//                else {
//                    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//                        @Override
//                        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
//
//
//                        }
//
//                        @Override
//                        public void onVerificationFailed(@NonNull FirebaseException e) {
//                            Toast.makeText(Signup_activity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT);
//                        }
//
//                        @Override
//                        public void onCodeSent(@NonNull String verificationId,
//                                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                            Intent intent=new Intent(Signup_activity.this,Otp_Activity.class);
//                            intent.putExtra("number",Number);
//                            intent.putExtra("name",Name);
//                            intent.putExtra("password", Confirm_password);
//                            intent.putExtra("verificationId",verificationId);
//                            startActivity(intent);
//
//
//                        }
//                    };
//                    PhoneAuthOptions options =
//                            PhoneAuthOptions.newBuilder(auth)
//                                    .setPhoneNumber("+91"+ Number)       // Phone number to verify
//                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                                    .setActivity(Signup_activity.this)                 // Activity (for callback binding)
//                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                                    .build();
//                    PhoneAuthProvider.verifyPhoneNumber(options);
//                }
            }
        });
    }
    public  void otpsent(FirebaseAuth mauth, String number, String name, String confirm_password) {

    }
}