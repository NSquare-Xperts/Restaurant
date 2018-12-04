package com.nsquare.restaurant.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.nsquare.restaurant.R;

/**
 * Created by Pushkar on 28-08-2017.
 */

public class YourBookingTabsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_booking_tabs, container, false);
        int i;
        findViewByIds(view);

        return view;
    }

    private void findViewByIds(View rootView){

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabs();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.addFrag(new UpcomingYourBookingFragment(), getString(R.string.tab_upcoming));
        adapter.addFrag(new YourBookingHistoryFragment (), getString(R.string.tab_history));

        viewPager.setAdapter(adapter);
    }

    private void setupTabs() {

        LayoutInflater mInflater3 = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView3 = mInflater3.inflate(R.layout.list_tab_item_pub_details, null);

        final TextView tabOne1  = (TextView) convertView3.findViewById(R.id.list_tab_item_title);

        tabOne1.setText(getString(R.string.tab_upcoming));
        tabLayout.getTabAt(0).setCustomView(convertView3);

        LayoutInflater mInflater2 = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView2 = mInflater2.inflate(R.layout.list_tab_item_pub_details, null);
        final TextView tabTwo1 = (TextView) convertView2.findViewById(R.id.list_tab_item_title);

        tabTwo1.setText(getString(R.string.tab_history));
        tabLayout.getTabAt(1).setCustomView(convertView2);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == 0) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));
                } else if (position == 1) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));
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
