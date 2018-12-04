package com.nsquare.restaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.BookATableActivity;
import com.nsquare.restaurant.model.OfferItem;
import com.nsquare.restaurant.model.RestroItem;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mobintia-android-developer-1 on 10/3/16.
 */

public class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.MyViewHolder> {

    private ArrayList<OfferItem> speakersItems;
    private Context context;
    private Activity myActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView list_item_speakers_imageview_profile_pic;
        public TextView list_item_speakers_textview_restro_name;
        public TextView list_item_speakers_textview_address;
        public TextView list_item_speakers_textview_rate;
        public TextView list_item_speakers_textview_dishes;
        public TextView list_item_speakers_textview_order_online;
        public TextView list_item_speakers_textview_book_a_table;
        public RatingBar activity_restro_details_ratingbar_rating;


        public MyViewHolder(View view)
        {
            super(view);
            list_item_speakers_imageview_profile_pic = (ImageView) view.findViewById(R.id.list_item_speakers_imageview_profile_pic);
            list_item_speakers_textview_restro_name = (TextView) view.findViewById(R.id.list_item_speakers_textview_restro_name);
            list_item_speakers_textview_address = (TextView) view.findViewById(R.id.list_item_speakers_textview_address);
            list_item_speakers_textview_rate = (TextView) view.findViewById(R.id.list_item_speakers_textview_rate);
            list_item_speakers_textview_dishes = (TextView) view.findViewById(R.id.list_item_speakers_textview_dishes);
            list_item_speakers_textview_order_online = (TextView) view.findViewById(R.id.list_item_speakers_textview_order_online);
            list_item_speakers_textview_book_a_table = (TextView) view.findViewById(R.id.list_item_speakers_textview_book_a_table);
            activity_restro_details_ratingbar_rating = (RatingBar) view.findViewById(R.id.activity_restro_details_ratingbar_rating);
        }
    }


    public OffersListAdapter(Context context, ArrayList<OfferItem> speakersItems, Activity myActivity) {
        this.speakersItems = speakersItems;
        this.context = context;
        this.myActivity = myActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_offers, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        OfferItem issueItem= speakersItems.get(position);
        if(issueItem.getOfferImage().equalsIgnoreCase("")){
        }else{
            Picasso.with(context).load(issueItem.getOfferImage()).placeholder(R.mipmap.ic_launcher).into(holder.list_item_speakers_imageview_profile_pic);
        }
    }

    @Override
    public int getItemCount() {
        return speakersItems.size();
    }
}