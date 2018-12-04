package com.nsquare.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.model.ReviewsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private ArrayList<ReviewsModel> reviewsModelArrayList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView list_item_reviews_imageview_profile_pic;
        public TextView list_item_reviews_textview_person_name;
        public TextView list_item_reviews_textview_review;

        public MyViewHolder(View view)
        {
            super(view);
            list_item_reviews_imageview_profile_pic = (ImageView) view.findViewById(R.id.list_item_reviews_imageview_profile_pic);
            list_item_reviews_textview_person_name = (TextView) view.findViewById(R.id.list_item_reviews_textview_person_name);
            list_item_reviews_textview_review = (TextView) view.findViewById(R.id.list_item_reviews_textview_review);
        }
    }


    public ReviewsAdapter(Context context, ArrayList<ReviewsModel> reviewsModelArrayList) {
        this.reviewsModelArrayList = reviewsModelArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_reviews, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        ReviewsModel issueItem= reviewsModelArrayList.get(position);
        if(issueItem.getProfileImage().equalsIgnoreCase("")){
        }else{
            Picasso.with(context).load(issueItem.getProfileImage()).placeholder(R.drawable.app_icon).into(holder.list_item_reviews_imageview_profile_pic);
        }
        holder.list_item_reviews_textview_person_name.setText(issueItem.getUserName());
        holder.list_item_reviews_textview_review.setText(issueItem.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewsModelArrayList.size();
    }
}
