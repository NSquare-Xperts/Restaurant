package com.nsquare.restaurant.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.nsquare.restaurant.Production;
import com.nsquare.restaurant.Section;
import com.nsquare.restaurant.activity.CartPreviewActivity;
import com.nsquare.restaurant.activity.MakePaymentActivity;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.databasehelper.DatabaseHelper;
import com.nsquare.restaurant.gateway.SectionData;
import com.nsquare.restaurant.gateway.wheelcarousel.WheelCarousel;
import com.nsquare.restaurant.model.CartModel;
import com.nsquare.restaurant.model.SubCategoryModel;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;
import com.nsquare.restaurant.util.InternetConnection;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.util.WrapContentHeightViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Pushkar on 28-08-2017.
 */

public class HomeFragment extends Fragment implements WheelCarousel.UpdateHomepageData, WheelCarousel.UpdateHomepageCategoryTitle {

    private InternetConnection internetConnection = new InternetConnection();
    private ProgressDialog progressDialog;
    private Scroller scroller;
    private boolean isTouch = false;
    public static HomeFragment collapsingActivity;
    private WheelCarousel wheel;
    //private RatingBar fragment_home_ratingbar;
    AppBarLayout appbar;
    float alpha = 1.0f;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout linearlayout_viewpager, linearlayout_static_data;
    private NestedScrollView scroll;
    private TextView activity_collapse_layout_textView_title, activity_collapse_layout_textView_category_title;
    private Button activity_collapse_layout_button_book_now, activity_collapse_layout_button_make_payment;
    private ArrayList<SubCategoryModel> subCategoryArrayList;
   // private DatabaseHelper databaseHelper;
    private int cartCount = 0, cartCountForPayment = 0;
    private Activity activity;
//    private ObservableWebView activity_collapse_layout_observableview;
//    private int mParallaxImageHeight;

    public HomeFragment() {
        // Required empty public constructor


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_collapse_layout, container, false);
        int i;
        findViewByIds(view);

       // databaseHelper = new DatabaseHelper(getActivity());
//        activity_collapse_layout_observableview.setScrollViewCallbacks(this);

//        mParallaxImageHeight = getResources().getDimensionPixelSize(
//                R.dimen.parallax_image_height);

        collapsingActivity = this;
//        main_collapsing = (WheelCarousel) findViewById(R.id.main_collapsing);
        wheel.dismissLoadingTreatment();

        getInstance();
        WheelCarousel.updateHomepageData = (WheelCarousel.UpdateHomepageData) collapsingActivity;
        WheelCarousel.updateHomepageCategoryTitle = (WheelCarousel.UpdateHomepageCategoryTitle) collapsingActivity;

