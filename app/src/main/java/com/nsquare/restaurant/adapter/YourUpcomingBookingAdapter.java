package com.nsquare.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.model.UpcomingYourBookingModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class YourUpcomingBookingAdapter extends RecyclerView.Adapter<YourUpcomingBookingAdapter.MyViewHolder> {

    private ArrayList<UpcomingYourBookingModel> upcomingYourBookingModelArrayList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView list_item_upcoming_booking_imageview_profile_pic;
        public TextView list_item_upcoming_booking_textview_restro_name;
        public TextView list_item_upcoming_booking_textview_address;
        public TextView list_item_upcoming_booking_textview_date_time;
        public TextView list_item_upcoming_booking_textview_status;

        public MyViewHolder(View view)
        {
            super(view);
            list_item_upcoming_booking_imageview_profile_pic = (ImageView) view.findViewById(R.id.list_item_upcoming_booking_imageview_profile_pic);
            list_item_upcoming_booking_textview_restro_name = (TextView) view.findViewById(R.id.list_item_upcoming_booking_textview_restro_name);
            list_item_upcoming_booking_textview_address = (TextView) view.findViewById(R.id.list_item_upcoming_booking_textview_address);
            list_item_upcoming_booking_textview_date_time = (TextView) view.findViewById(R.id.list_item_upcoming_booking_textview_date_time);
            list_item_upcoming_booking_textview_status = (TextView) view.findViewById(R.id.list_item_upcoming_booking_textview_status);
        }
    }


    public YourUpcomingBookingAdapter(Context context, ArrayList<UpcomingYourBookingModel> upcomingYourBookingModelArrayList) {
        this.upcomingYourBookingModelArrayList = upcomingYourBookingModelArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_upcoming_booking, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        UpcomingYourBookingModel issueItem= upcomingYourBookingModelArrayList.get(position);
        if(issueItem.getImage().equalsIgnoreCase("")){
        }else{
            Picasso.with(context).load(issueItem.getImage()).placeholder(R.drawable.app_icon).into(holder.list_item_upcoming_booking_imageview_profile_pic);
        }
        holder.list_item_upcoming_booking_textview_restro_name.setText(issueItem.getRestroName());
        holder.list_item_upcoming_booking_textview_address.setText(issueItem.getAddress());
        holder.list_item_upcoming_booking_textview_date_time.setText(issueItem.getDateTime());

        if (issueItem.getStatus().equalsIgnoreCase("1")){
            holder.list_item_upcoming_booking_textview_status.setText(context.getResources().getString(R.string.confirmed));
            holder.list_item_upcoming_booking_textview_status.setTextColor(context.getResources().getColor(R.color.green_light));
        }else if (issueItem.getStatus().equalsIgnoreCase("0")){
            holder.list_item_upcoming_booking_textview_status.setText(context.getResources().getString(R.string.pending));
            holder.list_item_upcoming_booking_textview_status.setTextColor(context.getResources().getColor(R.color.yellow_pending));
        }else if (issueItem.getStatus().equalsIgnoreCase("2")){
            holder.list_item_upcoming_booking_textview_status.setText(context.getResources().getString(R.string.canceled));
            holder.list_item_upcoming_booking_textview_status.setTextColor(context.getResources().getColor(R.color.red_canceled));
        }

    }

    @Override
    public int getItemCount() {
        return upcomingYourBookingModelArrayList.size();
    }
}
