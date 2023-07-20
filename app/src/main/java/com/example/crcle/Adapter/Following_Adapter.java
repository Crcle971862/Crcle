package com.example.crcle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.crcle.Model.Notification;
import com.bumptech.glide.Glide;
import com.example.crcle.Model.User;
import com.example.crcle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Following_Adapter extends RecyclerView.Adapter<Following_Adapter.viewholder> {
    List<User> list;
    Context context;

    public Following_Adapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Following_Adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Following_Adapter.viewholder holder, int position) {
        User user=list.get(position);
        Glide.with(context).load(user.getUser_image()).into(holder.profile_img);
        holder.name.setText(user.getName());
        if (user.getFollower().equals("")){
            holder.caption.setText("0 follower");
        }
        else {
            holder.caption.setText(list.get(position).getFollower()+" followers");

        }
        //holder.caption.setText(user.getFollower());
        //holder.follow.setVisibility(View.INVISIBLE);
        holder.unfollow.setVisibility(View.VISIBLE);
        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).collection("following").document(user.getUserid()).delete();
                FirebaseFirestore.getInstance().collection("user").document(user.getUserid()).collection("followers").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).delete();
                //notifyDataSetChanged();
                holder.unfollow.setVisibility(View.INVISIBLE);
                holder.follow.setVisibility(View.VISIBLE);
            }
        });
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Boolean> map=new HashMap<>();
                map.put(user.getUserid(),true);
                FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).collection("following").document(user.getUserid()).set(map);
                Map<String,Boolean> follow=new HashMap<>();
                follow.put(FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
                FirebaseFirestore.getInstance().collection("user").document(user.getUserid()).collection("followers").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(follow);
                holder.follow.setVisibility(View.INVISIBLE);
                holder.unfollow.setVisibility(View.VISIBLE);
            }
        });
        //notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void searchdatalist(ArrayList<User> searchlist){
        list=searchlist;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        ImageView profile_img,follow,unfollow;
        TextView name,caption;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            profile_img=itemView.findViewById(R.id.user_img);
            follow=itemView.findViewById(R.id.follow_btn);
            name=itemView.findViewById(R.id.name);
            caption=itemView.findViewById(R.id.followers);
            unfollow=itemView.findViewById(R.id.edit_profile);
        }
    }
}
