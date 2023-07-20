package com.example.crcle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crcle.Adapter.Notification_Adapter;
//import com.example.crcle.Model.Notification;
import com.example.crcle.Model.Home_model;
import com.example.crcle.Model.appNotification;
import com.example.crcle.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
    public static RecyclerView Notification_Rv;
    TextView No_notification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        Notification_Rv=view.findViewById(R.id.notification_rv);
        List<appNotification> notification_modelList=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        Notification_Rv.setLayoutManager(layoutManager);
        Notification_Adapter adapter=new Notification_Adapter(notification_modelList,getContext());
        Notification_Rv.setAdapter(adapter);
        No_notification=view.findViewById(R.id.no_notification);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                //DatabaseReference query =FirebaseDatabase.getInstance().getReference().child("notification").child(FirebaseAuth.getInstance();
                //FirebaseDatabase.getInstance().getReference().child("notification").child(FirebaseAuth.getInstance().getUid())
//                FirebaseDatabase.getInstance().getReference().child("notification").child(FirebaseAuth.getInstance().getUid()).orderByChild("notificationAt").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                            appNotification notification=dataSnapshot.getValue(appNotification.class);
//                            notification.setNotificationID(dataSnapshot.getKey());
//                            notification_modelList.add(notification);
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                //Query query= FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).collection("notification").orderBy("date", Query.Direction.DESCENDING);
                FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).collection("notification").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documentSnapshotList=queryDocumentSnapshots.getDocuments();
                        if (documentSnapshotList.isEmpty()){
                            Notification_Rv.setVisibility(View.INVISIBLE);
                            No_notification.setVisibility(View.VISIBLE);
                        }
                        else {
                            for (DocumentSnapshot documentSnapshot1:documentSnapshotList){
                                String notificationid=documentSnapshot1.getId();
                                Long time=Long.valueOf(documentSnapshot1.getString("time"));
                                Boolean checkOpen=Boolean.valueOf(documentSnapshot1.get("checkOpen").toString());
                                String postby=documentSnapshot1.getString("postBy");
                                String notificationby=documentSnapshot1.getString("notificationBy");
                                String postid=documentSnapshot1.getString("postID");
                                String type=documentSnapshot1.getString("type");
                                appNotification appNotification=new appNotification(notificationby,type,postid,postby, documentSnapshot1.getId(), time,checkOpen);
                                // appNotification.setNotificationID(documentSnapshot1.getId());
                                notification_modelList.add(appNotification);
                                adapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
                //adapter.notifyDataSetChanged();

            }
        };

        Thread thread=new Thread(runnable);
        thread.start();

        return view;
    }
}