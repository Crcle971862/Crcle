package com.example.crcle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crcle.Client;
import com.example.crcle.Comment_Activity;
import com.example.crcle.Interface;
import com.example.crcle.Model.Home_model;
//import com.example.crcle.Model.Notification;
import com.example.crcle.Model.Story_Model;
import com.example.crcle.Model.appNotification;
import com.example.crcle.Notification_model.Data;
import com.example.crcle.Notification_model.Fcm_Reqquest;
import com.example.crcle.Notification_model.Myresponse;
import com.example.crcle.R;
import com.example.crcle.fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Adapter extends RecyclerView.Adapter {
    List<Home_model> homeModelList;
    Context context;
    //RecyclerView.RecycledViewPool recycledViewPool;


    public Home_Adapter(List<Home_model> homeModelList, Context context) {
        this.homeModelList = homeModelList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (homeModelList.get(position).getGetviewtype()){
            case 0:
                return Home_model.user_story;
            case 1:
                return Home_model.posts;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case Home_model.user_story:
                View story= LayoutInflater.from(parent.getContext()).inflate(R.layout.stories_dashboard,parent,false);
                return new user_story(story);
            case Home_model.posts:
                View posts=LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_layout,parent,false);
                return new Post(posts);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homeModelList.get(position).getviewtype){
            case Home_model.user_story:
                List<Story_Model> story_modelList=homeModelList.get(position).getStory_modelList();
                ((user_story)holder).setuser_story(story_modelList);
                break;
            case Home_model.posts:
                Home_model list=homeModelList.get(position);
                //List<Posts_model> postsModelList=homeModelList.get(position).getPostsModelList();
                //Glide.with(context).load(list.getPost_img()).into()
                String image=list.getPost_img();
                String likes=list.getTotal_likes();
                String comments=list.getTotal_cmnts();
                String caption=list.getCaption();
                String userid=list.getPostby();
                String postid=list.getPostid();
                String postby=list.getPostby();
                String date=list.getDate();
                //List<Posts_model> posts_modelList=homeModelList.get(position).getPosts_modelList();
                ((Post)holder).setpost(image,likes,comments,caption,userid,postid,postby,date);
            default:
                return;
        }

    }
    @Override
    public int getItemCount() {
        return homeModelList.size();
    }

    public class user_story extends RecyclerView.ViewHolder {
        RecyclerView story_rv;
        TextView name;
        ImageView profile_image;
        public user_story(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.detail);
            profile_image=itemView.findViewById(R.id.story_image);
            story_rv=itemView.findViewById(R.id.storyrv);
        }
        public void  setuser_story(List<Story_Model> story_modelList){
            Story_Adapter story_adapter=new Story_Adapter(story_modelList,context);
            story_rv.setAdapter(story_adapter);
            LinearLayoutManager layoutManager=new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            story_rv.setLayoutManager(layoutManager);
            story_adapter.notifyDataSetChanged();
        }
    }
    /////////////////////posts/////////////////
    public  class Post extends  RecyclerView.ViewHolder{
        RecyclerView Post_rv;
        TextView Post_name,Post_date,Total_likes,Total_comments,Caption;
        ImageView Post_profile,Post_image,Post_likes,Post_comments,Heart;
        public Post(@NonNull View itemView) {
            super(itemView);
            Post_rv=itemView.findViewById(R.id.post_RV);
            Post_name=itemView.findViewById(R.id.post_name);
            Post_date=itemView.findViewById(R.id.post_date);
            Total_likes=itemView.findViewById(R.id.total_likes);
            Total_comments=itemView.findViewById(R.id.total_comments);
            Post_profile=itemView.findViewById(R.id.post_profile);
            Post_image=itemView.findViewById(R.id.post_image);
            Post_likes=itemView.findViewById(R.id.post_like);
            Post_comments=itemView.findViewById(R.id.post_comment);
            Caption=itemView.findViewById(R.id.caption);
            Heart=itemView.findViewById(R.id.heart);
        }

        public void setpost(String image, String likes,String comments,String caption,String userid,String postid,String postby,String date){
            date_time(date);
            Glide.with(context).load(image).into(Post_image);
            Total_likes.setText(likes+" likes");
            Total_comments.setText(comments+" comments");
            Caption.setText(caption);
            FirebaseFirestore.getInstance().collection("user").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        Glide.with(context).load(task.getResult().getString("profile")).into(Post_profile);
                        Post_name.setText(task.getResult().getString("name"));
                    }
                }
            });

            Post_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(),"working",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(v.getContext(), Comment_Activity.class);
                    intent.putExtra("postid",postid);
                    intent.putExtra("postby",postby);
                    intent.putExtra("cmnt",comments);
                    intent.putExtra("likes",likes);
                    v.getContext().startActivity(intent);
                }
            });
            FirebaseDatabase.getInstance().getReference("posts").child(postby).child(postid).child("likes").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
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
                                        setlikecount(postid,postby);
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
                    Post_likes.setVisibility(View.INVISIBLE);
                    Heart.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference("posts").child(postby).child(postid).child("likes").child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            setlikecount(postid,postby);
                        }
                    });
                    Interface anInterface= Client.getclient("https://fcm.googleapis.com").create(Interface.class);
                    FirebaseDatabase.getInstance().getReference("posts").child(postby).child(postid).child("likes").child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseFirestore.getInstance().collection("posts").document(postid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String name=snapshot.getValue(String.class);
                                                Data data=new Data(name,"Liked your post");
                                                if (!postby.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                                    FirebaseDatabase.getInstance().getReference().child("User").child(postby).child("fcm_token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String id=snapshot.getValue(String.class);
                                                            Fcm_Reqquest fcm_reqquest=new Fcm_Reqquest(id,data);
                                                            anInterface.Fcm_request(fcm_reqquest).enqueue(new Callback<Myresponse>() {
                                                                @Override
                                                                public void onResponse(Call<Myresponse> call, Response<Myresponse> response) {
                                                                    if (response.code()==200){
                                                                        if (response.body().success!=1){
                                                                            //Toast.makeText(Commment_Activity.this,"failed",Toast.LENGTH_SHORT).show();
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
                                        }
                                    }
                                }
                            });
                        }
                    });
                    HomeFragment.home_adapter.notifyDataSetChanged();
                }
            });

        }

        public void setlikecount(String postid,String postby){
            FirebaseDatabase.getInstance().getReference("posts").child(postby).child(postid).child("likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long commentscount= snapshot.getChildrenCount();
                    FirebaseFirestore.getInstance().collection("posts").document(postid).update("likes",String.valueOf(commentscount)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseFirestore.getInstance().collection("posts").document(postid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String likescount = documentSnapshot.getString("likes");
                                        Total_likes.setText(likescount + " likes");
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

        public void date_time(String date){
            Long time=Long.parseLong(date);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat simpletimeFormat=new SimpleDateFormat("dd:MMM:yyyy",Locale.getDefault());

            String date_format=simpleDateFormat.format(time);
            String time_format=simpletimeFormat.format(time);

            Post_date.setText(date_format+" "+time_format);
        }
    }
    /////////////////////posts/////////////////
}
