package com.nsquare.restaurant.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nsquare.restaurant.databasehelper.DatabaseHelper;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.Constants;
import com.nsquare.restaurant.util.InternetConnection;

import com.nsquare.restaurant.R;

import com.nsquare.restaurant.util.RobotoMedium.TextViewRoboto;

/**
 * Created by android on 19/5/16.
 */
public abstract class ParentActivity extends AppCompatActivity
{
    public SharedPreferences sharedPreferencesRemember;
    public APIController aController;
    public ProgressDialog progressDialog;
    public InternetConnection internetConnection = new InternetConnection();
    public SharedPreferences.Editor editor;
    public DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesRemember = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(ParentActivity.this);
        aController = (APIController) getApplicationContext();
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    public void setActionBarCustom(String title)
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
//        imageView.setImageResource(R.drawable.);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY
                | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        layoutParams.leftMargin = 60;
        imageView.setLayoutParams(layoutParams);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.orange_light));
        actionBar.setCustomView(imageView);

    }

    public void setActionBarCustomWithoutBack(String title)
    {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.home_top_logo);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY
                | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        //layoutParams.leftMargin = 60;
        imageView.setLayoutParams(layoutParams);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        actionBar.setCustomView(imageView);


//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange_light)));

    }

    public void setActionBarCustomWithBackCenterText(String title)
    {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView textView = new TextView(actionBar.getThemedContext());
        textView.setText(title);
        textView.setTextSize(20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setTextColor((ContextCompat.getColor(getApplicationContext(), R.color.white)));
        } else {

            textView.setTextColor(getResources().getColor(R.color.white));
        }

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY
                | Gravity.LEFT | Gravity.LEFT);
        //layoutParams.leftMargin = 60;
        textView.setLayoutParams(layoutParams);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.orange_light));
        actionBar.setCustomView(textView);
    }

    public void setActionBarCustomWithBackLeftText(String title)
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        TextViewRoboto textView = new TextViewRoboto(actionBar.getThemedContext());
        textView.setText(title);
        textView.setTextSize(20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setTextColor((ContextCompat.getColor(getApplicationContext(), R.color.white)));
        } else {

            textView.setTextColor(getResources().getColor(R.color.white));
        }

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY
                | Gravity.LEFT | Gravity.LEFT);
        //layoutParams.leftMargin = 60;
        textView.setLayoutParams(layoutParams);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.orange_light));
        actionBar.setCustomView(textView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBar()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){


            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor((ContextCompat.getColor(getApplicationContext(), R.color.orange_dark)));
            } else {

                window.setStatusBarColor(getResources().getColor(R.color.orange_dark));
            }
        }
    }

    public void showProcessingDialog() {
        if (Constants.progressDialog != null) {
            Constants.progressDialog.dismiss();
        }
        Constants.progressDialog = new ProgressDialog(ParentActivity.this);
        Constants.progressDialog.setMessage(getResources().getString(R.string.please_wait));
        Constants.progressDialog.setCancelable(false);
        Constants.progressDialog.setCanceledOnTouchOutside(false);
        Constants.progressDialog.show();
    }

    public void dismissProgressDialog() {
        Constants.progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        if (Constants.progressDialog != null) {
            Constants.progressDialog.dismiss();
        }
        super.onDestroy();
    }
}
