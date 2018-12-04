package com.nsquare.restaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.model.PhotosModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class PhotosAdapter extends BaseAdapter {

    private ArrayList<PhotosModel> connectionssItemArrayList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view)
        {
            super(view);


        }
    }


    public PhotosAdapter(Context context, ArrayList<PhotosModel> connectionssItemArrayList) {
        this.connectionssItemArrayList = connectionssItemArrayList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return connectionssItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return connectionssItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_photos, null);



        }
//        Resources r = Resources.getSystem();
//        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, r.getDisplayMetrics());

        ImageView list_item_photos_imageview_picture = (ImageView) convertView.findViewById(R.id.list_item_photos_imageview_picture);

        PhotosModel issueItem= connectionssItemArrayList.get(position);
        System.out.println("ProfilePIc"+issueItem.getPhotoUrl());

        if(issueItem.getPhotoUrl().equalsIgnoreCase("")){
        }else{
            Picasso.with(context).load(issueItem.getPhotoUrl()).placeholder(R.drawable.app_icon).into(list_item_photos_imageview_picture);
        }

        return convertView;
    }
}
