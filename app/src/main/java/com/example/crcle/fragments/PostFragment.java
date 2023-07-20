package com.example.crcle.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.crcle.Model.Posts_model;
import com.example.crcle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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

public class PostFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PostFragment() {
        // Required empty public constructor
    }
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
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
    TextView caption;
    Button postbtn;
    ImageView postImg,add_img,profile_img;
    TextView Name,profession;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_post, container, false);
        caption=view.findViewById(R.id.post_description);
        postbtn=view.findViewById(R.id.post_btn);
        postImg=view.findViewById(R.id.post_img);
        add_img=view.findViewById(R.id.add_img);
        Name=view.findViewById(R.id.name);
        profile_img=view.findViewById(R.id.profile_image);
        firebaseFirestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        firebaseFirestore.collection("user").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Name.setText(task.getResult().get("name").toString());
                Glide.with(getContext()).load(task.getResult().get("profile").toString()).into(profile_img);
            }
        });

        ActivityResultLauncher<String> addimg=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result!=null){
                    uri=result;
                    add_img.setImageURI(result);
                    postbtn.setEnabled(true);
                    add_img.setVisibility(View.VISIBLE);
                }
            }
        });
        postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addimg.launch("image/*");
            }
        });
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_layout);
                dialog.show();
                dialog.setCancelable(false);
                String Caption=caption.getText().toString();
//                HomeFragment homeFragment=new HomeFragment();
//                getParentFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                StorageReference reference= storage.getReference().child("posts").child(FirebaseAuth.getInstance().getUid()).child(new Date().getTime()+"");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                List<Posts_model> list=new ArrayList<>();
//                                list.add(new Posts_model(uri.toString(),Caption,0,0));
                                Map<String,String> data=new HashMap<>();
                                data.put("image",uri.toString());
                                data.put("date",new Date().getTime()+"");
                                data.put("likes","0");
                                data.put("comments","0");
                                data.put("caption",Caption);
                                data.put("postby",FirebaseAuth.getInstance().getUid());

                                firebaseFirestore.collection("user").document(FirebaseAuth.getInstance().getUid()).collection("post").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        dialog.cancel();
                                        add_img.setImageURI(null);
                                        caption.setText("");
                                        Toast.makeText(getContext(),"post uploaded",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                firebaseFirestore.collection("posts").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });



        return view;
    }
}