        if (internetConnection.isNetworkAvailable(getActivity())) {
            //System.out.println("call getCategory api");
            getCategories();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }

//        SectionData sectionData = new SectionData();
//        List<Section> sectionList = new ArrayList();
//        int[] array1 = new int[]{R.color.orange_dark,  R.color.yellow_dark, R.color.green_dark , R.color.c1_dark, R.color.c2_dark, R.color.c3_dark, R.color.c4_dark,  1442775040};
//        String[] titleArray = new String[]{"Category 1",  "Category 2", "Category 3", "Category 4", "Category 5", "Category 6", "Category 7", "Category 8"};
//        String[] iconArray = new String[]{"http://192.168.1.33:8080/circle_icon/Group_457.png",  "http://192.168.1.33:8080/circle_icon/Group_458.png", "http://192.168.1.33:8080/circle_icon/Group_459.png", "http://192.168.1.33:8080/circle_icon/Group_460.png"
//                , "http://192.168.1.33:8080/circle_icon/Group_460.png", "http://192.168.1.33:8080/circle_icon/Group_460.png", "http://192.168.1.33:8080/circle_icon/Group_460.png", "http://192.168.1.33:8080/circle_icon/Group_460.png"};
//        int[] iconDrawableArray = new int[]{R.drawable.inc1, R.drawable.inc2, R.drawable.inc3, R.drawable.inc4, R.drawable.inc4
//                , R.drawable.inc4, R.drawable.inc4, R.drawable.inc4};
//
//        activity_collapse_layout_textView_category_title.setText(titleArray[0]);
//        int[] array = new int[]{R.color.orange_light,  R.color.yellow_light, R.color.green_light, R.color.c1_light, R.color.c2_light, R.color.c3_light, R.color.c4_light, SupportMenu.CATEGORY_MASK};
//        for (i = 0; i < 3; i++) {
//            int j;
//            Section section = new Section();
//            section.setIconURL(iconArray[i]);
//            section.setPrimaryColor(array[i]);
//            section.setSecondaryColor(array1[i]);
//            section.setTitle(titleArray[i]);
//            section.setImage(iconDrawableArray[i]);
//            List<String> list = new ArrayList();
//            for (j = (i * 4) + 1; j <= (i * 4) + 4; j++) {
//                list.add(j + "");
//            }
//            section.setProductionIds(list);
//            sectionList.add(section);
//        }
//        sectionData.setSectionList(sectionList);
//
//        List<Production> productionList = new ArrayList();
//        String[] productionArray = new String[]{"Sample 1",  "Sample 2", "Sample 3", "Sample 4", "Sample 5",  "Sample 6", "Sample 7", "Sample 8",
//                "Sample 9",  "Sample 10", "Sample 11", "Sample 12", "Sample 13",  "Sample 14", "Sample 15", "Sample 16", "Sample 17"};
//        String[] mobileArray = new String[]{"1111111111",  "222222222", "3333333333", "4444444444", "5555555555",  "6666666666", "7777777777", "8888888888",
//                "9999999999",  "1010101010", "1212121212", "1313131313", "1414141414",  "1515151515", "1616161616", "1717171717", "1818181818"};
//        String[] coverImage = new String[]{"http://192.168.1.38:8080/circle_icon/Group_457.png",  "http://192.168.1.38:8080/circle_icon/Group_458.png", "http://192.168.1.38:8080/circle_icon/Group_459.png", "http://192.168.1.38:8080/circle_icon/Group_460.png",
//                "http://192.168.1.38:8080/circle_icon/Group_457.png",  "http://192.168.1.38:8080/circle_icon/Group_458.png", "http://192.168.1.38:8080/circle_icon/Group_459.png", "http://192.168.1.38:8080/circle_icon/Group_460.png",
//                "http://192.168.1.38:8080/circle_icon/Group_457.png",  "http://192.168.1.38:8080/circle_icon/Group_458.png", "http://192.168.1.38:8080/circle_icon/Group_459.png", "http://192.168.1.38:8080/circle_icon/Group_460.png",
//                "http://192.168.1.38:8080/circle_icon/Group_457.png",  "http://192.168.1.38:8080/circle_icon/Group_458.png", "http://192.168.1.38:8080/circle_icon/Group_459.png", "http://192.168.1.38:8080/circle_icon/Group_460.png", "http://192.168.1.38:8080/circle_icon/Group_460.png"};
//
//        for (i = 1; i <= 16; i++) {
//            Production production1 = new Production();
//            production1.setBackgroundImageURL(coverImage[i]);
//            ArrayList list = new ArrayList();
//            for (int j = (i * 4) + 1; j <= (i * 4) + 4; j++) {
//                list.add(j + "");
//            }
//
//            System.out.println("list********** "+list.toString());
//            production1.setCastList(list);
//            production1.setCoverImageURL(coverImage[i]);
//            production1.setMpaaRating(mobileArray[i]);
//            production1.setProductionId(i + "");
//            production1.setReleaseDate(new Date());
//            production1.setRuntime(10);
//            production1.setVideoURL("dvsdcx");
//            production1.setTitle(productionArray[i]);
//            productionList.add(production1);
//        }
//        sectionData.setProductionList(productionList);
//        wheel.setSectionData(sectionData);

//        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                float percentage = ((float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange());
//
//                System.out.println("percentage*****: "+percentage);
////                wheel.setAlpha(percentage);
//
//                linearlayout_viewpager.setVisibility(View.VISIBLE);
//                linearlayout_static_data.setVisibility(View.VISIBLE);
////                int toolBarHeight = wheel.getMeasuredHeight();
////                int appBarHeight = appBarLayout.getMeasuredHeight();
////                Float f = ((((float) appBarHeight - toolBarHeight) + verticalOffset) / ( (float) appBarHeight - toolBarHeight)) * 255;
////                wheel.setAlpha(255 - Math.round(f));
//
//                int totalScroll = appBarLayout.getTotalScrollRange();
//                int currentScroll = totalScroll + verticalOffset;
//                System.out.println("currentScroll*****: "+currentScroll);
//
////                if ((currentScroll) < -255){
//                if ((currentScroll) < 900){
//                    float newAlpha = alpha - 0.1f;
////                    float newAlpha = alpha - percentage;
//                    if (newAlpha >= 0){
//                        wheel.setAlpha(newAlpha);
//                        alpha = newAlpha;
//                    }
//
//                } else {
//
//                    linearlayout_viewpager.setVisibility(View.VISIBLE);
//                    linearlayout_static_data.setVisibility(View.VISIBLE);
//                    float newAlpha = alpha + 0.1f;
////                    float newAlpha = alpha + percentage;
//                    if (newAlpha <= 1){
//                        wheel.setAlpha(newAlpha);
//                        alpha = newAlpha;
//                    }
////                    Log.d("HellowWorld", "Not Overlapping!");
//
//                }
//
//                if ((currentScroll) == 0){
//                    linearlayout_static_data.setVisibility(View.GONE);
//                }else{
//                    linearlayout_static_data.setVisibility(View.VISIBLE);
//                }
//            }
//        });

//        scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                wheel.setAlpha(getAlphaforActionBar(v.getScrollY()));
//            }
//        });

