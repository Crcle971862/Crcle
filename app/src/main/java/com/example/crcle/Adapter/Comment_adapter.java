package com.example.crcle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crcle.Model.Comment_model;
import com.example.crcle.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Comment_adapter extends RecyclerView.Adapter<Comment_adapter.viewholder> {
    List<Comment_model> comment_modelList;
    Context context;

    public Comment_adapter(List<Comment_model> comment_modelList, Context context) {
        this.comment_modelList = comment_modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public Comment_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Comment_adapter.viewholder holder, int position) {
        Comment_model comment_model=comment_modelList.get(position);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore.getInstance().collection("user").document(comment_model.getCmnt_by()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Glide.with(context).load(documentSnapshot.getString("profile")).into(holder.profile);
                        String Name=documentSnapshot.getString("name");
                        String comment=comment_model.getCmnt_body();
                        //holder.Comment.setText(HtmlCompat.fromHtml("<b>"+documentSnapshot.getString("name"),HtmlCompat.FROM_HTML_MODE_LEGACY)+" :-    "+comment_model.getCmnt_body()); ///
                        ((viewholder)holder).date_time(String.valueOf(comment_model.getPostat()),Name,comment);
                    }
                });

            }
        };

        Thread thread=new Thread(runnable);
        thread.start();



    }

    @Override
    public int getItemCount() {
        return comment_modelList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView Name,Comment,Time;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            profile=itemView.findViewById(R.id.profile);
            Name=itemView.findViewById(R.id.name);
            Comment=itemView.findViewById(R.id.comments);
            Time=itemView.findViewById(R.id.time);
        }

        public void date_time(String date,String name,String comment){
            Comment.setText(comment);
            Long time=Long.parseLong(date);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat simpletimeFormat=new SimpleDateFormat("dd/MMM/yyyy",Locale.getDefault());

            String date_format=simpleDateFormat.format(time);
            String time_format=simpletimeFormat.format(time);

            Time.setText(HtmlCompat.fromHtml("<b>"+name,HtmlCompat.FROM_HTML_MODE_LEGACY)+"     "+date_format+"  "+time_format);
        }
    }
}
