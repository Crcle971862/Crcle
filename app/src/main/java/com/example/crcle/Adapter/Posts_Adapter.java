package com.example.crcle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crcle.Model.Posts_model;
import com.example.crcle.R;
import com.example.crcle.fragments.Comments_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Posts_Adapter extends RecyclerView.Adapter<Posts_Adapter.viewholder> {
    List<Posts_model> posts_modelList;
    Context context;

    public Posts_Adapter(List<Posts_model> posts_modelList,Context context) {
        this.posts_modelList = posts_modelList;
        this.context=context;
    }

    @NonNull
    @Override
    public Posts_Adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Posts_Adapter.viewholder holder, int position) {
        Posts_model list=posts_modelList.get(position);
        FirebaseFirestore.getInstance().collection("user").document(list.getPostby()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                holder.Post_name.setText(task.getResult().getString("name"));
                Picasso.get().load(task.getResult().getString("profile")).into(holder.Post_profile);
            }
        });
        Picasso.get().load(list.getPost_img()).into(holder.Post_image);
//     Picasso.get().load(list.getPost_comment()).into(holder.Post_comments);
        holder.Post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Toast.makeText(v.getContext(),"working",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return posts_modelList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView Post_name,Post_date,Total_likes,Total_comments;
        ImageView Post_profile,Post_image,Post_likes,Post_comments;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            Post_name=itemView.findViewById(R.id.post_name);
            Post_date=itemView.findViewById(R.id.post_date);
            Total_likes=itemView.findViewById(R.id.total_likes);
            Total_comments=itemView.findViewById(R.id.total_comments);
            Post_profile=itemView.findViewById(R.id.post_profile);
            Post_image=itemView.findViewById(R.id.post_image);
            Post_likes=itemView.findViewById(R.id.post_like);
            Post_comments=itemView.findViewById(R.id.post_comment);
        }
    }
}
