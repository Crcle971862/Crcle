package com.example.crcle;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.crcle.fragments.UserFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Edit_profile_Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Edit_profile_Fragment() {
        // Required empty public constructor
    }
    public static Edit_profile_Fragment newInstance(String param1, String param2) {
        Edit_profile_Fragment fragment = new Edit_profile_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    TextView Edit_profile,Edit_background;
    ImageView User_profile,User_background;
    EditText Name,Bio;
    Button Update_btn,Signout_btn;
    FirebaseStorage storage;
    FirebaseFirestore firestoredatabase;
    Uri Background,Profile;
    Dialog dialog1,dialog2;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_edit_profile_, container, false);
        Edit_profile=view.findViewById(R.id.edit_profile_btn);
        Edit_background=view.findViewById(R.id.edit_back_btn);
        User_profile=view.findViewById(R.id.user_profile);
        User_background=view.findViewById(R.id.user_background);
        Name=view.findViewById(R.id.name);
        Bio=view.findViewById(R.id.bio);
        Update_btn=view.findViewById(R.id.update_btn);
        Signout_btn=view.findViewById(R.id.sign_out_btn);
        storage=FirebaseStorage.getInstance();
        firestoredatabase=FirebaseFirestore.getInstance();
        Bundle bundle=getArguments();
        Glide.with(getContext()).load(bundle.getString("profile_img")).into(User_profile);
        Glide.with(getContext()).load(bundle.getString("cover_img")).into(User_background);
        Name.setText(bundle.getString("name"));
        dialog1=new Dialog(getContext());
        dialog2=new Dialog(getContext());

        ////////////////////////////////////Sign out function///////////////////////////////////////
        Signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(intent);
            }
        });
        ////////////////////////////////////Sign out function///////////////////////////////////////
        //////////////////////////////////////////////////update back image///////////////////////////////////////
        ActivityResultLauncher<String> back_img=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result!=null){
                    dialog1.setContentView(R.layout.dialog_layout);
                    dialog1.show();
                    dialog1.setCancelable(false);
                    updatebackground(result);
                }
            }
        });

        Edit_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_img.launch("image/*");
            }
        });
        //////////////////////////////////////////////////update back image///////////////////////////////////////

        /////////////////////////////////////////////////update profile image////////////////////////////////////
        ActivityResultLauncher<String> profile=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result!=null){
                    dialog2.setContentView(R.layout.dialog_layout);
                    dialog2.show();
                    dialog2.setCancelable(false);
                    updateprofile(result);
                }
            }
        });

        Edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.launch("image/*");
            }
        });
        /////////////////////////////////////////////////update profile image////////////////////////////////////


        Update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Name.getText().toString().equals("")){
                    String bio=Bio.getText().toString();
                    if (bio.equals("")||!bio.equals("")){
                        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).update("bio",bio);
                        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).update("name",Name.getText().toString());
                        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid()).child("name").setValue(Name.getText().toString());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new UserFragment()).commit();
                    }


                }
            }
        });

        return view;
    }
    public void updateprofile(Uri profile){
        Runnable runnable2=new Runnable() {
            @Override
            public void run() {
                StorageReference reference= storage.getReference().child("profile").child(FirebaseAuth.getInstance().getUid());
                reference.putFile(profile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(),"profile saved",Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map<String,String> data=new HashMap<>();
                                data.put("profile",uri.toString());
                                firestoredatabase.collection("user").document(FirebaseAuth.getInstance().getUid()).update("profile",uri.toString());
                                User_profile.setImageURI(profile);
                                dialog2.cancel();
                            }
                        });
                    }
                });
            }
        };
        Thread thread2=new Thread(runnable2);
        thread2.start();

    }
    public void updatebackground(Uri background){
        Runnable runnable1=new Runnable() {
            @Override
            public void run() {
                StorageReference reference= storage.getReference().child("cover_photo").child(FirebaseAuth.getInstance().getUid());
                reference.putFile(background).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(),"cover photo saved",Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                firestoredatabase.collection("user").document(FirebaseAuth.getInstance().getUid()).update("cover_img",uri.toString());
                                User_background.setImageURI(background);
                                dialog1.cancel();
                            }
                        });
                    }
                });
            }
        };
        Thread thread1=new Thread(runnable1);
        thread1.start();
    }

}