package com.example.crcle.fragments;

//import static com.example.crcle.DBqueries.postlist;
import static com.example.crcle.DBqueries.posts;
//import static com.example.crcle.DBqueries.posts_modelList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.crcle.Adapter.Home_Adapter;
import com.example.crcle.Adapter.Posts_Adapter;
import com.example.crcle.Model.Home_model;
import com.example.crcle.Model.Posts_model;
import com.example.crcle.Model.Story_Model;
import com.example.crcle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    RecyclerView home_rv;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ImageView home_profile;
    public static List<Home_model> home_modelList;
    public static Home_Adapter home_adapter;

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
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        home_rv=view.findViewById(R.id.home_RV);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        home_profile=view.findViewById(R.id.story_image);
        home_modelList=new ArrayList<>();
        List<Story_Model> story_modelList=new ArrayList<>();
//        home_modelList.add(new Home_model(0,story_modelList));
////        home_modelList.add(new Home_model(1,postlist,"jhj"));
////        home_modelList.add(new Home_model(1,postlist,"jhj"));
//        story_modelList.add(new Story_Model("manish",R.drawable.prisma));
//        story_modelList.add(new Story_Model("manish",R.drawable.prisma));
//        story_modelList.add(new Story_Model("manish",R.drawable.prisma));
//        story_modelList.add(new Story_Model("manish",R.drawable.prisma));
//        story_modelList.add(new Story_Model("manish",R.drawable.prisma));
//        story_modelList.add(new Story_Model("manish",R.drawable.prisma));
//        story_modelList.add(new Story_Model("manish",R.drawable.prisma));




        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        home_rv.setLayoutManager(layoutManager);
        home_adapter=new Home_Adapter(home_modelList,getContext());
        home_rv.setAdapter(home_adapter);
        //home_adapter.notifyDataSetChanged();
        firebaseFirestore.collection("user").document(firebaseAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Glide.with(getActivity()).load(task.getResult().getString("profile")).into(home_profile);
                }
            }
        });
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                posts(home_adapter);

            }
        };

        Thread thread=new Thread(runnable);
        thread.start();

        return view;
    }
}