        activity_collapse_layout_button_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentForgotPassword = new Intent(getActivity(), BookATableActivity.class);
//                startActivity(intentForgotPassword);
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Intent intentForgotPassword = new Intent(getActivity(), CartPreviewActivity.class);
                startActivity(intentForgotPassword);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        activity_collapse_layout_button_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentForgotPassword = new Intent(getActivity(), BookATableActivity.class);
//                startActivity(intentForgotPassword);
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                Intent intentForgotPassword = new Intent(getActivity(), MakePaymentActivity.class);
                startActivity(intentForgotPassword);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return view;
    }

    private void findViewByIds(View rootView) {

//        activity_collapse_layout_observableview = (ObservableWebView) rootView.findViewById(R.id.activity_collapse_layout_observableview);
        activity_collapse_layout_textView_category_title = (TextView) rootView.findViewById(R.id.activity_collapse_layout_textView_category_title);
        activity_collapse_layout_textView_title = (TextView) rootView.findViewById(R.id.activity_collapse_layout_textView_title);
//        activity_collapse_layout_textView_mobile = (TextView) rootView.findViewById(R.id.activity_collapse_layout_textView_mobile);
//        activity_collapse_layout_textView_address = (TextView) rootView.findViewById(R.id.activity_collapse_layout_textView_address);
//        activity_collapse_layout_textView_price = (TextView) rootView.findViewById(R.id.activity_collapse_layout_textView_price);
        //fragment_home_ratingbar = (RatingBar) rootView.findViewById(R.id.fragment_home_ratingbar);
        scroll = (NestedScrollView) rootView.findViewById(R.id.scroll);
        linearlayout_viewpager = (LinearLayout) rootView.findViewById(R.id.linearlayout_viewpager);
        linearlayout_static_data = (LinearLayout) rootView.findViewById(R.id.linearlayout_static_data);
        appbar = (AppBarLayout) rootView.findViewById(R.id.appbar);
        wheel = (WheelCarousel) rootView.findViewById(R.id.wheelCarouselOuter);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        activity_collapse_layout_button_book_now = (Button) rootView.findViewById(R.id.activity_collapse_layout_button_book_now);
        activity_collapse_layout_button_make_payment = (Button) rootView.findViewById(R.id.activity_collapse_layout_button_make_payment);

        activity_collapse_layout_button_book_now.setText(getActivity().getResources().getString(R.string.view_cart));

        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabs();
        scroll.scrollTo(0, 0);
        scroll.smoothScrollTo(0, 0);
//        linearlayout_viewpager.setVisibility(View.GONE);

        //fragment_home_ratingbar.setVisibility(View.GONE);
//        activity_collapse_layout_textView_mobile.setVisibility(View.GONE);
//        activity_collapse_layout_textView_address.setVisibility(View.GONE);
//        activity_collapse_layout_textView_price.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        //cartCount = databaseHelper.getCartCount(0);
       // cartCountForPayment = databaseHelper.getCartCount(1);

        if(cartCount > 0){
            activity_collapse_layout_button_book_now.setVisibility(View.VISIBLE);
        }else{
            activity_collapse_layout_button_book_now.setVisibility(View.GONE);
        }

        if(cartCountForPayment > 0){
            activity_collapse_layout_button_make_payment.setVisibility(View.VISIBLE);
        }else{
            activity_collapse_layout_button_make_payment.setVisibility(View.GONE);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.addFrag(new OffersFragment(), getString(R.string.offers));
        adapter.addFrag(new RestroListFragment(), getString(R.string.events));
//        adapter.addFrag(new RestroListFragment(), getString(R.string.londge));
//        adapter.addFrag(new RestroListFragment(), getString(R.string.all_tab));
//        adapter.addFrag(new RestroListFragment(), getString(R.string.cafe_tab));
//        adapter.addFrag(new RestroListFragment(), getString(R.string.londge));

        viewPager.setAdapter(adapter);
    }

    private void setupTabs() {

        LayoutInflater mInflater1 = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView1 = mInflater1.inflate(R.layout.list_tab_item, null);

        final TextView tabOne1 = (TextView) convertView1.findViewById(R.id.list_tab_item_title);

        tabOne1.setText(getString(R.string.offers));
        tabLayout.getTabAt(0).setCustomView(convertView1);

        LayoutInflater mInflater2 = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView2 = mInflater2.inflate(R.layout.list_tab_item, null);
        final TextView tabTwo1 = (TextView) convertView2.findViewById(R.id.list_tab_item_title);

        tabTwo1.setText(getString(R.string.events));
        tabLayout.getTabAt(1).setCustomView(convertView2);

//        LayoutInflater mInflater3 = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        View convertView3 = mInflater3.inflate(R.layout.list_tab_item, null);
//
//        final TextView tabOnenew = (TextView) convertView3.findViewById(R.id.list_tab_item_title);
//
//        tabOnenew.setText(getString(R.string.londge));
//        tabLayout.getTabAt(2).setCustomView(convertView3);
//
//
//        LayoutInflater mInflater4 = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        View convertView4 = mInflater4.inflate(R.layout.list_tab_item, null);
//
//        final TextView tabOne4 = (TextView) convertView4.findViewById(R.id.list_tab_item_title);
//
//        tabOne4.setText(getString(R.string.all_tab));
//        tabLayout.getTabAt(3).setCustomView(convertView4);
//
//        LayoutInflater mInflater5 = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        View convertView5 = mInflater5.inflate(R.layout.list_tab_item, null);
//        final TextView tabTwo5 = (TextView) convertView5.findViewById(R.id.list_tab_item_title);
//
//        tabTwo5.setText(getString(R.string.cafe_tab));
//        tabLayout.getTabAt(4).setCustomView(convertView5);
//
//        LayoutInflater mInflater6 = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        View convertView6 = mInflater6.inflate(R.layout.list_tab_item, null);
//
//        final TextView tabOnenew6 = (TextView) convertView6.findViewById(R.id.list_tab_item_title);
//
//        tabOnenew6.setText(getString(R.string.londge));
//        tabLayout.getTabAt(5).setCustomView(convertView6);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == 0) {
                    tabOne1.setTextColor(getResources().getColor(R.color.orange_light));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));
