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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.CartPreviewActivity;
import com.nsquare.restaurant.activity.MainActivity;
import com.nsquare.restaurant.activity.MakePaymentActivity;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.adapter.OrderHistoryAdapter;
import com.nsquare.restaurant.model.MyOrderHistory;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;
import com.nsquare.restaurant.util.InternetConnection;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistoryFragment_Staff extends Fragment {

    private SharedPreferences sharedPreferencesRemember;
    private String sharedPreferencesRememberRole;
    private OrderHistoryAdapter orderHistoryAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView fragment_recent_jobs_recycler_view;
    private ArrayList<MyOrderHistory> orderHistoryModelArrayList = new ArrayList<MyOrderHistory>();
    private InternetConnection internetConnection = new InternetConnection();
    private String order_id="";
    private TextView textview_make_payment;
    private RelativeLayout relative_layout_checkout;
    private Button button_add_more;

    MenuItem addToCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_list_recycler, container, false);

        setHasOptionsMenu(true);
        //((ParentActivity)getActivity()).setActionBarCustomWithBackLeftText(getResources().getString(R.string.configure_device));

        findViewByIds(rootView);
        ((ParentActivity)getActivity()).setStatusBar();
        sharedPreferencesRemember = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPreferencesRememberRole = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencerole), "");

        order_id = sharedPreferencesRemember.getString(getResources().getString(R.string.order_id),"");
        relative_layout_checkout.setVisibility(View.VISIBLE);
        button_add_more.setVisibility(View.VISIBLE);
        if(sharedPreferencesRememberRole.equals("4")){
            //waiter login
            relative_layout_checkout.setVisibility(View.GONE);
            order_id = sharedPreferencesRemember.getString(getResources().getString(R.string.table_wise_order_id), "");
        }
        setListValues();

        textview_make_payment.setText(getResources().getString(R.string.make_payment));
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setListValues();
            }
        });

        relative_layout_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getActivity(),MakePaymentActivity.class);
                startActivity(intent);
            }
        });

        //add more
        button_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to main menu
                Intent intent = new Intent(getContext(),MainActivity.class);
                intent.putExtra(Constants.homeFromWaiter,Constants.homeFromWaiter);
                startActivity(intent);
            }
        });


        return rootView;
    }

    private void findViewByIds(View view){
        fragment_recent_jobs_recycler_view = (RecyclerView) view.findViewById(R.id.fragment_recent_jobs_recycler_view);
        swipe_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        //fragment_common_list_recycler_button_view_cart = (TextView) view.findViewById(R.id.fragment_common_list_recycler_button_view_cart);
        textview_make_payment = (TextView) view.findViewById(R.id.textview_make_payment);
        relative_layout_checkout = (RelativeLayout) view.findViewById(R.id.relative_layout_checkout);
        button_add_more = (Button) view.findViewById(R.id.button_add_more);
    }

    private void setListValues(){

        orderHistoryModelArrayList.clear();

        swipe_refresh_layout.setVisibility(View.VISIBLE);
        swipe_refresh_layout.setRefreshing(true);
        if(internetConnection.isNetworkAvailable(getActivity())){
            getOrderListByCustomer();
        }else{
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void getOrderListByCustomer() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getActivity().getResources().getString(R.string.field_order_id), order_id);

        ((ParentActivity) getActivity()).showProcessingDialog();
        APIManager.requestPostMethod(getActivity(), getResources().getString(R.string.getOrderDetails), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        String jsonOutput = jsonObject.getJSONArray(Constants.data).toString();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<MyOrderHistory>>(){}.getType();
                        orderHistoryModelArrayList = gson.fromJson(jsonOutput, listType);
                        if (orderHistoryModelArrayList.size() > 0) {

                            swipe_refresh_layout.setRefreshing(false);

                            orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderHistoryModelArrayList);
                            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                            fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
                            fragment_recent_jobs_recycler_view.setAdapter(orderHistoryAdapter);
                            fragment_recent_jobs_recycler_view.setEnabled(true);
                            swipe_refresh_layout.setRefreshing(false);

                        }else {
                            Toast.makeText(getActivity(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                swipe_refresh_layout.setRefreshing(false);
                ((ParentActivity) getActivity()).dismissProgressDialog();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_add_to_cart, menu);
        setHasOptionsMenu(true);
        addToCart = menu.findItem(R.id.addToCart);
        addToCart.setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addToCart) {
             Intent intent = new Intent(getContext(),CartPreviewActivity.class);
             startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
