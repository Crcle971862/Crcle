package com.example.crcle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crcle.Model.Story_Model;
import com.example.crcle.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Story_Adapter extends RecyclerView.Adapter<Story_Adapter.viewholder> {
    //ArrayList<Story_Model> story_models;
    List<Story_Model> list;
    Context context;

    public Story_Adapter(List<Story_Model> list, Context context) {
        //this.story_models = story_models;
        this.list=list;
        this.context = context;
    }

    @NonNull
    @Override
    public Story_Adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stories_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Story_Adapter.viewholder holder, int position) {
        Story_Model user=list.get(position);
        Picasso.get().load(user.getProfile_img()).into(holder.Story_image);
        holder.Story_Name.setText(user.getProfile_name());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView Story_image;
        TextView Story_Name;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            Story_image=itemView.findViewById(R.id.story_image);
            Story_Name=itemView.findViewById(R.id.detail);
        }
    }
}
