package com.nsquare.restaurant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.adapter.RestroDetailsAdapter;
import com.nsquare.restaurant.fragment.CuisinesFragment;
import com.nsquare.restaurant.fragment.PhotosFragment;
import com.nsquare.restaurant.fragment.ReviewsFragment;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by Pushkar on 04-09-2017.
 */

public class RestroDetailsActivity extends ParentActivity {

    public static int[] mResources = {
            R.drawable.cafe_banner, R.drawable.cafe_banner
    };
    ViewPager mViewPager;
    CirclePageIndicator titleIndicator;
    int currentPage ;
    Timer swipeTimer;
    Runnable Update;
    Handler handler;
    private Activity myActivity;
    private RestroDetailsAdapter restroDetailsAdapter;
    private String intentTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView activity_restro_details_textview_book_a_table, activity_restro_details_textview_pre_order;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restro_details);

        intentTitle = getIntent().getStringExtra(getResources().getString(R.string.key_title));

        setStatusBar();
        setActionBarCustomWithBackLeftText(intentTitle);

        myActivity = this;
        findViewByIds();
        handler = new Handler();

        restroDetailsAdapter = new RestroDetailsAdapter(getApplicationContext(), myActivity);
        mViewPager.setAdapter(restroDetailsAdapter);

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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        activity_restro_details_textview_book_a_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPassword = new Intent(getApplicationContext(), BookATableActivity.class);
                startActivity(intentForgotPassword);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        activity_restro_details_textview_pre_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentForgotPassword = new Intent(getApplicationContext(), MenuListActivity.class);
//                startActivity(intentForgotPassword);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }


    public void findViewByIds()
    {
        mViewPager = (ViewPager)findViewById(R.id.activity_info_screen_pager);
        titleIndicator = (CirclePageIndicator) findViewById(R.id.activity_info_screen_titles);
        activity_restro_details_textview_book_a_table = (TextView) findViewById(R.id.activity_restro_details_textview_book_a_table);
        activity_restro_details_textview_pre_order = (TextView) findViewById(R.id.activity_restro_details_textview_pre_order);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabs();
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


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new CuisinesFragment(), getString(R.string.tab_cuisines));
        adapter.addFrag(new ReviewsFragment(), getString(R.string.tab_reviews));
        adapter.addFrag(new PhotosFragment(), getString(R.string.tab_photos));

        viewPager.setAdapter(adapter);
    }

    private void setupTabs() {

        LayoutInflater mInflater3 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView3 = mInflater3.inflate(R.layout.list_tab_item_pub_details, null);

        final TextView tabOne1  = (TextView) convertView3.findViewById(R.id.list_tab_item_title);

        tabOne1.setText(getString(R.string.tab_cuisines));
        tabLayout.getTabAt(0).setCustomView(convertView3);

        LayoutInflater mInflater2 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView2 = mInflater2.inflate(R.layout.list_tab_item_pub_details, null);
        final TextView tabTwo1 = (TextView) convertView2.findViewById(R.id.list_tab_item_title);

        tabTwo1.setText(getString(R.string.tab_reviews));
        tabLayout.getTabAt(1).setCustomView(convertView2);

        LayoutInflater mInflater1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView1 = mInflater1.inflate(R.layout.list_tab_item_pub_details, null);

        final TextView tabOnenew = (TextView) convertView1.findViewById(R.id.list_tab_item_title);

        tabOnenew.setText(getString(R.string.tab_photos));
        tabLayout.getTabAt(2).setCustomView(convertView1);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == 0) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));
                    tabOnenew.setTextColor(getResources().getColor(R.color.white));

                } else if (position == 1) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));
                    tabOnenew.setTextColor(getResources().getColor(R.color.white));

                }else if (position == 2) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));
                    tabOnenew.setTextColor(getResources().getColor(R.color.white));

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
