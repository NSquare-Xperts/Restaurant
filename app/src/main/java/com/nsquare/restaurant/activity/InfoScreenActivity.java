package com.nsquare.restaurant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.adapter.InfoScreenAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;

/**
 * Created by Pushkar on 23-08-2017.
 */

public class InfoScreenActivity extends ParentActivity {

    public static int[] mResources = {
            R.drawable.splash_one, R.drawable.splash_two
    };
    ViewPager mViewPager;
    CirclePageIndicator titleIndicator ;
    private InfoScreenAdapter infoScreenAdapter;
    int currentPage ;
    Timer swipeTimer;
    Runnable Update;
    Handler handler;
    private Activity myActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen);

        getSupportActionBar().hide();
        setStatusBar();

        editor = sharedPreferencesRemember.edit();
        editor.putString(getResources().getString(R.string.sharedPreferenceIsInfoVisited), "1");
        editor.commit();

        myActivity = this;
        findViewByIds();
        handler = new Handler();

        infoScreenAdapter = new InfoScreenAdapter(getApplicationContext(), myActivity);
        mViewPager.setAdapter(infoScreenAdapter);

        titleIndicator.setViewPager(mViewPager);
        titleIndicator.setFillColor(getResources().getColor(R.color.white));
        titleIndicator.setStrokeColor(getResources().getColor(R.color.white));
//        titleIndicator.setPageColor(getResources().getColor(R.color.orange_light));
        titleIndicator.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){
                    handler.postDelayed(sendData, 1000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////                titleIndicator.setCurrentItem(position);
//                if(position == 1){
//                    handler.postDelayed(sendData, 1000);
//                }
//
////                handler.post(sendData);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    public void findViewByIds()
    {
        mViewPager = (ViewPager)findViewById(R.id.activity_info_screen_pager);
        titleIndicator = (CirclePageIndicator) findViewById(R.id.activity_info_screen_titles);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(swipeTimer == null){
        }else{
            swipeTimer.cancel();
        }

        if(handler == null){
        }else{
            handler.removeCallbacks(Update);
        }

        finish();

//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(swipeTimer == null){
        }else{
            swipeTimer.cancel();
        }

        if(handler == null){
        }else{
            handler.removeCallbacks(Update);
        }
    }

    private final Runnable sendData = new Runnable(){
        public void run(){
            try {
                //prepare and send the data here..

                finish();
//                Intent intent = new Intent(InfoScreenActivity.this, CollapsingActivity.class);
                Intent intent = new Intent(InfoScreenActivity.this, SignUpAndLoginDecisionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                handler.postDelayed(this, 1000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
