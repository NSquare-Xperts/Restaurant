package com.nsquare.restaurant.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.adapter.MyCartItemsListAdapter;
import com.nsquare.restaurant.fragment.HomeFragment;
import com.nsquare.restaurant.fragment.OrderHistoryFragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nsquare.restaurant.fragment.waiter.WaiterTablesFragment;
import com.nsquare.restaurant.model.MyCartData;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ParentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private android.support.v4.app.FragmentManager fragmentManager;
    private TextView textview_name;
    private LinearLayout linearlayout;
    private ImageView activity_company_sign_up_imageView_profile_pic;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    NavigationView navigationView;
    Toolbar toolbar;
    public static int optionMenuFlag = 0;
    AlertDialog alert11;
    public static Activity myActivity;
    private ImageView toolbar_title;
    private ImageView add_to_cart;
    private String sharedPreferencerole = "";
    private String order_id_ = "";

    private String statusCount;
    //private ArrayList<MyCartData> toGetCountcartModelArrayList = new ArrayList<MyCartData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        myActivity = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setStatusBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String tableId = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencetableid), "");
        order_id_ = sharedPreferencesRemember.getString(getResources().getString(R.string.order_id), "");
        Constants.table_id =tableId;

//        setActionBarCustomWithBackLeftText(getResources().getString(R.string.app_name));
//        setActionBarCustomWithoutBack(getResources().getString(R.string.app_name));
        fragmentManager = getSupportFragmentManager();
//
        //sharedPreferencesRemember = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final String sharedPreferenceUserId = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceUserId), "");
        final String sharedPreferencefirst_name = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencefirst_name), "");
        final String sharedPreferencelast_name = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencelast_name), "");
        sharedPreferencerole = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencerole), "");
        String sharedPreferenceprofileimage = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceprofileimage), "");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        toolbar_title = (ImageView) findViewById(R.id.toolbar_title);
        add_to_cart = (ImageView) findViewById(R.id.add_to_cart);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        textview_name = (TextView) hView.findViewById(R.id.textview_name);
        activity_company_sign_up_imageView_profile_pic = (CircularImageView) hView.findViewById(R.id.activity_company_sign_up_imageView_profile_pic);


        if(sharedPreferencefirst_name == null || sharedPreferencefirst_name.equalsIgnoreCase("")){
        }else {
            textview_name.setText(sharedPreferencefirst_name + " " + sharedPreferencelast_name);
        }
        if (sharedPreferenceprofileimage.equalsIgnoreCase("")) {
        } else {
            //System.out.println("getprofileActivity: " + sharedPreferenceprofileimage);
            Picasso.with(getApplicationContext()).load(sharedPreferenceprofileimage).placeholder(R.drawable.app_icon).into(activity_company_sign_up_imageView_profile_pic);
        }

        hView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                if(sharedPreferenceUserId == null || sharedPreferenceUserId.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_login), Toast.LENGTH_SHORT).show();
                }else {
                    Intent intentForgotPassword = new Intent(getApplicationContext(), ViewProfileActivity.class);
                    startActivity(intentForgotPassword);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check internet connection
                if(internetConnection.isNetworkAvailable(getApplicationContext())){
                    getCartList();
                }else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(getIntent().hasExtra(Constants.homeFromWaiter)){
            //load only home fragment
            add_to_cart.setVisibility(View.GONE);
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            HomeFragment fragment = new HomeFragment();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

        }else {
            if (sharedPreferencerole.equalsIgnoreCase("4")) {

                toolbar_title.setVisibility(View.VISIBLE);
                toolbar.setTitle("");
                optionMenuFlag = 0;
                //toolbar.inflateMenu(R.menu.home);
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                WaiterTablesFragment fragment = new WaiterTablesFragment();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            } else {

                add_to_cart.setVisibility(View.VISIBLE);
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment fragment = new HomeFragment();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            }
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        navigationView.getMenu().findItem(id).setCheckable(true);

        if (id == R.id.home) {

            if(sharedPreferencerole.equalsIgnoreCase("4")){
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar.setTitle("");
                optionMenuFlag = 0;
//            toolbar.inflateMenu(R.menu.home);
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                WaiterTablesFragment fragment = new WaiterTablesFragment();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            }else{
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar.setTitle("");
                optionMenuFlag = 0;
//            toolbar.inflateMenu(R.menu.home);
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment fragment = new HomeFragment();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            }



            // Handle the camera action
        }else if (id == R.id.order_history) {

            //sharedPreferencesRemember = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            /*String customer_id = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerId),"");
            String customerOrderId = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId),"");

            if(customer_id == null || customer_id.equalsIgnoreCase("null") || customer_id.equalsIgnoreCase("")){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_order), Toast.LENGTH_SHORT).show();
            }else{
                toolbar_title.setVisibility(View.GONE);
                toolbar.setTitle(getResources().getString(R.string.order_history));
                optionMenuFlag = 2;
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                OrderHistoryFragment fragment = new OrderHistoryFragment();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            }*/

            //System.out.println("order_id_ : "+order_id_);
            if(order_id_ == null || order_id_.equalsIgnoreCase("null") || order_id_.equalsIgnoreCase("")){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_order), Toast.LENGTH_SHORT).show();
            }else{
                toolbar_title.setVisibility(View.GONE);
                toolbar.setTitle(getResources().getString(R.string.order_history));
                optionMenuFlag = 2;
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                OrderHistoryFragment fragment = new OrderHistoryFragment();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            }

        }else if (id == R.id.signout) {
            new android.app.AlertDialog.Builder(MainActivity.this)
                    .setMessage(getResources().getString(R.string.are_you_sure_you_want_logout))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences.Editor editor = sharedPreferencesRemember.edit();
                            editor.remove(getResources().getString(R.string.sharedPreferenceUserId));
                            editor.remove(getResources().getString(R.string.sharedPreferenceprofileimage));
                            editor.remove(getResources().getString(R.string.sharedPreferencefirst_name));
                            editor.remove(getResources().getString(R.string.sharedPreferencelast_name));
                            editor.remove(getResources().getString(R.string.sharedPreferenceemail));
                            editor.remove(getResources().getString(R.string.sharedPreferencepassword));
                            editor.remove(getResources().getString(R.string.sharedPreferencerole));
                            editor.commit();

                            try {
                                finish();
                                Intent intentLogout=new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intentLogout);
                            }catch (Exception e){
                                System.out.println("Exception: "+e);
                            }
                        }
                    }).setNegativeButton(getResources().getString(R.string.no), null).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferencerole = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencerole), "");

        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_home);

        if(sharedPreferencerole.equalsIgnoreCase("4")){
            navigationView.getMenu().findItem(R.id.signout).setVisible(true);
            navigationView.getMenu().findItem(R.id.order_history).setVisible(false);
        }else{
            navigationView.getMenu().findItem(R.id.signout).setVisible(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finish();
            return;
        } else {
            Toast.makeText(getBaseContext(), R.string.toast_exit, Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }



    private void getCartList() {

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getResources().getString(R.string.field_table), Constants.table_id);

        APIManager.requestPostMethod(MainActivity.this, getResources().getString(R.string.getCartList), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    statusCount = jsonObject.getString("status");
                    if(statusCount.equalsIgnoreCase("200")){
                        Intent intent = new Intent(MainActivity.this,CartPreviewActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this,jsonObject.getString(getResources().getString(R.string.message)),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
