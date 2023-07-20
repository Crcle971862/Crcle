package com.example.crcle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crcle.Model.User;
import com.example.crcle.R;
import com.example.crcle.fragments.HomeFragment;
import com.example.crcle.fragments.SearchFragment;
import com.example.crcle.fragments.See_User_Fragment;
import com.example.crcle.fragments.UserFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.viewholder> {

    List<User> userList;
    Context context;
    AdapterView.OnItemClickListener itemClickListener;

    public Search_Adapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public Search_Adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Search_Adapter.viewholder holder, int position) {
        User list=userList.get(position);
        Picasso.get().load(list.getUser_image()).into(holder.User_img);
        holder.Name.setText(list.getName());
        if (list.getFollower().equals("")){
            holder.Follower.setText("0 follower");
        }
        else {
            holder.Follower.setText(list.getFollower()+" followers");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    FragmentManager fragmentTransaction=((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction user=fragmentTransaction.beginTransaction();
                    user.replace(R.id.container,new UserFragment()).addToBackStack(SearchFragment.tag);
                    user.commit();
                }
                else {
                    Bundle bundle=new Bundle();
                    bundle.putString("userid", list.getUserid());
                    See_User_Fragment see_user_fragment=new See_User_Fragment();
                    see_user_fragment.setArguments(bundle);
                    FragmentManager fragment=((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction see_user=fragment.beginTransaction();
                    see_user.replace(R.id.container,see_user_fragment);
                    see_user.commit();
                    Log.d("id",list.getUserid());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public void searchdatalist(ArrayList<User> searchlist){
        userList=searchlist;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView User_img,Follow_btn;
        TextView Name,Follower;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            User_img=itemView.findViewById(R.id.user_img);
            Name=itemView.findViewById(R.id.name);
            Follower=itemView.findViewById(R.id.followers);
        }
    }
}
