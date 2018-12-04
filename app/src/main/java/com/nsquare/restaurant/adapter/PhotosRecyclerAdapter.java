package com.nsquare.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.model.PhotosModel;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class PhotosRecyclerAdapter extends RecyclerView.Adapter<PhotosRecyclerAdapter.MyViewHolder> {

    private ArrayList<PhotosModel> photosModelArrayList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView list_item_photos_imageview_picture;

        public MyViewHolder(View view)
        {
            super(view);
            list_item_photos_imageview_picture = (ImageView) view.findViewById(R.id.list_item_photos_imageview_picture);
        }
    }

    public PhotosRecyclerAdapter(Context context, ArrayList<PhotosModel> photosModelArrayList) {
        this.photosModelArrayList = photosModelArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_photos, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        PhotosModel issueItem= photosModelArrayList.get(position);
        System.out.println("ProfilePIc"+issueItem.getPhotoUrl());

        if(issueItem.getPhotoUrl().equalsIgnoreCase("")){
        }else{
            Picasso.with(context).load(issueItem.getPhotoUrl()).placeholder(R.drawable.app_icon).into(holder.list_item_photos_imageview_picture);
        }

    }

    @Override
    public int getItemCount() {
        return photosModelArrayList.size();
    }
}
