package com.example.crcle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.crcle.Adapter.Userpost_adapter;
import com.example.crcle.DBqueries;
import com.example.crcle.Model.Userpost_model;
import com.example.crcle.R;

import java.util.ArrayList;
import java.util.List;

public class UserPosts_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public UserPosts_Fragment() {
        // Required empty public constructor
    }

    public static UserPosts_Fragment newInstance(String param1, String param2) {
        UserPosts_Fragment fragment = new UserPosts_Fragment();
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
    public static List<Userpost_model> userpost_modelList;
    RecyclerView User_post_Rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_posts_, container, false);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Bundle bundle=getArguments();
                GridView Post_grid=view.findViewById(R.id.post_grid);
                //User_post_Rv=view.findViewById(R.id.user_post_rv);
                userpost_modelList=new ArrayList<>();
                Userpost_adapter userpost_adapter=new Userpost_adapter(userpost_modelList,getContext());
                Post_grid.setAdapter(userpost_adapter);
                String data=bundle.get("userID").toString();
                Log.d("data",data);
                DBqueries.User_all_posts(userpost_adapter,data);
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();

        return view;
    }
}