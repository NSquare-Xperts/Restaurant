package com.nsquare.restaurant.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.nsquare.restaurant.adapter.MyCartItemsListAdapter;
import com.nsquare.restaurant.adapter.OrderHistoryAdapter;
import com.nsquare.restaurant.adapter.VegMenuListAdapter;
import com.nsquare.restaurant.databasehelper.DatabaseHelper;
import com.nsquare.restaurant.fragment.HomeFragment;
import com.nsquare.restaurant.model.CartModel;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.adapter.CartItemsListAdapter;
import com.nsquare.restaurant.model.MyCartData;
import com.nsquare.restaurant.model.MyOrderHistory;
import com.nsquare.restaurant.model.vegMenuList.VegMenuList_NewModel;
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
public class CartPreviewActivity extends ParentActivity implements MyCartItemsListAdapter.IsQuantityChanged {

    private android.support.v4.app.FragmentManager fragmentManager;
    private RecyclerView fragment_recent_jobs_recycler_view;
    private LinearLayout linear_layout_to_hide;
    private ArrayList<MyCartData> cartModelArrayList = new ArrayList<MyCartData>();
    private MyCartItemsListAdapter cartItemsListAdapter;
    public static CartPreviewActivity cartPreviewActivity;
    private TextView activity_order_preview_textview_subtotal,fragment_common_list_recycler_button_checkout, activity_order_preview_textview_tax, activity_order_preview_textview_grand_total;
    private Button fragment_common_list_recycler_button_place_order;
    private Button button_add_more;
    private double taxes = 0;
    double subTotal = 0, grandTotal = 0, taxAmount = 0;
    private EditText activity_book_a_table_edittext_name, activity_book_a_table_edittext_mobile_number;
    private String mobileNumber, userName, customer_id = "", customerOrderId = "",order_id="";
    boolean flagisTrue=false;
    private String cart_id ="";
    private ArrayList<String> cartListIds = new ArrayList<>();
    private String sharedPreferencerole = "";
    private ArrayList<MyOrderHistory> orderHistoryModelArrayList = new ArrayList<MyOrderHistory>();
    private TextView textview_no_record_found;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preview);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.order_preview));
        findViewByIds();

        sharedPreferencerole = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencerole), "");
        order_id = sharedPreferencesRemember.getString(getResources().getString(R.string.order_id), "");

        if (sharedPreferencerole.equalsIgnoreCase("4")) {
            //waiter login
            order_id = sharedPreferencesRemember.getString(getResources().getString(R.string.table_wise_order_id),"");
            button_add_more.setVisibility(View.GONE);
            linear_layout_to_hide.setVisibility(View.GONE);

            fragment_common_list_recycler_button_place_order.setText(getResources().getString(R.string.place_order));
            fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
            fragment_common_list_recycler_button_checkout.setVisibility(View.GONE);
        }else {
            fragment_common_list_recycler_button_place_order.setText(getResources().getString(R.string.checkout));
            fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
            fragment_common_list_recycler_button_checkout.setVisibility(View.GONE);
        }

        mobileNumber = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberMobileno),"");
        userName = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberUsername),"");
        //customer_id = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerId),"");
        //customerOrderId = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId),"");

        //tableId = sharedPreferencesRemember.getString(getResources().getString(R.string.tableId), "");

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

        if(internetConnection.isNetworkAvailable(getApplicationContext())){
            //fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
            getCartList();
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }

        fragment_common_list_recycler_button_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //move to verify OTP page if first time
                //check order-id is already present or not
                if (sharedPreferencerole.equalsIgnoreCase("4")) {
                    placeOrder();
                } else {
                    if (order_id.isEmpty()) {
                        Intent intent = new Intent(CartPreviewActivity.this, LoginViaOTPActivity.class);
                        intent.putExtra("cartIds", cart_id);
                        startActivity(intent);
                    } else {
                        //place order
                        placeOrder();
                    }
                }
            }
        });



    }

    private void findViewByIds(){
        fragment_recent_jobs_recycler_view = (RecyclerView) findViewById(R.id.fragment_recent_jobs_recycler_view);
        activity_order_preview_textview_subtotal = (TextView) findViewById(R.id.activity_order_preview_textview_subtotal);

        textview_no_record_found = (TextView) findViewById(R.id.textview_no_record_found);

        activity_order_preview_textview_tax = (TextView) findViewById(R.id.activity_order_preview_textview_tax);
        activity_order_preview_textview_grand_total = (TextView) findViewById(R.id.activity_order_preview_textview_grand_total);
        fragment_common_list_recycler_button_checkout = (TextView) findViewById(R.id.fragment_common_list_recycler_button_checkout);

        button_add_more = (Button) findViewById(R.id.button_add_more);
        fragment_common_list_recycler_button_place_order = (Button) findViewById(R.id.fragment_common_list_recycler_button_place_order);
        activity_book_a_table_edittext_name = (EditText) findViewById(R.id.activity_book_a_table_edittext_name);
        activity_book_a_table_edittext_mobile_number = (EditText) findViewById(R.id.activity_book_a_table_edittext_mobile_number);
        linear_layout_to_hide =  (LinearLayout) findViewById(R.id.relative_layout_to_hide);
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

    private void placeOrder__() {
        int isFirstTime = 0;
/*        try {

            mobileNumber = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberMobileno),"");
            userName = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberUsername),"");

            if(mobileNumber.equalsIgnoreCase(null) || mobileNumber.equalsIgnoreCase("")) {
                editor = sharedPreferencesRemember.edit();
                editor.putString(getResources().getString(R.string.sharedPreferenceRememberMobileno), activity_book_a_table_edittext_mobile_number.getText().toString());
                editor.putString(getResources().getString(R.string.sharedPreferenceRememberUsername), activity_book_a_table_edittext_name.getText().toString());
                editor.commit();

                isFirstTime = 0;
            }else {
                isFirstTime = 1;
            }

            jsonObjectCart = new JSONObject();
            JSONArray jsonArrayCart = new JSONArray();
            for(int i = 0; i < cartModelArrayList.size(); i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(getResources().getString(R.string.database_menu_id), cartModelArrayList.get(i).getDatabase_menu_id());
                jsonObject.put(getResources().getString(R.string.database_menu_quantity), cartModelArrayList.get(i).getDatabase_menu_quantity());
                jsonObject.put(getResources().getString(R.string.database_menu_price), cartModelArrayList.get(i).getDatabase_menu_price());
                jsonObject.put(getResources().getString(R.string.database_menu_name), cartModelArrayList.get(i).getDatabase_menu_name());
                jsonObject.put(getResources().getString(R.string.database_user_id), cartModelArrayList.get(i).getDatabase_user_id());
                jsonObject.put(getResources().getString(R.string.database_menu_amount), (Double.parseDouble(cartModelArrayList.get(i).getDatabase_menu_quantity()) * Double.parseDouble(cartModelArrayList.get(i).getDatabase_menu_price()))+"");
                jsonArrayCart.put(jsonObject);
            }
            jsonObjectCart.put(getResources().getString(R.string.cart_items), jsonArrayCart);
            jsonObjectCart.put(getResources().getString(R.string.sub_total), subTotal);
            jsonObjectCart.put(getResources().getString(R.string.grand_total), grandTotal);
            jsonObjectCart.put(getResources().getString(R.string.user_name), activity_book_a_table_edittext_name.getText().toString());
            jsonObjectCart.put(getResources().getString(R.string.mobile_number_), activity_book_a_table_edittext_mobile_number.getText().toString());
            jsonObjectCart.put(getResources().getString(R.string.isFirstTime), isFirstTime);
            jsonObjectCart.put(getResources().getString(R.string.tableId), getResources().getString(R.string.tableIdValues));
            jsonObjectCart.put(getResources().getString(R.string.taxes_), taxAmount);
            jsonObjectCart.put(getResources().getString(R.string.customer_id), customer_id);
            jsonObjectCart.put(getResources().getString(R.string.customerOrderId), customerOrderId);
            jsonObjectCart.put(getResources().getString(R.string.isMakePayment), 0);

            System.out.println("placeOrder: "+jsonObjectCart.toString());
        } catch (Exception ignored) {
        }*/

        Map<String, String> postParams = new HashMap<String, String>();
        // postParams.put(getResources().getString(R.string.userOrder), jsonObjectCart.toString());

        showProcessingDialog();
        APIManager.requestPostMethod(this, getResources().getString(R.string.placeOrder), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        customer_id = jsonObject.getString(getResources().getString(R.string.customer_id));
                        customerOrderId = jsonObject.getString(getResources().getString(R.string.customerOrderId));

                        JSONArray orderMenuIds = jsonObject.getJSONArray(getResources().getString(R.string.orderMenuIds));


                        editor = sharedPreferencesRemember.edit();
                        editor.putString(getResources().getString(R.string.sharedPreferenceRememberCustomerId), customer_id);
                        editor.putString(getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId), customerOrderId);
                        editor.apply();

                        for(int i = 0; i < cartModelArrayList.size(); i++){
                            JSONObject jsonObject1 = orderMenuIds.getJSONObject(i);
                            String order_menus_id = jsonObject1.getString(getResources().getString(R.string.order_menus_id));

                            //cartModelArrayList.get(i).setDatabase_menu_status("1");
                            MyCartData cartModel = cartModelArrayList.get(i);
                            //cartModel.setDatabase_menu_status("1");
                            //cartModel.setDatabase_order_menus_id(order_menus_id);
                            //databaseHelper.updateRecordById(Integer.parseInt(cartModel.getDatabase_menu_id()), cartModel.getDatabase_user_id(), cartModel);
                        }

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();
            }
        });
    }

    private void getMastervalues() {

        String signUp = "getMastervalues";

        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest signUpRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.baseURL) + getResources().getString(R.string.getMastervalues), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    System.out.println("getMastervalues: " + s);
                    JSONObject object = new JSONObject(s);
                    if (object.getString(getResources().getString(R.string.status)).equalsIgnoreCase(getResources().getString(R.string.status200))) {

                        JSONArray jsonArray = object.getJSONArray(getResources().getString(R.string.masterType));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        taxes = Double.parseDouble(jsonObject.getString(getResources().getString(R.string.taxes_)));

                        for(int i = 0; i < cartModelArrayList.size(); i++){
                            //subTotal = subTotal + (Double.parseDouble(cartModelArrayList.get(i).getDatabase_menu_quantity()) * Double.parseDouble(cartModelArrayList.get(i).getDatabase_menu_price()));
                        }

                        activity_order_preview_textview_subtotal.setText(getResources().getString(R.string.Rs)+" "+subTotal);

                        taxAmount = (subTotal * taxes / 100);

                        activity_order_preview_textview_tax.setText(getResources().getString(R.string.Rs)+" "+taxAmount);

                        grandTotal = subTotal + taxAmount;

                        activity_order_preview_textview_grand_total.setText(getResources().getString(R.string.Rs)+" "+grandTotal);

                        progressDialog.dismiss();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    System.out.println("getMastervalues: " + e);
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("getMastervalues: " + error);
                progressDialog.dismiss();
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                        Toast.makeText(getApplicationContext(), R.string.timeout_error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put(getResources().getString(R.string.masterType), getResources().getString(R.string.taxes_));

                System.out.println("getMastervalues: " + postParam);
                return postParam;
            }
        };

        int socketTimeout = Constants.SocketTimeout;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        signUpRequest.setRetryPolicy(policy);
        // Adding request to request queue
        APIController.getInstance().addToRequestQueue(signUpRequest, signUp);
    }


    private void getCartItems() {

        Map<String, String> postParams = new HashMap<String, String>();
        postParams.put(getResources().getString(R.string.tableId), getResources().getString(R.string.tableIdValues));
        postParams.put(getResources().getString(R.string.customer_id), customer_id);
        postParams.put(getResources().getString(R.string.customerOrderId), customerOrderId);

        showProcessingDialog();
        APIManager.requestPostMethod(this, getResources().getString(R.string.getCartItems), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        JSONArray orderMenuIds = jsonObject.getJSONArray(getResources().getString(R.string.order_list));
                        if(orderMenuIds.length() > 0){

                            cartModelArrayList = new ArrayList<>();

                            for(int i = 0; i < orderMenuIds.length(); i++){
                                JSONObject orderList = orderMenuIds.getJSONObject(i);
                                customer_id = orderList.getString(getResources().getString(R.string.customer_id));
                                String customer_name = orderList.getString(getResources().getString(R.string.customer_name));
                                String mobile_number = orderList.getString(getResources().getString(R.string.mobile_number_));
                                String order_menus_id = orderList.getString(getResources().getString(R.string.order_menus_id));
                                String tableId = orderList.getString(getResources().getString(R.string.tableId));
                                customerOrderId = orderList.getString(getResources().getString(R.string.customer_order_id));
                                String menu_id = orderList.getString(getResources().getString(R.string.menu_id));
                                String itemName = orderList.getString(getResources().getString(R.string.itemName));
                                String menu_price = orderList.getString(getResources().getString(R.string.menu_price));
                                String imageName = orderList.getString(getResources().getString(R.string.imageName));
                                String menu_quantity = orderList.getString(getResources().getString(R.string.menu_quantity));
                                String menu_amount = orderList.getString(getResources().getString(R.string.menu_amount));
                                String is_processed = orderList.getString(getResources().getString(R.string.is_processed));

                                activity_book_a_table_edittext_name.setText(customer_name);
                                activity_book_a_table_edittext_name.setEnabled(false);
                                activity_book_a_table_edittext_mobile_number.setText(mobile_number);
                                activity_book_a_table_edittext_mobile_number.setEnabled(false);

                                // cartModelArrayList.add(new CartModel("", order_menus_id, customer_id, menu_id, itemName, menu_price, imageName, menu_quantity, is_processed));
                            }
                            if(cartModelArrayList.size() > 0){
                                getMastervalues();
                            }else{
                                fragment_common_list_recycler_button_place_order.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();
            }
        });
    }

    //get cart details

    private void getCartList() {

        // swipe_refresh_layout.setRefreshing(true);
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getResources().getString(R.string.field_table), Constants.table_id);

        APIManager.requestPostMethod(CartPreviewActivity.this, getResources().getString(R.string.getCartList), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.getInt("status") == 200) {

                        String totalCartAmount = jsonObject.getString("total_amount");
                        activity_order_preview_textview_grand_total.setText(getResources().getString(R.string.Rs)+" "+totalCartAmount);
                        activity_order_preview_textview_subtotal.setText(getResources().getString(R.string.Rs)+" "+totalCartAmount);
                        String jsonString =  jsonObject.getJSONArray(Constants.data).toString();

                        cartModelArrayList.clear();
                        cartListIds.clear();
                        cart_id= "";
                        Gson gson = new Gson();
                        String jsonOutput = jsonString;
                        Type listType = new TypeToken<ArrayList<MyCartData>>(){}.getType();
                        cartModelArrayList = gson.fromJson(jsonOutput, listType);
                        //set data to adapter [list]
                        if (cartModelArrayList.size() > 0) {
                            // textview_no_record_found.setVisibility(View.GONE);
                            //swipe_refresh_layout.setVisibility(View.VISIBLE);
                            if(flagisTrue){
                                cartItemsListAdapter.updateList(cartModelArrayList);
                            }else {
                                cartItemsListAdapter = new MyCartItemsListAdapter(CartPreviewActivity.this, cartModelArrayList);
                                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartPreviewActivity.this);
                                linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                                fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
                                fragment_recent_jobs_recycler_view.setAdapter(cartItemsListAdapter);
                                fragment_recent_jobs_recycler_view.setEnabled(true);
                                //swipe_refresh_layout.setRefreshing(false);
                            }
                            //get all cart id
                            for(MyCartData myCartData : cartModelArrayList){
                                cartListIds.add(myCartData.getId());
                            }
                            cart_id = TextUtils.join(",",cartListIds);
                        }else {
                            textview_no_record_found.setVisibility(View.VISIBLE);
                            fragment_recent_jobs_recycler_view.setVisibility(View.GONE);
                            //textview_no_record_found.setVisibility(View.VISIBLE);
                            //swipe_refresh_layout.setVisibility(View.GONE);
                        }
                    }else if (jsonObject.getInt("status") == 400) {

                        textview_no_record_found.setVisibility(View.VISIBLE);
                        fragment_recent_jobs_recycler_view.setVisibility(View.GONE);

                       // Toast.makeText(CartPreviewActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //swipe_refresh_layout.setRefreshing(false);
            }
        });
    }
   /* private void sendUserOtp() {

        // swipe_refresh_layout.setRefreshing(true);
        HashMap<String, String> postParams = new HashMap<>();

        postParams.put(getResources().getString(R.string.field_mobile), mobileNumber);
        postParams.put(getResources().getString(R.string.field_fullname), userName);
        postParams.put(getResources().getString(R.string.field_table), Constants.table_id);

        APIManager.requestPostMethod(CartPreviewActivity.this, getResources().getString(R.string.sendUserOTP), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        String order_id = jsonObject.getString("order_id");


                    }else if (jsonObject.getInt("status") == 400) {
                        Toast.makeText(CartPreviewActivity.this, jsonObject.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //swipe_refresh_layout.setRefreshing(false);
            }
        });
    }*/


    private void placeOrder() {

        // swipe_refresh_layout.setRefreshing(true);
        HashMap<String, String> postParams = new HashMap<>();

        postParams.put(getResources().getString(R.string.field_order_id), order_id);
        postParams.put(getResources().getString(R.string.field_table), Constants.table_id);
        postParams.put(getResources().getString(R.string.field_cart_id), cart_id);


        APIManager.requestPostMethod(CartPreviewActivity.this, getResources().getString(R.string.placeOrder_), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        //String order_id = jsonObject.getString("order_id");

                        //editor = sharedPreferencesRemember.edit();
                        //editor.putString(getResources().getString(R.string.order_id), order_id);
                        //editor.commit();
                        Intent intent = new Intent(CartPreviewActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(CartPreviewActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();

                    }else if (jsonObject.getInt("status") == 400) {
                        Toast.makeText(CartPreviewActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
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
        postParams.put(getResources().getString(R.string.field_table),Constants.table_id);
        // send ids by comma seperated and it is optional
        postParams.put(getResources().getString(R.string.field_dishes_extra_id), dish_extra_id);

        APIManager.requestPostMethod(CartPreviewActivity.this, getResources().getString(R.string.addCart), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        dismissProgressDialog();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(CartPreviewActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();

                        //refresh
                        if (internetConnection.isNetworkAvailable(CartPreviewActivity.this)) {
                            //getMenuListByType();
                        } else {
                            Toast.makeText(CartPreviewActivity.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                        }
                    }else if (jsonObject.getInt("status") == 400) {
                        dismissProgressDialog();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(CartPreviewActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
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


    //getOrder details
    private void getOrderListByCustomer() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getResources().getString(R.string.field_order_id), order_id);

        //showProcessingDialog();
        APIManager.requestPostMethod(CartPreviewActivity.this, getResources().getString(R.string.getOrderDetails), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        String jsonOutput = jsonObject.getJSONArray(Constants.data).toString();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<MyOrderHistory>>(){}.getType();
                        orderHistoryModelArrayList = gson.fromJson(jsonOutput, listType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
