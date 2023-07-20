package com.example.crcle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.crcle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class See_User_Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public See_User_Fragment() {
        // Required empty public constructor
    }

    public static See_User_Fragment newInstance(String param1, String param2) {
        See_User_Fragment fragment = new See_User_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    TextView Name;
    ImageView User_profile,User_background;
    Button Follow_btn,Unfollow_btn;
    FirebaseAuth auth;
    LinearLayout Total_post;
    TextView Followers,Following,Posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_see__user_, container, false);
        Bundle bundle=getArguments();
        //bundle=getArguments();
        Name=view.findViewById(R.id.user_name);
        User_profile=view.findViewById(R.id.user_profile);
        User_background=view.findViewById(R.id.user_background);
        Follow_btn=view.findViewById(R.id.follow_btn);
        Unfollow_btn=view.findViewById(R.id.edit_profile);
        auth=FirebaseAuth.getInstance();
        Followers=view.findViewById(R.id.followers);
        Following=view.findViewById(R.id.following);
        Posts=view.findViewById(R.id.posts);
        Total_post=view.findViewById(R.id.total_post);
        String userid=bundle.get("userid").toString();

        total_data(userid);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                ////////////////////////moving to total post fragment////////////////////////////
                Total_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle1=new Bundle();
                        bundle1.putString("userID",userid);
                        UserPosts_Fragment userPosts_fragment=new UserPosts_Fragment();
                        userPosts_fragment.setArguments(bundle1);
                        Log.d("uud",userid);
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,userPosts_fragment,null).addToBackStack(null).commit();
                    }
                });
                ////////////////////////moving to total post fragment////////////////////////////
                FirebaseFirestore.getInstance().collection("user").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String profile_img=task.getResult().get("profile").toString();
                            String cover_img=task.getResult().get("cover_img").toString();
                            String name=task.getResult().get("name").toString();
                            Name.setText(name);
                            Glide.with(getActivity()).load(profile_img).into(User_profile);
                            Glide.with(getActivity()).load(cover_img).into(User_background);
                            //Picasso.get().load(profile_img).into(User_profile);
                        }
                    }
                });
                FirebaseFirestore.getInstance().collection("user").document(userid).collection("followers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documentSnapshotList=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot:documentSnapshotList){
                            if (documentSnapshot.getId().equals(auth.getUid())){
                                Unfollow_btn.setVisibility(View.VISIBLE);
                                Follow_btn.setVisibility(View.INVISIBLE);

                            }
                        }

                    }
                });
                Unfollow_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore.getInstance().collection("user").document(userid).collection("followers").document(auth.getUid()).delete();
                        Follow_btn.setVisibility(View.VISIBLE);
                        Unfollow_btn.setVisibility(View.INVISIBLE);
                        FirebaseFirestore.getInstance().collection("user").document(auth.getUid()).collection("following").document(userid).delete();

                    }
                });

                Follow_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Boolean> map=new HashMap<>();
                        map.put(auth.getUid(), true);
                        FirebaseFirestore.getInstance().collection("user").document(userid).collection("followers").document(auth.getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Unfollow_btn.setVisibility(View.VISIBLE);
                                Follow_btn.setVisibility(View.INVISIBLE);
                                Map<String,Boolean> following=new HashMap<>();
                                following.put(userid,true);
                                FirebaseFirestore.getInstance().collection("user").document(auth.getUid()).collection("following").document(userid).set(following);
                            }
                        });
                    }
                });
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();


        return view;
    }
    public void total_data(String userid){
        AggregateQuery count=FirebaseFirestore.getInstance().collection("user").document(userid).collection("followers").count();
        count.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()){
                    AggregateQuerySnapshot snapshot= task.getResult();
                    Followers.setText(String.valueOf(snapshot.getCount()));
                }
            }
        });

        AggregateQuery followingcount=FirebaseFirestore.getInstance().collection("user").document(userid).collection("following").count();
        followingcount.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()){
                    AggregateQuerySnapshot snapshot= task.getResult();
                    Following.setText(String.valueOf(snapshot.getCount()));
                }
            }
        });

        AggregateQuery postscount=FirebaseFirestore.getInstance().collection("user").document(userid).collection("post").count();
        postscount.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()){
                    AggregateQuerySnapshot snapshot= task.getResult();
                    Posts.setText(String.valueOf(snapshot.getCount()));
                }
            }
        });
    }

}
