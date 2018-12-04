package com.nsquare.restaurant.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.adapter.ReviewsAdapter;
import com.nsquare.restaurant.model.ReviewsModel;

import java.util.ArrayList;

/**
 * Created by Pushkar on 08-09-2017.
 */

public class AllReviewsActivity extends ParentActivity {

    private ReviewsAdapter reviewsAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView fragment_reviews_recycler_view;
    private ArrayList<ReviewsModel> reviewsModelArrayList = new ArrayList<ReviewsModel>();
    private TextView fragment_reviews_textview_add_review;
    private RelativeLayout fragment_reviews_relativelayout_bottom_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reviews);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.all_reviews));
        findViewByIds();

        setListValues();

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setListValues();
            }
        });
    }

    private void findViewByIds(){
        fragment_reviews_recycler_view = (RecyclerView) findViewById(R.id.fragment_reviews_recycler_view);
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        fragment_reviews_textview_add_review = (TextView) findViewById(R.id.fragment_reviews_textview_add_review);
        fragment_reviews_relativelayout_bottom_view = (RelativeLayout) findViewById(R.id.fragment_reviews_relativelayout_bottom_view);
        fragment_reviews_relativelayout_bottom_view.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setListValues(){

        reviewsModelArrayList.clear();

        swipe_refresh_layout.setRefreshing(true);
        reviewsModelArrayList.add(new ReviewsModel("1", "James Smith", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "DAY ONE"));
        reviewsModelArrayList.add(new ReviewsModel("1", "James Smith", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "DAY ONE"));
        reviewsModelArrayList.add(new ReviewsModel("1", "James Smith", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "DAY ONE"));
        reviewsModelArrayList.add(new ReviewsModel("1", "James Smith", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "DAY ONE"));
        reviewsModelArrayList.add(new ReviewsModel("1", "James Smith", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "DAY ONE"));

        reviewsAdapter = new ReviewsAdapter(getApplicationContext(),reviewsModelArrayList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
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
}
