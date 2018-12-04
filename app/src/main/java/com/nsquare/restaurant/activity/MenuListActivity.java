package com.nsquare.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.fragment.VegMenuListFragment;

/**
 * Created by Pushkar on 16-04-2018.
 */

public class MenuListActivity extends ParentActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String key_subCategoryId, key_title;
    public static UpdateDataOnTabChangeVeg updateDataOnTabChangeVeg;
    //public static UpdateDataOnTabChangeNonVeg updateDataOnTabChangeNonVeg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_your_booking_tabs);

        setStatusBar();

        Intent intent = getIntent();
        key_title = intent.getStringExtra(getResources().getString(R.string.key_title));
        key_subCategoryId = intent.getStringExtra(getResources().getString(R.string.key_subCategoryId));

        setActionBarCustomWithBackLeftText(key_title);

        findViewByIds();
    }

    private void findViewByIds(){

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabs();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new VegMenuListFragment(), getString(R.string.tab_veg));
       // adapter.addFrag(new NonVegMenuListFragment(), getString(R.string.tab_non_veg));
        viewPager.setAdapter(adapter);
    }

    private void setupTabs() {

        LayoutInflater mInflater3 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView3 = mInflater3.inflate(R.layout.list_tab_item_pub_details, null);

        final TextView tabOne1  = (TextView) convertView3.findViewById(R.id.list_tab_item_title);

        tabOne1.setText(getString(R.string.tab_veg));
        tabLayout.getTabAt(0).setCustomView(convertView3);

      /*  LayoutInflater mInflater2 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView2 = mInflater2.inflate(R.layout.list_tab_item_pub_details, null);
        final TextView tabTwo1 = (TextView) convertView2.findViewById(R.id.list_tab_item_title);*/

        // tabTwo1.setText(getString(R.string.tab_non_veg));
        // tabLayout.getTabAt(1).setCustomView(convertView2);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    //tabTwo1.setTextColor(getResources().getColor(R.color.white));

                    updateDataOnTabChangeVeg.updateDataOnTabChangeVeg();
                } /*else if (position == 1) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));

                    //updateDataOnTabChangeNonVeg.updateDataOnTabChangeNonVeg();
                }*/
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

    public interface UpdateDataOnTabChangeVeg  {
        public void updateDataOnTabChangeVeg();
    }

    public interface UpdateDataOnTabChangeNonVeg  {
        public void updateDataOnTabChangeNonVeg();
    }
}
