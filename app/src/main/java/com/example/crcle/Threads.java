package com.example.crcle;

import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Threads implements Runnable{
    @Override
    public void run() {
        //profile_thread(result);
//        public void profile_thread(Uri result){
//            StorageReference reference= FirebaseStorage.getInstance().getReference().child("profile").child(FirebaseAuth.getInstance().getUid());
//            reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    //Toast.makeText(getContext(),"profile saved",Toast.LENGTH_SHORT).show();
//                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            Map<String,String> data=new HashMap<>();
////                                   data.put("name","");
////                                   data.put("number","");
////                                   data.put("password","");
//                            data.put("profile",uri.toString());
//                            FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).update("profile",uri.toString());
//                        }
//                    });
//                }
//            });
//        }
    }

}
