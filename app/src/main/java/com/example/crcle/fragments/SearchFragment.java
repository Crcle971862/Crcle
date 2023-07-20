package com.example.crcle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.crcle.Adapter.Search_Adapter;
import com.example.crcle.Model.User;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    ListView listView;
    androidx.appcompat.widget.SearchView searchview;
    ArrayList name;
    RecyclerView Search_rv;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firestore;
    List<User> userList;
    public static String tag="search";
    //String total;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        //listView=view.findViewById(R.id.listview_search);
        searchview=view.findViewById(R.id.searchview);
        Search_rv=view.findViewById(R.id.search_list_rv);
        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firestore=FirebaseFirestore.getInstance();
        userList=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Search_rv.setLayoutManager(layoutManager);
        Search_Adapter search_adapter=new Search_Adapter(userList,getContext());
        Search_rv.setAdapter(search_adapter);
//        Runnable runnable=new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        };
//        Thread thread=new Thread(runnable);
//        thread.start();


        firestore.collection("user").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot:documentSnapshotList){
                    String image=documentSnapshot.getString("profile");
                    String name=documentSnapshot.getString("name");
                    String follower=documentSnapshot.getString("followers");
                    userList.add(new User(name,image,follower, documentSnapshot.getId(),"manish"));
                    search_adapter.notifyDataSetChanged();

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
                search_adapter.searchdatalist(list);
                return true;
            }
        });
        return view;
    }
}