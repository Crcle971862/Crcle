package com.example.crcle;

//import static com.example.crcle.Cloud_notification.Fcm_notification.Comments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.crcle.Adapter.Comment_adapter;
import com.example.crcle.Model.Comment_model;
import com.example.crcle.Model.appNotification;
import com.example.crcle.Notification_model.Data;
import com.example.crcle.Notification_model.Fcm_Reqquest;
import com.example.crcle.Notification_model.Myresponse;
import com.example.crcle.fragments.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Comment_Activity  extends AppCompatActivity {
    Intent intent;
    ImageView image,postbtn,Heart,Post_likes;
    TextView Comment,cmnt_count,likes_count,post_at;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    RecyclerView Comments_Rv;
    Comment_adapter adapter;
    private NotificationManagerCompat notificationManagerCompat;
    Interface anInterface;
    public static final String Example="example";
    String postid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        image=findViewById(R.id.img);
        postbtn=findViewById(R.id.comment_post_btn);
        Comment=findViewById(R.id.comment);
        cmnt_count=findViewById(R.id.total_comments);
        likes_count=findViewById(R.id.total_likes);
        auth=FirebaseAuth.getInstance();
        Heart=findViewById(R.id.heart);
        Post_likes=findViewById(R.id.post_like);
        firebaseDatabase=FirebaseDatabase.getInstance();
        Comments_Rv=findViewById(R.id.comments_rv);
        notificationManagerCompat=NotificationManagerCompat.from(this);
        List<Comment_model> list=new ArrayList<>();
        postid=getIntent().getStringExtra("postid");
        String postby=getIntent().getStringExtra("postby");
//        cmnt_count.setText(getIntent().getStringExtra("cmnt")+" comments");
//        likes_count.setText(getIntent().getStringExtra("likes")+" likes");
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        Comments_Rv.setLayoutManager(layoutManager);
        adapter=new Comment_adapter(list,this);
        Comments_Rv.setAdapter(adapter);
        android.app.Notification Notify;
        anInterface=Client.getclient("https://fcm.googleapis.com").create(Interface.class);
        FirebaseFirestore.getInstance().collection("posts").document(postid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Glide.with(Comment_Activity.this).load(documentSnapshot.getString("image")).into(image);
            }
        });
        getLikes_CommentsCount();


        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getcomment=Comment.getText().toString();
                if(!getcomment.isEmpty()){
                    //postbtn.setVisibility(View.VISIBLE);
                    Comment_model comment_model=new Comment_model();
                    comment_model.setCmnt_body(getcomment);
                    comment_model.setPostat(new Date().getTime());
                    comment_model.setCmnt_by(auth.getUid());
//                    comment_model.setSeen(false);
//                    comment_model.setPost_id(postid);
                    firebaseDatabase.getReference("posts").child(postby).child(postid).child("comments").push().setValue(comment_model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Comment.setText("");
                            FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String name=snapshot.getValue(String.class);
                                    Data data=new Data(name,getcomment);
                                    if (!postby.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                        FirebaseDatabase.getInstance().getReference().child("User").child(postby).child("fcm_token").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String id=snapshot.getValue(String.class);
                                                Fcm_Reqquest fcm_reqquest=new Fcm_Reqquest(id,data);
                                                Log.d("alok",snapshot.getValue(String.class));
                                                anInterface.Fcm_request(fcm_reqquest).enqueue(new Callback<Myresponse>() {
                                                    @Override
                                                    public void onResponse(Call<Myresponse> call, Response<Myresponse> response) {
                                                        if (response.code()==200){
                                                            if (response.body().success!=1){
                                                                Toast.makeText(Comment_Activity.this,"failed",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Myresponse> call, Throwable t) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            if (!postby.equals(FirebaseAuth.getInstance().getUid())){
//                                appNotification notification=new appNotification();
//                                notification.setNotificationAt(new Date().getTime());
//                                notification.setType("comment");
//                                notification.setPostID(postid);
//                                notification.setPostedBy(postby);
//                                notification.setNotificationBy(auth.getUid());
//                                notification.setCheckOpen(false);
//                                FirebaseFirestore.getInstance().collection("user").document(postby).collection("notification").add(notification);
                                //firebaseDatabase.getReference().child("notification").child(postby).push().setValue(notification);
                                Map<String,String> map=new HashMap<>();
                                map.put("time",String.valueOf(new Date().getTime()));
                                map.put("checkOpen",String.valueOf(false));
                                map.put("postBy",postby);
                                map.put("notificationBy",FirebaseAuth.getInstance().getUid());
                                map.put("type","comment");
                                map.put("postID",postid);
                                FirebaseFirestore.getInstance().collection("user").document(postby).collection("notification").add(map);
                            }
                        }
                    });
                }
            }
        });

        firebaseDatabase.getReference("posts").child(postby).child(postid).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Comment_model comment_model=dataSnapshot.getValue(Comment_model.class);
                    list.add(comment_model);
                }
                adapter.notifyDataSetChanged();

                long commentscount= snapshot.getChildrenCount();
                FirebaseFirestore.getInstance().collection("posts").document(postid).update("comments",String.valueOf(commentscount)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseFirestore.getInstance().collection("posts").document(postid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String commentscount = documentSnapshot.getString("comments");
                                    cmnt_count.setText(commentscount + " comments");
                                }
                            }
                        });

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //////////////////////////////////post_likes///////////////////////////////////
    firebaseDatabase.getReference("posts").child(postby).child(postid).child("likes").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Heart.setVisibility(View.VISIBLE);
                    Post_likes.setVisibility(View.INVISIBLE);
                    Heart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Heart.setVisibility(View.INVISIBLE);
                            Post_likes.setVisibility(View.VISIBLE);
                            FirebaseDatabase.getInstance().getReference("posts").child(postby).child(postid).child("likes").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    updatedLikecounts(postid,postby);
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Post_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Heart.setVisibility(View.VISIBLE);
                Post_likes.setVisibility(View.INVISIBLE);
                FirebaseDatabase.getInstance().getReference("posts").child(postby).child(postid).child("likes").child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updatedLikecounts(postid, postby);
                    }
                });
                FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.getValue(String.class);
                        Data data = new Data(name, "Liked your post");
                        if (!postby.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            FirebaseDatabase.getInstance().getReference().child("User").child(postby).child("fcm_token").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String id = snapshot.getValue(String.class);
                                    Fcm_Reqquest fcm_reqquest = new Fcm_Reqquest(id, data);
                                    Log.d("alok", snapshot.getValue(String.class));
                                    anInterface.Fcm_request(fcm_reqquest).enqueue(new Callback<Myresponse>() {
                                        @Override
                                        public void onResponse(Call<Myresponse> call, Response<Myresponse> response) {
                                            if (response.code() == 200) {
                                                if (response.body().success != 1) {
                                                    Toast.makeText(Comment_Activity.this, "failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Myresponse> call, Throwable t) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (!postby.equals(FirebaseAuth.getInstance().getUid())){
                    Map<String,String> map=new HashMap<>();
                    map.put("time",String.valueOf(new Date().getTime()));
                    map.put("checkOpen",String.valueOf(false));
                    map.put("postBy",postby);
                    map.put("notificationBy",FirebaseAuth.getInstance().getUid());
                    map.put("type","like");
                    map.put("postID",postid);
                    FirebaseFirestore.getInstance().collection("user").document(postby).collection("notification").add(map);

//                    appNotification notification=new appNotification();
//                    notification.setNotificationAt(new Date().getTime());
//                    notification.setType("comment");
//                    notification.setPostID(postid);
//                    notification.setPostedBy(postby);
//                    notification.setNotificationBy(auth.getUid());
//                    notification.setCheckOpen(false);
                //firebaseDatabase.getReference().child("notification").child(postby).push().setValue(notification);
                }
            }
        });

        //////////////////////////////////post_likes///////////////////////////////////

    }
    public void getLikes_CommentsCount(){

        FirebaseFirestore.getInstance().collection("posts").document(postid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String likescount=documentSnapshot.getString("likes");
                    likes_count.setText(likescount+" likes");
                    String commentscount = documentSnapshot.getString("comments");
                    cmnt_count.setText(commentscount + " comments");
                }
                HomeFragment.home_adapter.notifyDataSetChanged();

            }

        });
    }

    public void updatedLikecounts(String postid,String postby){
        FirebaseDatabase.getInstance().getReference("posts").child(postby).child(postid).child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long likescount= snapshot.getChildrenCount();
                FirebaseFirestore.getInstance().collection("posts").document(postid).update("likes",String.valueOf(likescount)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseFirestore.getInstance().collection("posts").document(postid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String likescount = documentSnapshot.getString("likes");
                                    likes_count.setText(likescount + " likes");
                                }
                            }
                        });

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        adapter.notifyDataSetChanged();
        Intent intent=new Intent(Comment_Activity.this,MainActivity.class);
        startActivity(intent);
    }
}