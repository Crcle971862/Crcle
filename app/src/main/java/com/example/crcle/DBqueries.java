package com.example.crcle;

import android.content.Context;
import android.graphics.ColorSpace;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.crcle.Adapter.Highlights_Adapter;
import com.example.crcle.Adapter.Home_Adapter;
import com.example.crcle.Adapter.Posts_Adapter;
import com.example.crcle.Adapter.Userpost_adapter;
import com.example.crcle.Model.Highlights_model;
import com.example.crcle.Model.Home_model;
import com.example.crcle.Model.Posts_model;
import com.example.crcle.Model.Userpost_model;
import com.example.crcle.fragments.HomeFragment;
import com.example.crcle.fragments.UserFragment;
import com.example.crcle.fragments.UserPosts_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBqueries {
    public  static FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    //public static List<Home_model> postlist=new ArrayList<>();

    public static void posts(Home_Adapter home_adapter){
        Query query=firebaseFirestore.collection("posts").orderBy("date", Query.Direction.DESCENDING);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot1:documentSnapshotList){
                    String image=documentSnapshot1.getString("image");
                    String cap=documentSnapshot1.getString("caption");
                    String likes=documentSnapshot1.getString("likes");
                    String postBy=documentSnapshot1.getString("postby");
                    String comments=documentSnapshot1.getString("comments");
                    String postby=documentSnapshot1.getString("postby");
                    String date=documentSnapshot1.getString("date");
                    HomeFragment.home_modelList.add(new Home_model(1,image,cap,likes,comments,postBy,documentSnapshot1.getId(),date));
                    home_adapter.notifyDataSetChanged();
                }

            }
        });

    }
    public static void highlights(Highlights_Adapter highlights_adapter){
        firebaseFirestore.collection("user").document(FirebaseAuth.getInstance().getUid()).collection("highlights").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot:documentSnapshotList){
                    UserFragment.high_list.add(new Highlights_model(documentSnapshot.getString("image"),documentSnapshot.getString("caption")));
                    highlights_adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static void User_all_posts(Userpost_adapter userpost_adapter,String userid){
        firebaseFirestore.collection("user").document(userid).collection("post").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot:list){
                    UserPosts_Fragment.userpost_modelList.add(new Userpost_model(documentSnapshot.getString("image")));
                    userpost_adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