//                    tabOnenew.setTextColor(getResources().getColor(R.color.white));
//                    tabOne4.setTextColor(getResources().getColor(R.color.white));
//                    tabTwo5.setTextColor(getResources().getColor(R.color.white));
//                    tabOnenew6.setTextColor(getResources().getColor(R.color.white));
                } else if (position == 1) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.orange_light));
//                    tabOnenew.setTextColor(getResources().getColor(R.color.white));
//                    tabOne4.setTextColor(getResources().getColor(R.color.white));
//                    tabTwo5.setTextColor(getResources().getColor(R.color.white));
//                    tabOnenew6.setTextColor(getResources().getColor(R.color.white));
                }
                /*else if (position == 2) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));
                    tabOnenew.setTextColor(getResources().getColor(R.color.orange_light));
                    tabOne4.setTextColor(getResources().getColor(R.color.white));
                    tabTwo5.setTextColor(getResources().getColor(R.color.white));
                    tabOnenew6.setTextColor(getResources().getColor(R.color.white));
                } else if (position == 3) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));
                    tabOnenew.setTextColor(getResources().getColor(R.color.white));
                    tabOne4.setTextColor(getResources().getColor(R.color.orange_light));
                    tabTwo5.setTextColor(getResources().getColor(R.color.white));
                    tabOnenew6.setTextColor(getResources().getColor(R.color.white));
                } else if (position == 4) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));
                    tabOnenew.setTextColor(getResources().getColor(R.color.white));
                    tabOne4.setTextColor(getResources().getColor(R.color.white));
                    tabTwo5.setTextColor(getResources().getColor(R.color.orange_light));
                    tabOnenew6.setTextColor(getResources().getColor(R.color.white));
                } else if (position == 5) {
                    tabOne1.setTextColor(getResources().getColor(R.color.white));
                    tabTwo1.setTextColor(getResources().getColor(R.color.white));
                    tabOnenew.setTextColor(getResources().getColor(R.color.white));
                    tabOne4.setTextColor(getResources().getColor(R.color.white));
                    tabTwo5.setTextColor(getResources().getColor(R.color.white));
                    tabOnenew6.setTextColor(getResources().getColor(R.color.orange_light));
                }*/
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public static HomeFragment getInstance() {
        return collapsingActivity;
    }

    @Override
    public void updateHomepageData(Production production) {
        activity_collapse_layout_textView_title.setText(production.getTitle());
//        activity_collapse_layout_textView_mobile.setText(production.getVideoURL());
//        activity_collapse_layout_textView_address.setText(production.getMpaaRating());
//        //fragment_home_ratingbar.setVisibility(View.GONE);
//        activity_collapse_layout_textView_mobile.setVisibility(View.GONE);
//        activity_collapse_layout_textView_address.setVisibility(View.GONE);
//        activity_collapse_layout_textView_price.setVisibility(View.GONE);
    }

    @Override
    public void updateHomepageCategoryTitle(String title) {
        activity_collapse_layout_textView_category_title.setText(title);
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


    private void getCategories() {
//
        String login = "getCategories";
        System.out.println("inside getCategories");
        //progressDialog.show();

        StringRequest loginRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.baseURL) + getResources().getString(R.string.getCategories), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    System.out.println("inside getCategories:: " + s.toString());
                    JSONObject object = new JSONObject(s);
                    // String requestArray = object.getString(getResources().getString(R.string.request));
//                    JSONArray jsonArrayRequest = object.getJSONArray(requestArray);
                    if (object.getString(getResources().getString(R.string.status)).equalsIgnoreCase(getResources().getString(R.string.status200))) {
                        //parentActivity.dismissProgressDialog();
                        //Toast.makeText(getActivity(), object.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArrayCategories = object.getJSONArray(getResources().getString(R.string.data));

                        if (jsonArrayCategories.length() > 0) {

                            SectionData sectionData = new SectionData();
                            List<Section> sectionList = new ArrayList();

//                          int[] arrayColorDark = new int[]{R.color.orange_dark, R.color.yellow_dark, R.color.green_dark, R.color.c1_dark, R.color.c2_dark, R.color.c3_dark, R.color.c4_dark, 1442775040};
//                          int[] arrayColorLight = new int[]{R.color.orange_light, R.color.yellow_light, R.color.green_light, R.color.c1_light, R.color.c2_light, R.color.c3_light, R.color.c4_light, SupportMenu.CATEGORY_MASK};

                            String[] arrayColorDark= new String[jsonArrayCategories.length()];
                            String[] arrayColorLight = new String[jsonArrayCategories.length()];

//                            arrayColorLight[0] = 0xFF00FF00;
//                            arrayColorLight[1] = 0xFF00FF48;
//                            arrayColorLight[2] = 0xFF458700;
//
//                            arrayColorDark[0] = 0xFF12FF00;
//                            arrayColorDark[1] = 0xFF00FF65;
//                            arrayColorDark[2] = 0xFF007858;

//                            for (int i = 0; i < jsonArrayCategories.length(); i++) {
//
//                                JSONObject jsonObject = jsonArrayCategories.getJSONObject(i);
//                                String color_code_dark = jsonObject.getString(getResources().getString(R.string.color_code_dark));
//                                String color_code_light = jsonObject.getString(getResources().getString(R.string.color_code_light));
//
//                                int colorLight = Integer.parseInt(color_code_light, 16);
//                                int rLight = (colorLight >> 16) & 0xFF;
//                                int gLight = (colorLight >> 8) & 0xFF;
//                                int bLight = (colorLight >> 0) & 0xFF;
//
//
//                                int colorDark = Integer.parseInt(color_code_dark, 16);
//                                int rDark = (colorDark >> 16) & 0xFF;
//                                int gDark = (colorDark >> 8) & 0xFF;
//                                int bDark = (colorDark >> 0) & 0xFF;
//
//
//                                int finalLight = rLight+gLight+bLight;
//                                int finalDark = rDark+gDark+bDark;
//
//                                arrayColorLight[i] = finalLight;//Integer.parseInt(color_code_light);
//                                arrayColorDark[i] = finalDark;
//                            }

                            String[] titleArray = new String[jsonArrayCategories.length()];
                            String[] categoryIds = new String[jsonArrayCategories.length()];
                            int[] iconDrawableArray = new int[]{R.drawable.inc1, R.drawable.inc2, R.drawable.inc3, R.drawable.inc4, R.drawable.inc4 , R.drawable.inc4, R.drawable.inc4, R.drawable.inc4};

                            subCategoryArrayList = new ArrayList<>();

                            for (int i = 0; i < jsonArrayCategories.length(); i++) {
                                JSONObject jsonObject = jsonArrayCategories.getJSONObject(i);
                                String title = jsonObject.getString(getResources().getString(R.string.title));
                                String id = jsonObject.getString(getResources().getString(R.string.id));
                                String color_code_dark = jsonObject.getString(getResources().getString(R.string.color_code_dark));
                                String color_code_light = jsonObject.getString(getResources().getString(R.string.color_code_light));
                                String image_file = jsonObject.getString(getResources().getString(R.string.image_file));

                                titleArray[i] = title;
                                categoryIds[i] = id;

                                int j;
                                Section section = new Section();
                                section.setIconURL(image_file);
                                section.setPrimaryColor(color_code_dark);
                                section.setSecondaryColor(color_code_light);
                                section.setTitle(titleArray[i]);
                                section.setImage(R.drawable.inc1);
                                List<String> list = new ArrayList();

                                JSONArray jsonArraySubCategories = jsonObject.getJSONArray(getResources().getString(R.string.subCategories));
                                if (jsonArraySubCategories.length() > 0) {
                                    for (int k = 0; k < jsonArraySubCategories.length(); k++) {
                                        JSONObject jsonObjectSubCategories = jsonArraySubCategories.getJSONObject(k);

                                        String subcategoryName = jsonObjectSubCategories.getString(getResources().getString(R.string.title));
                                        //String categryId = jsonObjectSubCategories.getString(getResources().getString(R.string.categryId));
                                        String subcategoryId = jsonObjectSubCategories.getString(getResources().getString(R.string.id));
                                        String subcategoryImage = jsonObjectSubCategories.getString(getResources().getString(R.string.image_file));

                                        //if (categryId.equalsIgnoreCase(categoryId)) {
                                        list.add(subcategoryId + "");
                                        //}
                                        subCategoryArrayList.add(new SubCategoryModel(subcategoryName, id, subcategoryId, subcategoryImage));
                                    }
                                }
                                section.setProductionIds(list);
                                sectionList.add(section);
                            }
                            sectionData.setSectionList(sectionList);

                            List<Production> productionList = new ArrayList();

                            String[] productionArray = new String[subCategoryArrayList.size()];
                            String[] mobileArray = new String[subCategoryArrayList.size()];
                            String[] coverImage = new String[subCategoryArrayList.size()];
                            String[] categoryId = new String[subCategoryArrayList.size()];

                            for (int i = 0; i < subCategoryArrayList.size(); i++) {
                                productionArray[i] = subCategoryArrayList.get(i).getSubcategoryName();
                                mobileArray[i] = subCategoryArrayList.get(i).getSubcategoryId();
                                coverImage[i] = subCategoryArrayList.get(i).getSubcategoryImage();
                                categoryId[i] = subCategoryArrayList.get(i).getCategryId();
                            }
                            System.out.println("subCategoryArrayList***** size " + subCategoryArrayList.size());
                            System.out.println("total category list***** size " + titleArray.length);


                            for (int i = 0; i < productionArray.length; i++) {
                                Production production1 = new Production();
                                production1.setBackgroundImageURL(coverImage[i]);
                                ArrayList list = new ArrayList();

                                for(int k = 0 ; k < categoryIds.length; k++){
                                    for(int l = 0 ; l < productionArray.length; l++){

                                        if(categoryIds[k].equalsIgnoreCase(categoryId[l])){
                                            list.add(mobileArray[k] + "");
                                        }
                                    }
                                    production1.setCastList(list);
                                    production1.setCoverImageURL(coverImage[i]);
                                    production1.setMpaaRating(mobileArray[i]);
                                    production1.setProductionId(mobileArray[i]);
                                    production1.setReleaseDate(new Date());
                                    production1.setRuntime(10);
                                    production1.setVideoURL("dvsdcx");
                                    production1.setTitle(productionArray[i]);
                                    productionList.add(production1);
                                }

                                if(i == 0){
                                    activity_collapse_layout_textView_title.setText(productionArray[0]);
//                                    activity_collapse_layout_textView_mobile.setText(mobileArray[0]);
//                                    activity_collapse_layout_textView_address.setText(coverImage[0]);
                                }
                            }
                            sectionData.setProductionList(productionList);
                            wheel.setSectionData(sectionData);

                            activity_collapse_layout_textView_category_title.setText(titleArray[0]);

                            //fragment_home_ratingbar.setVisibility(View.GONE);
//                            activity_collapse_layout_textView_mobile.setVisibility(View.GONE);
//                            activity_collapse_layout_textView_address.setVisibility(View.GONE);
//                            activity_collapse_layout_textView_price.setVisibility(View.GONE);
                        }

                    } else {
                        // parentActivity.dismissProgressDialog();
                        Toast.makeText(getActivity(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    System.out.println("getCategories exptn : " + e);
                    //parentActivity.dismissProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("getCategories error: " + error.toString());
                // parentActivity.dismissProgressDialog();
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                        Toast.makeText(getActivity(), R.string.timeout_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {

        };
        int socketTimeout = Constants.SocketTimeout;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        loginRequest.setRetryPolicy(policy);
        // Adding request to request queue
        APIController.getInstance().addToRequestQueue(loginRequest, login);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}
