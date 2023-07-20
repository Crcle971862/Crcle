package com.example.crcle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.crcle.Adapter.Follower_Adapter;
import com.example.crcle.Adapter.Search_Adapter;
import com.example.crcle.Model.User;
import com.example.crcle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Follower_Fragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Follower_Fragment() {
        // Required empty public constructor
    }
    public static Follower_Fragment newInstance(String param1, String param2) {
        Follower_Fragment fragment = new Follower_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    RecyclerView follower_RV;
    ListView listView;
    androidx.appcompat.widget.SearchView searchview;
    List<User> userList;

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
        View view=inflater.inflate(R.layout.fragment_follower_, container, false);
        follower_RV=view.findViewById(R.id.follower_rv);
        searchview=view.findViewById(R.id.searchview);
        userList=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        follower_RV.setLayoutManager(layoutManager);
        Follower_Adapter follower_adapter=new Follower_Adapter(userList,getContext());
        follower_RV.setAdapter(follower_adapter);

        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).collection("followers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot:documentSnapshotList){
                    //Boolean follow=documentSnapshot.getBoolean("follow");
                    if (documentSnapshot.contains("follow")){
                        FirebaseFirestore.getInstance().collection("user").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Log.d("name",task.getResult().get("name").toString());
                                userList.add(new User(task.getResult().get("name").toString(),task.getResult().get("profile").toString(),task.getResult().getString("followers"), documentSnapshot.getId(),true));
                                follower_adapter.notifyDataSetChanged();
                            }
                        });
                    }
                    else {
                        FirebaseFirestore.getInstance().collection("user").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Log.d("name",task.getResult().get("name").toString());
                                userList.add(new User(task.getResult().get("name").toString(),task.getResult().get("profile").toString(),task.getResult().getString("followers"), documentSnapshot.getId(),false));
                                follower_adapter.notifyDataSetChanged();
                            }
                        });
                    }
//                    else {
//                        FirebaseFirestore.getInstance().collection("user").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                Log.d("name",task.getResult().get("name").toString());
//                                userList.add(new User(task.getResult().get("name").toString(),task.getResult().get("profile").toString(),"10k", documentSnapshot.getId(),false));
//                                follower_adapter.notifyDataSetChanged();
//                            }
//                        });
//                    }

                }
            }
        });


        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<User> list=new ArrayList<>();
                for (User user:userList){
                    if (user.getName().toLowerCase().contains(newText.toLowerCase())){
                        list.add(user);
                    }
                }
                follower_adapter.searchdatalist(list);
                return true;
            }
        });
        return view;
    }
}