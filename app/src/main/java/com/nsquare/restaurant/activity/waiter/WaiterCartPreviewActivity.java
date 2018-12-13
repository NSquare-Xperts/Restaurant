package com.nsquare.restaurant.activity.waiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.LoginViaOTPActivity;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.adapter.MyCartItemsListAdapter;
import com.nsquare.restaurant.adapter.OrderHistoryAdapter;
import com.nsquare.restaurant.model.MyCartData;
import com.nsquare.restaurant.model.MyOrderHistory;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Pushkar on 23-04-2018.
 * Updated by Ritu Chavan on 12-06-2018.
 */
public class WaiterCartPreviewActivity extends ParentActivity implements MyCartItemsListAdapter.IsQuantityChanged{

    private RecyclerView fragment_recent_jobs_recycler_view;
    private OrderHistoryAdapter orderHistoryAdapter;
    //private ArrayList<MyCartData> cartModelArrayList = new ArrayList<MyCartData>();
    private MyCartItemsListAdapter cartItemsListAdapter;
    public static WaiterCartPreviewActivity cartPreviewActivity;
    private TextView activity_order_preview_textview_subtotal,fragment_common_list_recycler_button_checkout, activity_order_preview_textview_tax, activity_order_preview_textview_grand_total;
    private Button fragment_common_list_recycler_button_place_order;
    private double taxes = 0;
    double subTotal = 0, grandTotal = 0, taxAmount = 0;
    private EditText activity_book_a_table_edittext_name, activity_book_a_table_edittext_mobile_number;
    private String mobileNumber, userName, customer_id = "", customerOrderId = "",order_id="";
    private ArrayList<MyOrderHistory> orderHistoryModelArrayList = new ArrayList<MyOrderHistory>();
    boolean flagisTrue=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preview);

        setStatusBar();
        //setActionBarCustomWithBackLeftText(getResources().getString(R.string.order_preview));
        findViewByIds();

        fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
        //fragment_common_list_recycler_button_checkout.setVisibility(View.GONE);

        mobileNumber = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberMobileno),"");
        userName = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberUsername),"");
        //customer_id = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerId),"");
        //customerOrderId = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId),"");
        order_id = sharedPreferencesRemember.getString(getResources().getString(R.string.order_id), "");

        if(mobileNumber.equalsIgnoreCase(null) || mobileNumber.equalsIgnoreCase("")) {
            editor = sharedPreferencesRemember.edit();
            editor.putString(getResources().getString(R.string.sharedPreferenceRememberMobileno), activity_book_a_table_edittext_mobile_number.getText().toString());
            editor.putString(getResources().getString(R.string.sharedPreferenceRememberUsername), activity_book_a_table_edittext_name.getText().toString());

            editor.commit();
        }else {
            activity_book_a_table_edittext_name.setText(userName);
            activity_book_a_table_edittext_name.setEnabled(false);
            activity_book_a_table_edittext_mobile_number.setText(mobileNumber);
            activity_book_a_table_edittext_mobile_number.setEnabled(false);
        }
        cartPreviewActivity = this;
        MyCartItemsListAdapter.isQuantityChanged = (MyCartItemsListAdapter.IsQuantityChanged)cartPreviewActivity;
        ///
        //getInstance();
        //CartItemsListAdapter.updateValuesInterface = (CartItemsListAdapter.UpdateValuesInterface) cartPreviewActivity;

        //if(cartModelArrayList.size() > 0){
       /* cartItemsListAdapter = new MyCartItemsListAdapter(CartPreviewActivity.this,cartModelArrayList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
        fragment_recent_jobs_recycler_view.setAdapter(cartItemsListAdapter);*/


        if(internetConnection.isNetworkAvailable(getApplicationContext())){
            //fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
            //getCartList();
            getOrderListByCustomer();
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }

        fragment_common_list_recycler_button_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //move to verify OTP page if first time
                //check order-id is already present or not
                if(order_id.isEmpty()) {
                    Intent intent = new Intent(WaiterCartPreviewActivity.this, LoginViaOTPActivity.class);
                    startActivity(intent);
                }else {
                    //place order
                    placeOrder();
                }
            }
        });

        fragment_common_list_recycler_button_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call update cart API
               // addMenuToCart(cartModelArrayList.get(po),);
            }
        });
    }

    private void findViewByIds(){
        fragment_recent_jobs_recycler_view = (RecyclerView) findViewById(R.id.fragment_recent_jobs_recycler_view);
        activity_order_preview_textview_subtotal = (TextView) findViewById(R.id.activity_order_preview_textview_subtotal);
        activity_order_preview_textview_tax = (TextView) findViewById(R.id.activity_order_preview_textview_tax);
        activity_order_preview_textview_grand_total = (TextView) findViewById(R.id.activity_order_preview_textview_grand_total);
        fragment_common_list_recycler_button_checkout = (TextView) findViewById(R.id.fragment_common_list_recycler_button_checkout);

        fragment_common_list_recycler_button_place_order = (Button) findViewById(R.id.fragment_common_list_recycler_button_place_order);
        activity_book_a_table_edittext_name = (EditText) findViewById(R.id.activity_book_a_table_edittext_name);
        activity_book_a_table_edittext_mobile_number = (EditText) findViewById(R.id.activity_book_a_table_edittext_mobile_number);
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

    private boolean validateForm(){

        if(activity_book_a_table_edittext_name.getText().toString().trim().equalsIgnoreCase("")){
            activity_book_a_table_edittext_name.setError(getResources().getString(R.string.error_name));
            return false;
        }
        if(activity_book_a_table_edittext_mobile_number.getText().toString().trim().equalsIgnoreCase("")){
            activity_book_a_table_edittext_mobile_number.setError(getResources().getString(R.string.error_mobileno));
            return false;
        }
        return true;
    }

    private void placeOrder() {

        // swipe_refresh_layout.setRefreshing(true);
        HashMap<String, String> postParams = new HashMap<>();

        postParams.put(getResources().getString(R.string.field_order_id), mobileNumber);
        postParams.put(getResources().getString(R.string.field_dish_id), userName);
        postParams.put(getResources().getString(R.string.field_dish_quantity), Constants.table_id);
        postParams.put(getResources().getString(R.string.field_table), Constants.table_id);
        postParams.put(getResources().getString(R.string.field_dishes_extra_id), Constants.table_id);

        APIManager.requestPostMethod(WaiterCartPreviewActivity.this, getResources().getString(R.string.placeOrder_), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        String order_id = jsonObject.getString("order_id");
                    }else if (jsonObject.getInt("status") == 400) {
                        Toast.makeText(WaiterCartPreviewActivity.this, jsonObject.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //swipe_refresh_layout.setRefreshing(false);
            }
        });
    }
    //on cart update call
    private void addMenuToCart(String dish_id, String dish_qty, String table_no, String dish_extra_id,final BottomSheetDialog bottomSheetDialog) {

        showProcessingDialog();

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getResources().getString(R.string.field_dish),dish_id);
        postParams.put(getResources().getString(R.string.field_dish_quantity),dish_qty);
        postParams.put(getResources().getString(R.string.field_table),"1");
        // send ids by comma seperated and it is optional
        postParams.put(getResources().getString(R.string.field_dishes_extra_id), dish_extra_id);

        APIManager.requestPostMethod(WaiterCartPreviewActivity.this, getResources().getString(R.string.addCart), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        dismissProgressDialog();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(WaiterCartPreviewActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();

                        //refresh
                        if (internetConnection.isNetworkAvailable(WaiterCartPreviewActivity.this)) {
                            //getMenuListByType();
                        } else {
                            Toast.makeText(WaiterCartPreviewActivity.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                        }

                    }else if (jsonObject.getInt("status") == 400) {
                        dismissProgressDialog();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(WaiterCartPreviewActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    bottomSheetDialog.dismiss();
                    dismissProgressDialog();
                }
            }
        });
    }

    @Override
    public void checkQuantityIsChangedOrNot(String data) {
        //refresh page
        //TODO : call  update cart API else
        //call place order API
        if(data.equalsIgnoreCase("1")){
            flagisTrue= true;
            //getCartList();
            //add to cart api
            fragment_common_list_recycler_button_place_order.setVisibility(View.GONE);
            fragment_common_list_recycler_button_checkout.setVisibility(View.VISIBLE);
        }else {
            //place order
            fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
            fragment_common_list_recycler_button_checkout.setVisibility(View.GONE);
        }
    }

    private void getOrderListByCustomer() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getResources().getString(R.string.field_order_id), order_id);

        showProcessingDialog();
        APIManager.requestPostMethod(WaiterCartPreviewActivity.this, getResources().getString(R.string.getOrderDetails), postParams, new APIManager.VolleyCallback() {
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

                           // swipe_refresh_layout.setRefreshing(false);

                            orderHistoryAdapter = new OrderHistoryAdapter(WaiterCartPreviewActivity.this, orderHistoryModelArrayList);
                            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WaiterCartPreviewActivity.this);
                            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                            fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
                            fragment_recent_jobs_recycler_view.setAdapter(orderHistoryAdapter);
                            fragment_recent_jobs_recycler_view.setEnabled(true);
                            //swipe_refresh_layout.setRefreshing(false);

                        }else {
                            Toast.makeText(WaiterCartPreviewActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //swipe_refresh_layout.setRefreshing(false);
                dismissProgressDialog();
            }
        });
    }
}
