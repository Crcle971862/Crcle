package com.example.crcle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.crcle.Model.Userpost_model;
import com.example.crcle.R;

import java.util.List;

public class Userpost_adapter extends BaseAdapter {
    List<Userpost_model> userpost_modelList;
    Context context;

    public Userpost_adapter(List<Userpost_model> userpost_modelList, Context context) {
        this.userpost_modelList = userpost_modelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userpost_modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.grid_post_layout,null);
        ImageView imageView=(ImageView)view.findViewById(R.id.user_post);
        Glide.with(context).load(userpost_modelList.get(position).getImg()).into(imageView);

        return view;
    }
}
