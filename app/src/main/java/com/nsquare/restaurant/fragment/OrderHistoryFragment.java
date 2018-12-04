package com.nsquare.restaurant.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nsquare.restaurant.activity.MakePaymentActivity;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.adapter.OrderHistoryAdapter;
import com.nsquare.restaurant.model.CartModel;
import com.nsquare.restaurant.model.OrderHistoryModel;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;
import com.nsquare.restaurant.util.InternetConnection;
import com.nsquare.restaurant.util.RecyclerItemClickListener;

import com.nsquare.restaurant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderHistoryFragment extends Fragment {
    private SharedPreferences sharedPreferencesRemember;
    private OrderHistoryAdapter orderHistoryAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView fragment_recent_jobs_recycler_view;
    private ArrayList<OrderHistoryModel> orderHistoryModelArrayList = new ArrayList<OrderHistoryModel>();
    private InternetConnection internetConnection = new InternetConnection();
    private String customer_id = "", customerOrderId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_list_recycler, container, false);
        findViewByIds(rootView);

        sharedPreferencesRemember = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        customer_id = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerId),"");
        customerOrderId = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId),"");

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
                Intent intent = new Intent(getActivity(), MakePaymentActivity.class);
                startActivity(intent);
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

        orderHistoryModelArrayList.clear();

        swipe_refresh_layout.setRefreshing(true);

        if(internetConnection.isNetworkAvailable(getActivity())){
            getOrderListByCustomer();
        }else{
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void getOrderListByCustomer() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getActivity().getResources().getString(R.string.customer_id), customer_id);
        postParams.put(getActivity().getResources().getString(R.string.customerOrderId), customerOrderId); //1 = Veg, 2 = Non Veg
        postParams.put(getActivity().getResources().getString(R.string.apiType), getActivity().getResources().getString(R.string.order_list)); //1 = Veg, 2 = Non Veg

        ((ParentActivity) getActivity()).showProcessingDialog();
        APIManager.requestPostMethod(getActivity(), getResources().getString(R.string.getOrderListByCustomer), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        JSONArray jsonArrayMenuItems = jsonObject.getJSONArray(getResources().getString(R.string.order_list));
                        if (jsonArrayMenuItems.length() > 0) {

                            for (int i = 0; i < jsonArrayMenuItems.length(); i++) {
                                JSONObject json = jsonArrayMenuItems.getJSONObject(i);
                                String order_id = json.getString(getActivity().getResources().getString(R.string.order_id));
                                String order_unique_id = json.getString(getActivity().getResources().getString(R.string.order_unique_id));

                                String total_amount = "";
                                if (json.getInt("order_status") == 0) {
                                    total_amount = json.getString(getActivity().getResources().getString(R.string.sub_total));
                                } else if (json.getInt("order_status") == 3) {
                                    total_amount = json.getString(getActivity().getResources().getString(R.string.total_amount));
                                }
                                String order_status = json.getString(getActivity().getResources().getString(R.string.order_status));
                                String created_at = json.getString(getActivity().getResources().getString(R.string.created_at));

                                orderHistoryModelArrayList.add(new OrderHistoryModel(order_id, order_unique_id, total_amount, order_status, created_at));
                            }

                            orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderHistoryModelArrayList, getActivity());
                            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                            fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
                            fragment_recent_jobs_recycler_view.setAdapter(orderHistoryAdapter);
                            fragment_recent_jobs_recycler_view.setEnabled(true);
                            swipe_refresh_layout.setRefreshing(false);

                        } else {
                            orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderHistoryModelArrayList, getActivity());
                            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                            fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
                            fragment_recent_jobs_recycler_view.setAdapter(orderHistoryAdapter);
                            fragment_recent_jobs_recycler_view.setEnabled(true);
                            swipe_refresh_layout.setRefreshing(false);
                        }
                    } else {
                        orderHistoryModelArrayList.clear();
                        //orderHistoryAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                swipe_refresh_layout.setRefreshing(false);
                ((ParentActivity) getActivity()).dismissProgressDialog();
            }
        });
    }
}
