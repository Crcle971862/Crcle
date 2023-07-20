package com.example.crcle.fragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.crcle.Adapter.Highlights_Adapter;
import com.example.crcle.DBqueries;
import com.example.crcle.Edit_profile_Fragment;
import com.example.crcle.Loading_alert;
import com.example.crcle.Model.Highlights_model;
import com.example.crcle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
    RecyclerView Highlights_Rv;
    LinearLayout Total_follower,Total_following,Total_post;
    ImageView User_background,User_profile;
    TextView Name,Followers,Following,Posts,Bio;
    FirebaseStorage storage;
    FirebaseDatabase database;
    StorageReference storageReference;
    FirebaseFirestore firestoredatabase;
    public static List<Highlights_model> high_list;
    Button Edit_profile;
    String profile_img,cover_img,name;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);
        Highlights_Rv=view.findViewById(R.id.highlights_rv);
        Total_follower=view.findViewById(R.id.total_followers);
        Total_following=view.findViewById(R.id.total_following);
        User_background=view.findViewById(R.id.user_background);
        User_profile=view.findViewById(R.id.user_profile);
        Total_post=view.findViewById(R.id.total_post);
        storage=FirebaseStorage.getInstance();
        Name=view.findViewById(R.id.user_name);
        Followers=view.findViewById(R.id.followers);
        Following=view.findViewById(R.id.following);
        Posts=view.findViewById(R.id.posts);
        Edit_profile=view.findViewById(R.id.edit_profile);
        ImageView add_highlights;
        add_highlights=view.findViewById(R.id.highlight_btn);
        Bio=view.findViewById(R.id.user_bio);
        firestoredatabase=FirebaseFirestore.getInstance();
        high_list=new ArrayList<>();
        /////////////////////fetching user data//////////////////////
        firestoredatabase.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    profile_img=task.getResult().get("profile").toString();
                    cover_img=task.getResult().get("cover_img").toString();
                    name=task.getResult().get("name").toString();
                    Name.setText(name);
                    Glide.with(getActivity()).load(profile_img).into(User_profile);
                    Glide.with(getActivity()).load(cover_img).into(User_background);
                    Bio.setText(task.getResult().getString("bio"));
                }
            }
        });
        /////////////////////fetching user data//////////////////////

        //Loading_alert loading_alert=new Loading_alert(getActivity().);
        ///////////////////////moving to edit profile fragment//////////////////////////////////////////
        Edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("profile_img",profile_img);
                bundle.putString("cover_img",cover_img);
                bundle.putString("name",name);
                Edit_profile_Fragment edit_profile_fragment=new Edit_profile_Fragment();
                edit_profile_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,edit_profile_fragment,null).addToBackStack(null).commit();
            }
        });
        ///////////////////////moving to edit profile fragment//////////////////////////////////////////
        Total_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentManager fragmentManager;
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new Follower_Fragment(),null).addToBackStack(null)
                        .commit();
            }
        });
        Total_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new FollowingFragment(),null).addToBackStack(null)
                        .commit();
            }
        });
        Total_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =new Bundle();
                bundle.putString("userID",FirebaseAuth.getInstance().getUid());
                UserPosts_Fragment userPosts_fragment=new UserPosts_Fragment();
                userPosts_fragment.setArguments(bundle);
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,userPosts_fragment,null).addToBackStack(null).commit();
            }
        });



        /////////////////////update background image/////////////////
        ActivityResultLauncher<String> back_img=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result!=null){
                    User_background.setImageURI(result);
                    Dialog dialog=new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_layout);
                    dialog.show();
                    dialog.setCancelable(false);
                    Runnable back=new Runnable() {
                        @Override
                        public void run() {
                            StorageReference reference= storage.getReference().child("cover_photo").child(FirebaseAuth.getInstance().getUid());
                            reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getContext(),"cover photo saved",Toast.LENGTH_SHORT).show();
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            firestoredatabase.collection("user").document(FirebaseAuth.getInstance().getUid()).update("cover_img",uri.toString());
                                            dialog.cancel();

                                        }
                                    });
                                }
                            });
                        }
                    };
                    Thread backthread=new Thread(back);
                    backthread.start();

                }
            }
        });
        User_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_img.launch("image/*");
            }
        });
        /////////////////////update background image/////////////////

        ///////////////update profile///////////////////
        ActivityResultLauncher<String> profile=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result!=null){
                    Fragment fragment=new Fragment();
                    Bundle bundle=new Bundle();
                    User_profile.setImageURI(result);
                    Dialog dialog=new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_layout);
                    dialog.show();
                    dialog.setCancelable(false);
                    Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            Log.d("manish",Thread.currentThread().getName());
                            StorageReference reference= storage.getReference().child("profile").child(FirebaseAuth.getInstance().getUid());
                            reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getContext(),"profile saved",Toast.LENGTH_SHORT).show();
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Map<String,String> data=new HashMap<>();
                                            data.put("profile",uri.toString());
                                            firestoredatabase.collection("user").document(FirebaseAuth.getInstance().getUid()).update("profile",uri.toString());
                                            dialog.cancel();
                                        }
                                    });
                                }
                            });
                        }
                    };
                    Thread profile_thread=new Thread(runnable);
                    profile_thread.start();


                }
            }
        });


        User_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.launch("image/*");
            }
        });
        ///////////////update profile///////////////////

        /////////////////////////////add highlights/////////////////////

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        Highlights_Rv.setLayoutManager(layoutManager);
        Highlights_Adapter adapter=new Highlights_Adapter(high_list,getContext());
        Highlights_Rv.setAdapter(adapter);
        DBqueries.highlights(adapter);
        adapter.notifyDataSetChanged();
        ActivityResultLauncher<String> add=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_layout);
                dialog.show();
                dialog.setCancelable(false);
                Runnable highlight=new Runnable() {
                    @Override
                    public void run() {
                        StorageReference reference=storage.getReference().child("user_highlights").child(FirebaseAuth.getInstance().getUid()).child(new Date().getTime()+"");
                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String,String> map=new HashMap<>();
                                        map.put("image",uri.toString());
                                        map.put("caption","");
                                        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).collection("highlights").add(map);
                                        Toast.makeText(getContext(),"Highlight saved",Toast.LENGTH_SHORT).show();
                                        dialog.cancel();

                                    }
                                });
                            }
                        });
                    }
                };
                Thread highthread=new Thread(highlight);
                highthread.start();

            }
        });
        add_highlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.launch("image/*");
            }
        });
        /////////////////////////////add highlights/////////////////////
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        AggregateQuery count=FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("followers").count();
        count.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()){
                    AggregateQuerySnapshot snapshot= task.getResult();
                    Followers.setText(String.valueOf(snapshot.getCount()));
                    FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("followers",String.valueOf(snapshot.getCount()));

                }
            }
        });

        AggregateQuery followingcount=FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("following").count();
        followingcount.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()){
                    AggregateQuerySnapshot snapshot= task.getResult();
                    Following.setText(String.valueOf(snapshot.getCount()));
                    FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("followings",String.valueOf(snapshot.getCount()));
                }
            }
        });

        AggregateQuery postscount=FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("post").count();
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

//    public void totaldata(){
//        AggregateQuery count=FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("followers").count();
//        count.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    AggregateQuerySnapshot snapshot= task.getResult();
//                    Followers.setText(String.valueOf(snapshot.getCount()));
//                }
//            }
//        });
//    }
}