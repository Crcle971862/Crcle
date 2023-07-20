package com.example.crcle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.crcle.Model.Notification;
import com.example.crcle.Comment_Activity;
import com.example.crcle.Model.appNotification;
import com.example.crcle.R;
import com.example.crcle.fragments.NotificationFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.viewholder> {
    List<appNotification> notification_modelList;
    Context context;

    public Notification_Adapter(List<appNotification> notification_modelList, Context context) {
        this.notification_modelList = notification_modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public Notification_Adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Notification_Adapter.viewholder holder, int position) {
        appNotification list=notification_modelList.get(position);
        FirebaseFirestore.getInstance().collection("user").document(list.getNotificationBy()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Glide.with(context).load(documentSnapshot.getString("profile")).into(holder.Profile_img);
//                holder.Follow.setVisibility(View.INVISIBLE);
//                holder.Unfollow.setVisibility(View.INVISIBLE);
                holder.Name.setText(documentSnapshot.getString("name"));
                if (list.getType().equals("comment")){
                    holder.Comment_type.setText("commented on your post");

                    if (list.isCheckOpen()==true){
                        holder.itemView.setBackgroundResource(R.drawable.background);
                    }
                }
                else if (list.getType().equals("like")){
                    holder.Comment_type.setText("liked your post");
                    if (list.isCheckOpen()==true){
                        holder.itemView.setBackgroundResource(R.drawable.background);
                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map=new HashMap<>();
                map.put("checkOpen","true");
                FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).collection("notification").document(list.getNotificationID()).update("checkOpen","true");
                Intent intent=new Intent(v.getContext(),Comment_Activity.class);
                intent.putExtra("postid",list.getPostID());
                intent.putExtra("postby",list.getPostedBy());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notification_modelList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView Profile_img,Follow,Unfollow;
        TextView Name,Comment_type,No_notification;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            Profile_img=itemView.findViewById(R.id.notification_profile);
            Name=itemView.findViewById(R.id.notification_name);
            Comment_type=itemView.findViewById(R.id.Notification_type);
            Follow=itemView.findViewById(R.id.follow);
            Unfollow=itemView.findViewById(R.id.unfollow);
            No_notification=itemView.findViewById(R.id.no_notification);
        }

    }
}
