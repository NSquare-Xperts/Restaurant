package com.nsquare.restaurant.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.nsquare.restaurant.fragment.HomeFragment;
import com.nsquare.restaurant.fragment.OrderHistoryFragment;
import com.squareup.picasso.Picasso;

public class LifeCycleActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //on first time activity is created
    }

    @Override
    protected void onStart() {
        super.onStart();

        //activity is foreground

    }

    @Override
    protected void onResume() {
        super.onResume();

        //again starts interacting with user

    }

    @Override
    protected void onPause() {
        super.onPause();

        //on back pressed or home button or next activity is called
    }

    @Override
    protected void onStop() {
        super.onStop();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
