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
import android.widget.Toast;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.MenuListActivity;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.adapter.OffersListAdapter;
import com.nsquare.restaurant.adapter.RestroListAdapter;
import com.nsquare.restaurant.adapter.VegMenuListAdapter;
import com.nsquare.restaurant.model.OfferItem;
import com.nsquare.restaurant.model.RestroItem;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.InternetConnection;
import com.nsquare.restaurant.util.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class OffersFragment extends Fragment
{

    private RecyclerView fragment_recent_jobs_recycler_view;
    private ArrayList<OfferItem> speakersItemArrayList = new ArrayList<OfferItem>();
    private OffersListAdapter speakersListAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private InternetConnection internetConnection = new InternetConnection();


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

        if (internetConnection.isNetworkAvailable(getActivity())) {
            getMenuListByType();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }

    }

    private void getMenuListByType() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getResources().getString(R.string.masterType), getResources().getString(R.string.offers_key));

        //((ParentActivity) getActivity()).showProcessingDialog();
        APIManager.requestPostMethod(getActivity(), getResources().getString(R.string.getMastervalues), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        speakersItemArrayList.clear();

                        //Toast.makeText(getActivity(), object.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArrayMenuItems = jsonObject.getJSONArray(getResources().getString(R.string.masterType));
                        if (jsonArrayMenuItems.length() > 0) {

                            for(int i = 0; i < jsonArrayMenuItems.length(); i++){
                                JSONObject json = jsonArrayMenuItems.getJSONObject(i);
                                String offerId = json .getString(getActivity().getResources().getString(R.string.offerId));
                                String imageName = json .getString(getActivity().getResources().getString(R.string.imageName));

                                speakersItemArrayList.add(new OfferItem(offerId,  imageName));
                            }

                            speakersListAdapter = new OffersListAdapter(getActivity(), speakersItemArrayList, getActivity());
                            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                            fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
                            fragment_recent_jobs_recycler_view.setAdapter(speakersListAdapter);

                            fragment_recent_jobs_recycler_view.setEnabled(true);
                            swipe_refresh_layout.setRefreshing(false);

                        }else{
                            speakersListAdapter = new OffersListAdapter(getActivity(),speakersItemArrayList, getActivity());
                            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                            fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
                            fragment_recent_jobs_recycler_view.setAdapter(speakersListAdapter);

                            fragment_recent_jobs_recycler_view.setEnabled(true);
                            swipe_refresh_layout.setRefreshing(false);
                        }
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    swipe_refresh_layout.setRefreshing(false);
                    e.printStackTrace();
                }
                swipe_refresh_layout.setRefreshing(false);

            }
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        setListValues();
//    }

}
