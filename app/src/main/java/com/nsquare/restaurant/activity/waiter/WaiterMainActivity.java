package com.nsquare.restaurant.activity.waiter;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.LoginActivity;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.activity.ViewProfileActivity;
import com.nsquare.restaurant.fragment.waiter.WaiterTablesFragment;
import com.squareup.picasso.Picasso;

public class WaiterMainActivity extends ParentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private android.support.v4.app.FragmentManager fragmentManager;
    private MenuItem callbutton, bellbutton, searchbutton;
    private TextView textview_name;
    private LinearLayout linearlayout;
    private ImageView activity_company_sign_up_imageView_profile_pic;
    //String reedempoint, companyname, name, lastname, image, profile_image, sharedPreferencescompany, userId;
    private Dialog dialogLogout;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    NavigationView navigationView;
    Toolbar toolbar;
    public static int optionMenuFlag = 0;
    AlertDialog alert11;
    public static Activity myActivity;
    private ImageView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiter_main_layout);

        myActivity = this;
//        getActionBar().hide();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setStatusBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        setActionBarCustomWithBackLeftText(getResources().getString(R.string.app_name));
//        setActionBarCustomWithoutBack(getResources().getString(R.string.app_name));
        fragmentManager = getSupportFragmentManager();
//
        //sharedPreferencesRemember = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final String sharedPreferenceUserId = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceUserId), "");
        final String sharedPreferencefirst_name = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencefirst_name), "");
        final String sharedPreferencelast_name = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencelast_name), "");
        String sharedPreferenceprofileimage = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceprofileimage), "");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        toolbar_title = (ImageView) findViewById(R.id.toolbar_title);
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
            System.out.println("getprofileActivity: " + sharedPreferenceprofileimage);
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

        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WaiterTablesFragment fragment = new WaiterTablesFragment();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        navigationView.getMenu().findItem(id).setCheckable(true);
        if (id == R.id.home) {
            toolbar_title.setVisibility(View.VISIBLE);
            toolbar.setTitle("");
            optionMenuFlag = 0;
//            toolbar.inflateMenu(R.menu.home);
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            WaiterTablesFragment fragment = new WaiterTablesFragment();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // Handle the camera action
        }
        else if (id == R.id.signout) {
            optionMenuFlag = 1;
            new android.app.AlertDialog.Builder(WaiterMainActivity.this)
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
//
//        else if (id == R.id.transaction) {
//
//            optionMenuFlag = 2;
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            TransactionFragment fragment = new TransactionFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//        } else if (id == R.id.directory) {
//
//            optionMenuFlag = 3;
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            DirectoryFragment fragment = new DirectoryFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//        } else if (id == R.id.payment) {
//            optionMenuFlag = 4;
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            PaymentFragment fragment = new PaymentFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//
//        }  else if (id == R.id.mynetwork) {
//            optionMenuFlag = 5;
////            toolbar.inflateMenu(R.menu.home_search);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            MyNetworkFragment fragment = new MyNetworkFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//        }else if (id == R.id.wallet) {
//
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            WalletFragment fragment = new WalletFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//        }else if (id == R.id.wishlist) {
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            WishListFragment fragment = new WishListFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//        }else if (id == R.id.logistics) {
//
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            LogisticsFragment fragment = new LogisticsFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//        }else if (id == R.id.help) {
//
//        }else if (id == R.id.faq) {
//
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            FAQFragment fragment = new FAQFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//
//        }else if (id == R.id.aboutus) {
//
//
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            AboutFragment fragment = new AboutFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//        }else if(id==R.id.contactus){
//
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            ContactUsFragment fragment = new ContactUsFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//        }
//
//        else if (id == R.id.setting) {
//
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            SettingFragment fragment = new SettingFragment();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//        }else if (id == R.id.logout) {
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
//            LayoutInflater factory = LayoutInflater.from(MainActivity.this);
//            final View view1 = factory.inflate(R.layout.logout_dailogue_layout, null);
//            //builder1.setMessage("Your Request to connect has been sent successfully");
//            builder1.setView(view1);
//            builder1.setCancelable(true);
//             alert11 = builder1.create();
//            alert11.show();
//
//            Button btnlogoutYes = (Button) view1.findViewById(R.id.btnlogoutYes);
//            Button btnlogoutno = (Button) view1.findViewById(R.id.btnlogoutno);
//
//            btnlogoutYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (internetConnection.isNetworkAvailable(getApplicationContext())) {
//                        logoutuser();
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            });
//
//            btnlogoutno.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alert11.cancel();
//                }
//            });
//
//
//
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    @Override
    protected void onResume() {
        super.onResume();
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

}
