package com.nsquare.restaurant.fragment;

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
import com.nsquare.restaurant.adapter.RestroListAdapter;
import com.nsquare.restaurant.model.RestroItem;

import java.util.ArrayList;


public class RestroListFragment extends Fragment
{

    private RecyclerView fragment_recent_jobs_recycler_view;
    private ArrayList<RestroItem> speakersItemArrayList = new ArrayList<RestroItem>();
    private RestroListAdapter speakersListAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;


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

            }
        }));

        return rootView;
    }

    private void findViewByIds(View rootView)
    {
        fragment_recent_jobs_recycler_view = (RecyclerView)rootView.findViewById(R.id.fragment_recent_jobs_recycler_view);
        swipe_refresh_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
    }

    private void setListValues(){

        swipe_refresh_layout.setRefreshing(true);
        fragment_recent_jobs_recycler_view.setEnabled(false);

        speakersItemArrayList.add(new RestroItem("1", "Streak Cafe", "West Delhi", "", "", ""));
        speakersItemArrayList.add(new RestroItem("2", "Streak Cafe", "West Delhi", "", "", ""));
        speakersItemArrayList.add(new RestroItem("1", "Streak Cafe", "West Delhi", "", "", ""));
        speakersItemArrayList.add(new RestroItem("1", "Streak Cafe", "West Delhi", "", "", ""));
        speakersItemArrayList.add(new RestroItem("1", "Streak Cafe", "West Delhi", "", "", ""));
        speakersItemArrayList.add(new RestroItem("1", "Streak Cafe", "West Delhi", "", "", ""));
        speakersItemArrayList.add(new RestroItem("1", "Streak Cafe", "West Delhi", "", "", ""));
        speakersItemArrayList.add(new RestroItem("1", "Streak Cafe", "West Delhi", "", "", ""));
        speakersItemArrayList.add(new RestroItem("1", "Streak Cafe", "West Delhi", "", "", ""));

        speakersListAdapter = new RestroListAdapter(getActivity(),speakersItemArrayList, getActivity());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
        fragment_recent_jobs_recycler_view.setAdapter(speakersListAdapter);

        fragment_recent_jobs_recycler_view.setEnabled(true);
        swipe_refresh_layout.setRefreshing(false);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        setListValues();
//    }

}
