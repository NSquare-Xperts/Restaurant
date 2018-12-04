package com.nsquare.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.nsquare.restaurant.R;

/**
 * Created by Pushkar on 08-09-2017.
 */

public class ViewProfileActivity extends ParentActivity {

    private Button activity_view_profile_button_edit;
    private ImageView activity_view_profile_imageview_profile_image;
    private TextView activity_view_profile_textview_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.my_profile));
        findViewByIds();

        sharedPreferencesRemember = PreferenceManager.getDefaultSharedPreferences(ViewProfileActivity.this);
        String sharedPreferencefirst_name = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencefirst_name), "");
        String sharedPreferencelast_name = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencelast_name), "");
        String sharedPreferenceprofileimage = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceprofileimage), "");

        if(sharedPreferencefirst_name == null || sharedPreferencefirst_name.equalsIgnoreCase("")){
        }else {
            activity_view_profile_textview_name.setText(sharedPreferencefirst_name + " " + sharedPreferencelast_name);
        }

        if (sharedPreferenceprofileimage.equalsIgnoreCase("")) {
        } else {
            System.out.println("getprofileActivity: " + sharedPreferenceprofileimage);
            Picasso.with(getApplicationContext()).load(sharedPreferenceprofileimage).placeholder(R.drawable.app_icon).into(activity_view_profile_imageview_profile_image);
        }

        activity_view_profile_button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPassword = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intentForgotPassword);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void findViewByIds(){
        activity_view_profile_button_edit = (Button) findViewById(R.id.activity_view_profile_button_edit);
        activity_view_profile_imageview_profile_image = (ImageView) findViewById(R.id.activity_view_profile_imageview_profile_image);
        activity_view_profile_textview_name = (TextView) findViewById(R.id.activity_view_profile_textview_name);
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
}
