package com.nsquare.restaurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nsquare.restaurant.activity.CartPreviewActivity;
import com.nsquare.restaurant.activity.MenuListActivity;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.adapter.NonVegMenuListAdapter;
import com.nsquare.restaurant.databasehelper.DatabaseHelper;
import com.nsquare.restaurant.model.CartModel;
import com.nsquare.restaurant.model.vegMenuList.VegMenuListModel;
import com.nsquare.restaurant.model.vegMenuList.VegMenuList_NewModel;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;
import com.nsquare.restaurant.util.InternetConnection;

import com.nsquare.restaurant.R;

import com.nsquare.restaurant.adapter.VegMenuListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NonVegMenuListFragment extends Fragment implements NonVegMenuListAdapter.UpdateValuesInterface, MenuListActivity.UpdateDataOnTabChangeNonVeg
{

    private VegMenuListAdapter yourUpcomingBookingAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView fragment_recent_jobs_recycler_view;
    private ArrayList<VegMenuListModel> upcomingYourBookingModelArrayList = new ArrayList<VegMenuListModel>();
    private ArrayList<VegMenuList_NewModel> upcomingYourBookingModelArrayList_new = new ArrayList<VegMenuList_NewModel>();
    private InternetConnection internetConnection = new InternetConnection();
    public static NonVegMenuListFragment vegMenuListFragment;
    private DatabaseHelper databaseHelper;
    private Button fragment_common_list_recycler_button_view_cart;
    private TextView textview_no_record_found;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_common_list_recycler, container, false);
        findViewByIds(rootView);

        databaseHelper = new DatabaseHelper(getActivity());

        vegMenuListFragment = this;
        getInstance();
        NonVegMenuListAdapter.updateValuesInterface = (NonVegMenuListAdapter.UpdateValuesInterface) vegMenuListFragment;
        //MenuListActivity.updateDataOnTabChangeNonVeg = (MenuListActivity.UpdateDataOnTabChangeNonVeg) vegMenuListFragment;

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setListValues();
            }
        });

        fragment_common_list_recycler_button_view_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPassword = new Intent(getActivity(), CartPreviewActivity.class);
                startActivity(intentForgotPassword);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

//        fragment_recent_jobs_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intentForgotPassword = new Intent(getActivity(), YourBookingDetailsActivity.class);
//                //intentForgotPassword.putExtra(getResources().getString(R.string.key_status), upcomingYourBookingModelArrayList.get(position).getStatus());
//                intentForgotPassword.putExtra(getResources().getString(R.string.key_flag), "0");
//                startActivity(intentForgotPassword);
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        }));
        return rootView;
    }

    private void findViewByIds(View view){

        textview_no_record_found = (TextView) view.findViewById(R.id.textview_no_record_found);
        fragment_recent_jobs_recycler_view = (RecyclerView) view.findViewById(R.id.fragment_recent_jobs_recycler_view);
        swipe_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        fragment_common_list_recycler_button_view_cart = (Button) view.findViewById(R.id.fragment_common_list_recycler_button_view_cart);
    }

    private void setListValues(){

        fragment_common_list_recycler_button_view_cart.setVisibility(View.GONE);

        if (internetConnection.isNetworkAvailable(getActivity())) {
            getMenuListByType();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void getMenuListByType() {

        swipe_refresh_layout.setRefreshing(true);

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getActivity().getResources().getString(R.string.field_subCategory), MenuListActivity.key_subCategoryId);
        postParams.put(getActivity().getResources().getString(R.string.field_menuType), "Non-Veg"); //1 = Veg, 2 = Non Veg

        //((ParentActivity) getActivity()).showProcessingDialog();
        APIManager.requestPostMethod(getActivity(), getResources().getString(R.string.getMenuListByType), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray array = jsonObject.getJSONArray(Constants.data);

                    if (jsonObject.getInt("status") == 200) {
                        upcomingYourBookingModelArrayList.clear();
                        Gson gson = new Gson();
                        Type founderListType = new TypeToken<List<VegMenuListModel>>(){
                        }.getType();
                        upcomingYourBookingModelArrayList = gson.fromJson(array.toString(), founderListType);

                        if (upcomingYourBookingModelArrayList.size() > 0) {

                            textview_no_record_found.setVisibility(View.GONE);
                            swipe_refresh_layout.setVisibility(View.VISIBLE);

                            yourUpcomingBookingAdapter = new VegMenuListAdapter(getActivity(),upcomingYourBookingModelArrayList_new);
                            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                            fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
                            fragment_recent_jobs_recycler_view.setAdapter(yourUpcomingBookingAdapter);
                            fragment_recent_jobs_recycler_view.setEnabled(true);
                            swipe_refresh_layout.setRefreshing(false);
                        }else {

                            textview_no_record_found.setVisibility(View.VISIBLE);
                            swipe_refresh_layout.setVisibility(View.GONE);
                        }


                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                swipe_refresh_layout.setRefreshing(false);
                //((ParentActivity) getActivity()).dismissProgressDialog();
            }
        });
    }

    @Override
    public void onUpdateValuesInterface(String quantity, int position, int totalQuantity, VegMenuListModel vegMenuListModel, String menu_status) {
        CartModel cartModel = new CartModel(",", "", "111", vegMenuListModel.getDish_id(), vegMenuListModel.getDish_name(), vegMenuListModel.getDish_price(), vegMenuListModel.getDish_image(),
                quantity, menu_status);

        int menuCount = databaseHelper.getCountOfMenuById(Integer.parseInt(vegMenuListModel.getDish_id()), "111");

        if(menuCount == 0){
            databaseHelper.addInTo(cartModel);
        }else{
            databaseHelper.updateRecordById(Integer.parseInt(vegMenuListModel.getDish_id()), "111", cartModel);
        }

        if(quantity.equalsIgnoreCase("0")){
            databaseHelper.deleteItem(Integer.parseInt(vegMenuListModel.getDish_id()), "111");
        }
        System.out.println("totalQuantity: "+totalQuantity);
        if(totalQuantity == 0){
            fragment_common_list_recycler_button_view_cart.setVisibility(View.GONE);
            databaseHelper.deleteAll();
        }else{
            fragment_common_list_recycler_button_view_cart.setVisibility(View.VISIBLE);
        }
        System.out.println("cart values: "+databaseHelper.getCartItemsByUserId("111").size());
    }

    public static NonVegMenuListFragment getInstance(){
        return vegMenuListFragment;
    }

    @Override
    public void updateDataOnTabChangeNonVeg() {
        setListValues();
    }
}
