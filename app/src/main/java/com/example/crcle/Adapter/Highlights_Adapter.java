package com.example.crcle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crcle.Model.Highlights_model;
import com.example.crcle.R;

import java.util.List;

public class Highlights_Adapter extends RecyclerView.Adapter<Highlights_Adapter.viewholder> {
    List<Highlights_model> list;
    Context context;

    public Highlights_Adapter(List<Highlights_model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Highlights_Adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.highlights_stories,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Highlights_Adapter.viewholder holder, int position) {
        Highlights_model highlights_model=list.get(position);
        Glide.with(context).load(highlights_model.getImage()).into(holder.Images);
        holder.Detail.setText(highlights_model.getDetail());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView Images;
        TextView Detail;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            Images=itemView.findViewById(R.id.story_image);
            Detail=itemView.findViewById(R.id.detail);
        }
    }
}
