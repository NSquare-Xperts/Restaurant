package com.nsquare.restaurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nsquare.restaurant.util.RecyclerItemClickListener;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.YourBookingDetailsActivity;
import com.nsquare.restaurant.adapter.YourUpcomingBookingAdapter;
import com.nsquare.restaurant.model.UpcomingYourBookingModel;

import java.util.ArrayList;


public class UpcomingYourBookingFragment extends Fragment
{

    private YourUpcomingBookingAdapter yourUpcomingBookingAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView fragment_recent_jobs_recycler_view;
    private ArrayList<UpcomingYourBookingModel> upcomingYourBookingModelArrayList = new ArrayList<UpcomingYourBookingModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_common_list_recycler, container, false);
        findViewByIds(rootView);

        setListValues();

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setListValues();
            }
        });

        fragment_recent_jobs_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intentForgotPassword = new Intent(getActivity(), YourBookingDetailsActivity.class);
                intentForgotPassword.putExtra(getResources().getString(R.string.key_status), upcomingYourBookingModelArrayList.get(position).getStatus());
                intentForgotPassword.putExtra(getResources().getString(R.string.key_flag), "0");
                startActivity(intentForgotPassword);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }));
        return rootView;
    }

    private void findViewByIds(View view){
        fragment_recent_jobs_recycler_view = (RecyclerView) view.findViewById(R.id.fragment_recent_jobs_recycler_view);
        swipe_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
    }

    private void setListValues(){

        upcomingYourBookingModelArrayList.clear();

        swipe_refresh_layout.setRefreshing(true);
        upcomingYourBookingModelArrayList.add(new UpcomingYourBookingModel("1", "Hard Rock Cafe", "12A, Sector 12, Main Road, Noida", "", "Wed 23 Sept, 2 Guests, 7:00 PM", "1"));
        upcomingYourBookingModelArrayList.add(new UpcomingYourBookingModel("1", "Hard Rock Cafe", "12A, Sector 12, Main Road, Noida", "", "Wed 23 Sept, 2 Guests, 7:00 PM", "1"));
        upcomingYourBookingModelArrayList.add(new UpcomingYourBookingModel("1", "Hard Rock Cafe", "12A, Sector 12, Main Road, Noida", "", "Wed 23 Sept, 2 Guests, 7:00 PM", "1"));
        upcomingYourBookingModelArrayList.add(new UpcomingYourBookingModel("1", "Hard Rock Cafe", "12A, Sector 12, Main Road, Noida", "", "Wed 23 Sept, 2 Guests, 7:00 PM", "1"));
        upcomingYourBookingModelArrayList.add(new UpcomingYourBookingModel("1", "Hard Rock Cafe", "12A, Sector 12, Main Road, Noida", "", "Wed 23 Sept, 2 Guests, 7:00 PM", "1"));
        upcomingYourBookingModelArrayList.add(new UpcomingYourBookingModel("1", "Hard Rock Cafe", "12A, Sector 12, Main Road, Noida", "", "Wed 23 Sept, 2 Guests, 7:00 PM", "1"));
        upcomingYourBookingModelArrayList.add(new UpcomingYourBookingModel("1", "Hard Rock Cafe", "12A, Sector 12, Main Road, Noida", "", "Wed 23 Sept, 2 Guests, 7:00 PM", "1"));

        yourUpcomingBookingAdapter = new YourUpcomingBookingAdapter(getActivity(),upcomingYourBookingModelArrayList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
        fragment_recent_jobs_recycler_view.setAdapter(yourUpcomingBookingAdapter);
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

        fragment_recent_jobs_recycler_view.setEnabled(true);
        swipe_refresh_layout.setRefreshing(false);
    }
}
