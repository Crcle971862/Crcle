package com.example.crcle.fragments;

import android.content.Context;
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

import com.example.crcle.Adapter.Follower_Adapter;
import com.example.crcle.Adapter.Following_Adapter;
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

public class FollowingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FollowingFragment() {
        // Required empty public constructor
    }

    public static FollowingFragment newInstance(String param1, String param2) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    RecyclerView following_RV;
    androidx.appcompat.widget.SearchView searchview;
    public List<User> userList;

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
        View view= inflater.inflate(R.layout.fragment_following, container, false);
        following_RV=view.findViewById(R.id.following_rv);
        searchview=view.findViewById(R.id.searchview);
        userList=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        following_RV.setLayoutManager(layoutManager);
        Following_Adapter following_adapter=new Following_Adapter(userList,getContext());
        following_RV.setAdapter(following_adapter);

        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).collection("following").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot:documentSnapshotList){
                    FirebaseFirestore.getInstance().collection("user").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Log.d("name",task.getResult().get("name").toString());
                            userList.add(new User(task.getResult().get("name").toString(),task.getResult().get("profile").toString(),task.getResult().getString("followers"), documentSnapshot.getId(),"manish"));
                            following_adapter.notifyDataSetChanged();

                        }
                    });

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
                following_adapter.searchdatalist(list);
                return true;
            }
        });
        return view;
    }
    public static void refresh(List<User> userList, Context context){
        new Following_Adapter(userList,context).notifyDataSetChanged();
    }
}