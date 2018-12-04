package com.nsquare.restaurant.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nsquare.restaurant.activity.AddReviewActivity;
import com.nsquare.restaurant.activity.AllReviewsActivity;
import com.nsquare.restaurant.adapter.ReviewsAdapter;
import com.nsquare.restaurant.model.ReviewsModel;

import com.nsquare.restaurant.R;

import java.util.ArrayList;


public class ReviewsFragment extends Fragment
{

    private ReviewsAdapter reviewsAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView fragment_reviews_recycler_view;
    private ArrayList<ReviewsModel> reviewsModelArrayList = new ArrayList<ReviewsModel>();
    private TextView fragment_reviews_textview_add_review, fragment_reviews_textview_see_all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        findViewByIds(rootView);

        setListValues();

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setListValues();
            }
        });

        fragment_reviews_textview_add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPassword = new Intent(getActivity(), AddReviewActivity.class);
                startActivity(intentForgotPassword);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        fragment_reviews_textview_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPassword = new Intent(getActivity(), AllReviewsActivity.class);
                startActivity(intentForgotPassword);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return rootView;
    }

    private void findViewByIds(View view){
        fragment_reviews_recycler_view = (RecyclerView) view.findViewById(R.id.fragment_reviews_recycler_view);
        swipe_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        fragment_reviews_textview_add_review = (TextView) view.findViewById(R.id.fragment_reviews_textview_add_review);
        fragment_reviews_textview_see_all = (TextView) view.findViewById(R.id.fragment_reviews_textview_see_all);
    }

    private void setListValues(){

        reviewsModelArrayList.clear();

        swipe_refresh_layout.setRefreshing(true);
        reviewsModelArrayList.add(new ReviewsModel("1", "James Smith", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "DAY ONE"));
        reviewsModelArrayList.add(new ReviewsModel("1", "James Smith", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "DAY ONE"));
        reviewsModelArrayList.add(new ReviewsModel("1", "James Smith", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "DAY ONE"));
        reviewsModelArrayList.add(new ReviewsModel("1", "James Smith", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "DAY ONE"));
        reviewsModelArrayList.add(new ReviewsModel("1", "James Smith", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "DAY ONE"));

        reviewsAdapter = new ReviewsAdapter(getActivity(),reviewsModelArrayList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        fragment_reviews_recycler_view.setLayoutManager(linearLayoutManager);
        fragment_reviews_recycler_view.setAdapter(reviewsAdapter);
//
////        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        // final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        // layoutManager = new LinearLayoutManager(this);
//        final MyLinearLayoutManager layoutManager = new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, getScreenHeight(getActivity()));
//        fragment_reviews_recycler_view.setLayoutManager(layoutManager);
//        fragment_reviews_recycler_view.setAdapter(reviewsAdapter);
//
//        fragment_reviews_recycler_view.setNestedScrollingEnabled(false); // Disables scrolling for RecyclerView, CustomLinearLayoutManager used instead of MyLinearLayoutManager
//        // recyclerView.setHasFixedSize(false);
//
//        fragment_reviews_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int visibleItemCount = layoutManager.getChildCount();
//                int totalItemCount = layoutManager.getItemCount();
//                int lastVisibleItemPos = layoutManager.findLastVisibleItemPosition();
//                Log.i("getChildCount", String.valueOf(visibleItemCount));
//                Log.i("getItemCount", String.valueOf(totalItemCount));
//                Log.i("lastVisibleItemPos", String.valueOf(lastVisibleItemPos));
//                if ((visibleItemCount + lastVisibleItemPos) >= totalItemCount) {
//                    Log.i("LOG", "Last Item Reached!");
//                }
//            }
//        });

        fragment_reviews_recycler_view.setEnabled(true);
        swipe_refresh_layout.setRefreshing(false);
    }

    private int getScreenHeight(Context context) {
        int measuredHeight;
        Point size = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            measuredHeight = size.y;
        } else {
            Display d = wm.getDefaultDisplay();
            measuredHeight = d.getHeight();
        }

        return measuredHeight;
    }
}
