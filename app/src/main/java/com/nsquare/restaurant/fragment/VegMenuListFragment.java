package com.nsquare.restaurant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.CartPreviewActivity;
import com.nsquare.restaurant.activity.MenuListActivity;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.adapter.VegMenuListAdapter;
import com.nsquare.restaurant.databasehelper.DatabaseHelper;
import com.nsquare.restaurant.model.CartModel;
import com.nsquare.restaurant.model.vegMenuList.DishCustomModel;
import com.nsquare.restaurant.model.vegMenuList.DishExtrasModel;
import com.nsquare.restaurant.model.vegMenuList.VegMenuListModel;
import com.nsquare.restaurant.model.vegMenuList.VegMenuList_NewModel;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;
import com.nsquare.restaurant.util.InternetConnection;
import com.nsquare.restaurant.util.RecyclerItemClickListener;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class VegMenuListFragment extends Fragment implements VegMenuListAdapter.UpdateValuesInterface, MenuListActivity.UpdateDataOnTabChangeVeg
{
    private Button fragment_common_list_recycler_button_view_cart;
    private VegMenuListAdapter yourUpcomingBookingAdapter;
    private QuantityMenuAdapter quantityMenuAdapter;
    private CustomMenuAdapter customMenuAdapter;
    private ExtraMenuAdapter extraMenuAdapter;

    private SwipeRefreshLayout swipe_refresh_layout;
    private TextView textview_no_record_found;
    private TextView textView_total;
    //private RelativeLayout relative_layout_common_list_recycler;
    private RecyclerView fragment_recent_jobs_recycler_view;
    private RelativeLayout relative_layout_checkout;
    private ArrayList<VegMenuList_NewModel> upcomingYourBookingModelArrayList_new = new ArrayList<VegMenuList_NewModel>();
    private InternetConnection internetConnection = new InternetConnection();
    public static VegMenuListFragment vegMenuListFragment;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private String tableNumberId;
    private Context context;
    private boolean isRepeatClicable;
    private int currentQty=0;
    private  String dishes_extra_id="";

    private int totalQuantityCount = 0;
    private int selectedQuantity;
    private String[] quantityArray;
    //private int tempGroupIdCustom ;
    private ArrayList<String> selectedCustomRadioButtonId = new ArrayList<>();
    private String selectedQuantityRadioButtonId="";
    //private int selectedCustomRadioButtonId;
    private ArrayList<String> selectedCheckBoxId = new ArrayList<>();
    private ArrayList<String> selectedRadioButtonIdFromGroupId = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_common_list_recycler, container, false);
        findViewByIds(rootView);
        //fetch table number
        tableNumberId = sharedPreferences.getString(getResources().getString(R.string.sharedPreferencetableid),"");
        databaseHelper = new DatabaseHelper(getActivity());

        vegMenuListFragment = this;
        getInstance();
        VegMenuListAdapter.updateValuesInterface = (VegMenuListAdapter.UpdateValuesInterface) vegMenuListFragment;
        MenuListActivity.updateDataOnTabChangeVeg = (MenuListActivity.UpdateDataOnTabChangeVeg) vegMenuListFragment;

        setListValues();
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setListValues();
            }
        });

        relative_layout_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,CartPreviewActivity.class);
                startActivity(intent);
            }
        });
        textView_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on checkout move to cart view
                Intent intent = new Intent(context,CartPreviewActivity.class);
                startActivity(intent);

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
        fragment_recent_jobs_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // if alredy in card pass selected_quantity
                if(!upcomingYourBookingModelArrayList_new.get(position).getSelected_dish_quantity().isEmpty()){
                    selectedQuantity = Integer.parseInt(upcomingYourBookingModelArrayList_new.get(position).getSelected_dish_quantity());
                }
                    showCoordinatorLayout_with_custome_menu_if_available(position, upcomingYourBookingModelArrayList_new.get(position).getDish_id(),selectedQuantity);
            }
        }));
        //behavior = BottomSheetBehavior.from(bottomSheet);
        //check slider is already open or not
        return rootView;
    }

    private void findViewByIds(View view){

        fragment_recent_jobs_recycler_view = (RecyclerView) view.findViewById(R.id.fragment_recent_jobs_recycler_view);
        relative_layout_checkout = (RelativeLayout) view.findViewById(R.id.relative_layout_checkout);
        swipe_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        textview_no_record_found = (TextView) view.findViewById(R.id.textview_no_record_found);
        textView_total = (TextView) view.findViewById(R.id.textView_total_qty);
        //relative_layout_common_list_recycler = (RelativeLayout) view.findViewById(R.id.relative_layout_common_list_recycler);
        fragment_common_list_recycler_button_view_cart = (Button) view.findViewById(R.id.fragment_common_list_recycler_button_view_cart);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    private void setListValues(){
        fragment_common_list_recycler_button_view_cart.setVisibility(View.GONE);
        if (internetConnection.isNetworkAvailable(getActivity())) {
            getMenuListByType();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }
    public static VegMenuListFragment getInstance(){
        return vegMenuListFragment;
    }

    private void getMenuListByType() {

        swipe_refresh_layout.setRefreshing(true);

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getActivity().getResources().getString(R.string.field_subCategory), MenuListActivity.key_subCategoryId);
        postParams.put(getActivity().getResources().getString(R.string.field_menuType), Constants.veg); //1 = Veg, 2 = Non Veg
        postParams.put(getActivity().getResources().getString(R.string.field_table_id), Constants.table_id); //1 = Veg, 2 = Non Veg

        APIManager.requestPostMethod(getActivity(), getResources().getString(R.string.getMenuListByType), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String totalCartAmount = jsonObject.getString("totalcartamount");
                    if(!totalCartAmount.isEmpty()){
                        relative_layout_checkout.setVisibility(View.VISIBLE);
                        textView_total.setText("Rs. "+ totalCartAmount);
                    }else{
                        relative_layout_checkout.setVisibility(View.GONE);
                    }
                    String jsonString =  jsonObject.getJSONArray(Constants.data).toString();
                    if (jsonObject.getInt("status") == 200) {

                        upcomingYourBookingModelArrayList_new.clear();

                        Gson gson = new Gson();
                        String jsonOutput = jsonString;
                        Type listType = new TypeToken<ArrayList<VegMenuList_NewModel>>(){}.getType();
                        upcomingYourBookingModelArrayList_new = gson.fromJson(jsonOutput, listType);
                        //set data to adapter [list]
                        if (upcomingYourBookingModelArrayList_new.size() > 0) {

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
                    }else if (jsonObject.getInt("status") == 400) {
                        Toast.makeText(getActivity(), jsonObject.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                swipe_refresh_layout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onUpdateValuesInterface(String quantity, int position, int totalQuantity, VegMenuListModel vegMenuListModel, String menu_status) {

        if(vegMenuListModel.isDish_customizable()) {
            showCoordinatorLayout(quantity, position, totalQuantity, vegMenuListModel, menu_status);
        }else{
            /*Developer : Ritu Chavan
             *  Modified Date : 23/07/2018
             *  */
            CartModel cartModel = new CartModel(",", "", "01", vegMenuListModel.getDish_id(), vegMenuListModel.getDish_name(), vegMenuListModel.getDish_price(), vegMenuListModel.getDish_image(),
                    quantity, menu_status);

            System.out.println("tableNumberId : " + tableNumberId);
            System.out.println("totalQuantity : " + totalQuantity);

            int menuCount = 01;

            if(tableNumberId != null)
                menuCount  = databaseHelper.getCountOfMenuById(Integer.parseInt(vegMenuListModel.getDish_id()), "01");
            //int menuCount = databaseHelper.getCountOfMenuById(Integer.parseInt(vegMenuListModel.getId()), cartModel.getDatabase_user_id());
            if(menuCount == 0){
                databaseHelper.addInTo(cartModel);
                isRepeatClicable = false;
            }else{
                databaseHelper.updateRecordById(Integer.parseInt(vegMenuListModel.getDish_id()), cartModel.getDatabase_user_id(), cartModel);
                isRepeatClicable = true;
            }

            if(quantity.equalsIgnoreCase("0")){
                databaseHelper.deleteItem(Integer.parseInt(vegMenuListModel.getDish_id()), cartModel.getDatabase_user_id());
            }
            System.out.println("totalQuantity: "+totalQuantity);
            if(totalQuantity == 0){
                fragment_common_list_recycler_button_view_cart.setVisibility(View.GONE);
                databaseHelper.deleteAll();
            }else{
                fragment_common_list_recycler_button_view_cart.setVisibility(View.VISIBLE);
            }

            System.out.println("cart values: "+databaseHelper.getCartItemsByUserId(cartModel.getDatabase_user_id()).size());
            //adjust popup on click of items
            if(totalQuantity > currentQty){
                //show slider
                currentQty = totalQuantity;
                //check  customization is available or not
//                if(vegMenuListModel.isDish_customizable())
//                    showCoordinatorLayout(position);
            }else {
                //do not show slider
                currentQty--;
            }
        }
//        if(quantity.equalsIgnoreCase("0")){
//            databaseHelper.deleteItem(Integer.parseInt(vegMenuListModel.getId()));
//        }
    }
    @Override
    public void updateDataOnTabChangeVeg() {
        setListValues();
    }

    public void showCoordinatorLayout(final String quantity, final int position, final int totalQuantity, final VegMenuListModel vegMenuListModel, final String menu_status){

        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.show();

        Button button_add_new = (Button)view.findViewById(R.id.button_add_new);
        Button button_repeat_last = (Button)view.findViewById(R.id.button_repeat_last);
        TextView textview_cancel = (TextView) view.findViewById(R.id.textview_cancel);
        TextView textview_qty = (TextView) view.findViewById(R.id.textview_qty);

        //on adding first time donot click repeat
        if(isRepeatClicable){
            button_repeat_last.setEnabled(true);
            button_repeat_last.setClickable(true);
        }else {
            button_repeat_last.setEnabled(false);
            button_repeat_last.setClickable(false);
        }

        button_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                alertAddCustomMenu(quantity, position, totalQuantity, vegMenuListModel, menu_status);
            }
        });

        button_repeat_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO :fetch previously added customization and insert into order menu table
                dialog.dismiss();
            }
        });

        textview_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    //Custome menu popup
    public void alertAddCustomMenu(final String quantity, final int position, final int totalQuantity, final VegMenuListModel vegMenuListModel, final String menu_status){

        final AlertDialog alert11;
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(context);
        final View view1 = factory.inflate(R.layout.list_menu_open_popup, null);
        builder1.setView(view1);
        builder1.setCancelable(true);

        alert11 = builder1.create();
        alert11.setCanceledOnTouchOutside(false);
        //alert11.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (!((Activity) context).isFinishing()) {
            //show dialog
            alert11.show();
        }
        //ArrayList<CustomeOrderItems> requestsItemArrayList = new ArrayList<>();
        // recyclerView_list_popup = (RecyclerView)view1.findViewById(R.id.recyclerView_list_popup);
        Button button_add = (Button) view1.findViewById(R.id.button_add);
        Button button_cancel = (Button) view1.findViewById(R.id.button_cancel);

        //custom/extra/quantity

        //customExtraMenuAdapter = new CustomExtraMenuAdapter(context, upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getExtra());
        //final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        // linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        //recyclerView_list_popup.setLayoutManager(linearLayoutManager);
        //fragment_recent_jobs_recycler_view.addItemDecoration(new MyDividerItemDecoration(activity, LinearLayoutManager.VERTICAL, 8));
        //recyclerView_list_popup.setAdapter(customExtraMenuAdapter);
        //show extras
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isAtleastOneSelected = true;
                //get extras selected from alert and add into TABLE_ORER table
                // customMenuParamterDetailsItem.setId(upcomingYourBookingModelArrayList.get(position).getDish_id());
                // customMenuParamterDetailsItem  = new CustomMenuParamterDetailsItem(customMenuParamterDetailsItem);
                // databaseHelper.addInToOrderMenu();

                //for(int k = 0; k < upcomingYourBookingModelArrayList.size(); k++){
                for (int a = 0; a < upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getExtra().size(); a++){
/*
                    if(upcomingYourBookingModelArrayList.get(position).getDish_extras().get(a).getDish_extra_is_selected().equalsIgnoreCase("0")){
                        isAtleastOneSelected = false;
                        break;
                    }
                    System.out.println("Selected Extras status: "+upcomingYourBookingModelArrayList.get(position).getDish_extras().get(a).getDish_extra_name()+" ******* "+
                            upcomingYourBookingModelArrayList.get(position).getDish_extras().get(a).getDish_extra_is_selected());*/
                }

                if(isAtleastOneSelected){

//                       for(int i = 0; i < vegMenuListModel.getDish_extras().size(); i++){
//                           vegMenuListModel.getDish_extras().get(position).get
//                       }

                    // for (int a = 0; a < upcomingYourBookingModelArrayList_new.getDishCustomExtrasModel().getExtra().size(); a++){


//                           CartModel cartModel = new CartModel();
//                           cartModel.setDatabase_menu_id(upcomingYourBookingModelArrayList.get(position).getDish_id());
//                           cartModel.setDatabase_menu_name(upcomingYourBookingModelArrayList.get(position).getDish_name());
//                           cartModel.setDatabase_menu_image(upcomingYourBookingModelArrayList.get(position).getDish_image());
//                           cartModel.setDatabase_menu_price(upcomingYourBookingModelArrayList.get(position).getDish_price());
//                           cartModel.setDatabase_menu_id(upcomingYourBookingModelArrayList.get(position).getDish_id());
//                           cartModel.setDatabase_menu_id(upcomingYourBookingModelArrayList.get(position).getDish_id());
//

                        /*System.out.println("Selected Extras status: "+upcomingYourBookingModelArrayList.get(position).getDish_extras().get(a).getDish_extra_name()+" ******* "+
                                upcomingYourBookingModelArrayList.get(position).getDish_extras().get(a).getDish_extra_is_selected());*/
                    //}


                    CartModel cartModel = new CartModel(",", "", "01", vegMenuListModel.getDish_id(), vegMenuListModel.getDish_name(), vegMenuListModel.getDish_price(), vegMenuListModel.getDish_image(),
                            quantity, menu_status);

                    System.out.println("tableNumberId : " + tableNumberId);
                    System.out.println("totalQuantity : " + totalQuantity);

                    int menuCount = 01;

                    if(tableNumberId != null)
                        menuCount  = databaseHelper.getCountOfMenuById(Integer.parseInt(vegMenuListModel.getDish_id()), "01");
                    //int menuCount = databaseHelper.getCountOfMenuById(Integer.parseInt(vegMenuListModel.getId()), cartModel.getDatabase_user_id());
                    if(menuCount == 0){
                        databaseHelper.addInTo(cartModel);
                        isRepeatClicable = false;
                    }else{
                        databaseHelper.updateRecordById(Integer.parseInt(vegMenuListModel.getDish_id()), cartModel.getDatabase_user_id(), cartModel);
                        isRepeatClicable = true;
                    }

                    if(quantity.equalsIgnoreCase("0")){
                        databaseHelper.deleteItem(Integer.parseInt(vegMenuListModel.getDish_id()), cartModel.getDatabase_user_id());
                    }
                    System.out.println("totalQuantity: "+totalQuantity);
                    if(totalQuantity == 0){
                        fragment_common_list_recycler_button_view_cart.setVisibility(View.GONE);
                        databaseHelper.deleteAll();
                    }else{
                        fragment_common_list_recycler_button_view_cart.setVisibility(View.VISIBLE);
                    }

                    System.out.println("cart values: "+databaseHelper.getCartItemsByUserId(cartModel.getDatabase_user_id()).size());
                    //adjust popup on click of items
                    if(totalQuantity > currentQty){
                        //show slider
                        currentQty = totalQuantity;
                        //check  customization is available or not
//                if(vegMenuListModel.isDish_customizable())
//                    showCoordinatorLayout(position);
                    }else {
                        //do not show slider
                        currentQty--;
                    }

                }else{
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_select_atleast_one), Toast.LENGTH_SHORT).show();
                }
                //}
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }
    //adapter to show extras



    //open new co-ordinator layout
    public void showCoordinatorLayout_with_custome_menu_if_available(final int position, final String dish_id, final int selected_quantity){

        // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.show();

        Button button_add_to_card = (Button)view.findViewById(R.id.button_add_to_card);
        TextView textview_cancel = (TextView) view.findViewById(R.id.textview_cancel);
        TextView textview_label_qty = (TextView) view.findViewById(R.id.textview_label_qty);
        TextView textview_label_custom = (TextView) view.findViewById(R.id.textview_label_custom);

        RecyclerView recyclerView_list_popup = (RecyclerView)view.findViewById(R.id.recyclerView_list_popup);
        RecyclerView recyclerView_Quantity = (RecyclerView)view.findViewById(R.id.recyclerView_Quantity);
        RecyclerView recyclerView_extra = (RecyclerView)view.findViewById(R.id.recyclerView_extra);

        ImageView list_item_veg_menu_list_imageview_plus = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_plus);
        ImageView list_item_veg_menu_list_imageview_minus = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_minus);
        final TextView list_item_veg_menu_list_textview_quantity = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_quantity);

       /* customExtraMenuAdapter = new CustomExtraMenuAdapter(context, upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel(),size);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView_list_popup.setLayoutManager(linearLayoutManager);
        //fragment_recent_jobs_recycler_view.addItemDecoration(new MyDividerItemDecoration(activity, LinearLayoutManager.VERTICAL, 8));
        recyclerView_list_popup.setAdapter(customExtraMenuAdapter);*/

        //custom
        if(upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getCustom() != null && !upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getCustom().isEmpty()) {

            textview_label_custom.setVisibility(View.VISIBLE);
            customMenuAdapter = new CustomMenuAdapter(context, upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getCustom());
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
            recyclerView_list_popup.setLayoutManager(linearLayoutManager);
            //fragment_recent_jobs_recycler_view.addItemDecoration(new MyDividerItemDecoration(activity, LinearLayoutManager.VERTICAL, 8));
            recyclerView_list_popup.setAdapter(customMenuAdapter);
        }

        //Quantity
        if(upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getQuantity() != null && !upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getQuantity().isEmpty()) {

            textview_label_qty.setVisibility(View.VISIBLE);
            quantityMenuAdapter = new QuantityMenuAdapter(context, upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getQuantity());
            final LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
            linearLayoutManager1.setOrientation(LinearLayout.VERTICAL);
            recyclerView_Quantity.setLayoutManager(linearLayoutManager1);
            recyclerView_Quantity.setAdapter(quantityMenuAdapter);
        }

        //extra
        if(upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getExtra() != null && !upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getExtra().isEmpty()) {
            extraMenuAdapter = new ExtraMenuAdapter(context, upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getExtra());
            final LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
            linearLayoutManager2.setOrientation(LinearLayout.VERTICAL);
            recyclerView_extra.setLayoutManager(linearLayoutManager2);
            recyclerView_extra.setAdapter(extraMenuAdapter);
        }


        // if already present in cart
        final int[] quantityCount = {0};
        quantityArray = new String[upcomingYourBookingModelArrayList_new.size()];

        if(selected_quantity > 1 ){

            quantityCount[0] = quantityCount[0] + selected_quantity;
            totalQuantityCount = selected_quantity;

            quantityArray[position] = String.valueOf(quantityCount[0]);

            list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
            list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");

        }
        textview_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        button_add_to_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call Resto/addCart
                String dish_qty = totalQuantityCount+"";
                String table_id = tableNumberId;
                dishes_extra_id = "";
                //for group of radio buttons
                for(String str : selectedRadioButtonIdFromGroupId){
                    selectedCheckBoxId.add(str);
                }
                //for single radio button
                selectedCheckBoxId.add(selectedQuantityRadioButtonId);

                dishes_extra_id = TextUtils.join(",", selectedCheckBoxId);

                if (internetConnection.isNetworkAvailable(getActivity())) {
                    addMenuToCart(dish_id,dish_qty,table_id,dishes_extra_id,dialog);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

        list_item_veg_menu_list_imageview_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantityCount[0] = quantityCount[0] + 1;
                totalQuantityCount = totalQuantityCount + 1;

                quantityArray[position] = String.valueOf(quantityCount[0]);

                list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");

            }
        });

        list_item_veg_menu_list_imageview_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check remaining quantity if 1 left
                //open popup
                //CALL REMOVEItem

                if(quantityCount[0] <=  1) {

                    if(selected_quantity > 1){
                        alertRemoveItem(dish_id,dialog);
                    }else
                        Toast.makeText(context,"Min quantity reached",Toast.LENGTH_SHORT).show();
                }else {
                    quantityCount[0] = quantityCount[0] - 1;
                    totalQuantityCount = totalQuantityCount - 1;

                    quantityArray[position] = String.valueOf(quantityCount[0]);
                    list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                    list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");
                }
            }
        });
    }


    //add items to cart
    private void addMenuToCart(String dish_id, String dish_qty, String table_no, String dish_extra_id,final BottomSheetDialog bottomSheetDialog) {

        ((ParentActivity)getActivity()).showProcessingDialog();

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getActivity().getResources().getString(R.string.field_dish),dish_id);
        postParams.put(getActivity().getResources().getString(R.string.field_dish_quantity),dish_qty);
        postParams.put(getActivity().getResources().getString(R.string.field_table),Constants.table_id);
        // send ids by comma seperated and it is optional
        postParams.put(getActivity().getResources().getString(R.string.field_dishes_extra_id), dish_extra_id);

        APIManager.requestPostMethod(getActivity(), getResources().getString(R.string.addCart), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    dishes_extra_id="";
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        ((ParentActivity)getActivity()).dismissProgressDialog();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(getActivity(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                        //refresh
                        if (internetConnection.isNetworkAvailable(getActivity())) {
                            getMenuListByType();
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                        }

                    }else if (jsonObject.getInt("status") == 400) {
                        ((ParentActivity)getActivity()).dismissProgressDialog();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(getActivity(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    bottomSheetDialog.dismiss();
                    ((ParentActivity)getActivity()).dismissProgressDialog();
                }
            }
        });
    }

    //remove cart
    private void removeMenuFromCart(String dish_id, final BottomSheetDialog bottomSheetDialog) {

        ((ParentActivity)getActivity()).showProcessingDialog();

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getActivity().getResources().getString(R.string.field_cart_id),dish_id);
        postParams.put(getActivity().getResources().getString(R.string.field_table_id),Constants.table_id);

        APIManager.requestPostMethod(getActivity(), getResources().getString(R.string.removeCart), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        ((ParentActivity)getActivity()).dismissProgressDialog();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(getActivity(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                        //refresh page
                        if (internetConnection.isNetworkAvailable(getActivity())) {
                            getMenuListByType();
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                        }

                    }else if (jsonObject.getInt("status") == 400) {
                        ((ParentActivity)getActivity()).dismissProgressDialog();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(getActivity(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    bottomSheetDialog.dismiss();
                    ((ParentActivity)getActivity()).dismissProgressDialog();
                }
            }
        });
    }
    //pop of remove item
    public void alertRemoveItem(final String dish_id, final BottomSheetDialog bottomSheetDialog){

        final AlertDialog alert11;
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());

        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view1 = factory.inflate(R.layout.dialog_remove_item, null);
        builder1.setView(view1);
        builder1.setCancelable(true);

        alert11 = builder1.create();
        alert11.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert11.show();
        Button button_popup_no = (Button) view1.findViewById(R.id.button_popup_no);
        Button button_popup_yes = (Button) view1.findViewById(R.id.button_popup_yes);

        button_popup_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
            }
        });

        button_popup_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
                if (internetConnection.isNetworkAvailable(getActivity())) {
                    removeMenuFromCart(dish_id,bottomSheetDialog);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //TODO : adapter to set Custom value : Group of radio buttons
    public class CustomMenuAdapter extends RecyclerView.Adapter<CustomMenuAdapter.ViewHolder> {

        private ArrayList<DishCustomModel> requestItems;
        private Context context;

        public CustomMenuAdapter(Context context,ArrayList<DishCustomModel> requestItems) {
            this.context = context;
            this.requestItems = requestItems;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView textView_label;
            public RadioGroup radioGroup;

            public ViewHolder(View itemView) {
                super(itemView);

                textView_label = (TextView) itemView.findViewById(R.id.textview_label);
                radioGroup = (RadioGroup) itemView.findViewById(R.id.radio_grp_select);

            }
            @Override
            public void onClick(View v) {
            }
        }

        @Override
        public  CustomMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View viewCustom = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_extra_menu_popup, parent, false);
            return new CustomMenuAdapter.ViewHolder(viewCustom);

        }

        @Override
        public void onBindViewHolder(final CustomMenuAdapter.ViewHolder holder,final int position) {
            //add logic here
            final DishCustomModel dishExtrasModel = requestItems.get(position);
            //TODO : FOR 'CUSTOM'
            holder.textView_label.setText(dishExtrasModel.getGroup_label());
            //call radio buttons
            for(int i = 0 ; i < requestItems.get(position).getDishCustomDataModelArrayList().size(); i++){

                final RadioButton radioButton = new RadioButton(context);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(context, null);
                params.setMargins(5, 10, 20, 2);
                radioButton.setLayoutParams(params);
                radioButton.setText(dishExtrasModel.getDishCustomDataModelArrayList().get(i).getDish_extra_name());
                radioButton.setTextSize(14);
                radioButton.setPadding(5, 0, 7, 0);
                radioButton.setButtonDrawable(getResources().getDrawable(R.drawable.selector_role_radio_button_bacground));
                radioButton.setTextColor(ContextCompat.getColorStateList(context, R.color.white));
                radioButton.setId(Integer.parseInt(dishExtrasModel.getDishCustomDataModelArrayList().get(i).getDish_extra_id()));
                holder.radioGroup.addView(radioButton);

                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                        if(isChecked){
                            selectedRadioButtonIdFromGroupId.add(radioButton.getId()+"");
                        }else {
                            ArrayList<String> tempSelectedRadios = new ArrayList<>();
                            tempSelectedRadios = selectedRadioButtonIdFromGroupId;

                            for (int a = 0 ; a < selectedRadioButtonIdFromGroupId.size(); a++){
                                if(selectedRadioButtonIdFromGroupId.get(a).equalsIgnoreCase(radioButton.getId()+"")){
                                    tempSelectedRadios.remove(a);
                                }
                            }
                            //selectedRadioButtonIdFromGroupId.clear();
                            selectedRadioButtonIdFromGroupId = tempSelectedRadios;
                        }

                        for (int k = 0; k < selectedRadioButtonIdFromGroupId.size(); k++){
                            System.out.println("selectedRadioButtonIdFromGroupId: "+selectedRadioButtonIdFromGroupId.get(k));
                        }
                        System.out.println("selectedRadioButtonIdFromGroupId: size "+selectedRadioButtonIdFromGroupId.size());
                    }
                });

//                holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
//
//                        System.out.println("group checked ID :" +radioGroup.getCheckedRadioButtonId());
//                        selectedRadioButtonIdFromGroupId.add(radioGroup.getCheckedRadioButtonId()+"");
//
//                    }
//                });
            }
        }
        @Override
        public int getItemCount() {
            return requestItems.size();
        }
    }

    //TODO 'Extra' : Single checkbox
    public class ExtraMenuAdapter extends RecyclerView.Adapter<ExtraMenuAdapter.ViewHolder> {

        private ArrayList<DishExtrasModel> requestItems;
        private Context context;
        ArrayList<Boolean> positionArray;

        public ExtraMenuAdapter(Context context, ArrayList<DishExtrasModel> requestItems) {
            this.context = context;
            this.requestItems = requestItems;
            positionArray = new ArrayList<Boolean>(requestItems.size());

            for(int i =0;i<requestItems.size();i++){
                positionArray.add(false);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView textview_label_chk;
            public CheckBox checkbox_select_multiple_menu;

            public ViewHolder(View itemView) {
                super(itemView);

                checkbox_select_multiple_menu = (CheckBox) itemView.findViewById(R.id.checkbox_select_multiple_menu);
                textview_label_chk = (TextView) itemView.findViewById(R.id.textview_label_chk);

            }

            @Override
            public void onClick(View v) {
            }
        }


        @Override
        public ExtraMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View viewCustom = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customise_menu_popup, parent, false);
            return new ExtraMenuAdapter.ViewHolder(viewCustom);

        }

        @Override
        public void onBindViewHolder(final ExtraMenuAdapter.ViewHolder holder, final int position) {
            //add logic here
            final DishExtrasModel dishExtrasModel = requestItems.get(position);
            //holder.textview_label_chk.setText("Extras");
            holder.checkbox_select_multiple_menu.setText(dishExtrasModel.getDish_extra_name());
            holder.checkbox_select_multiple_menu.setTag(position);

            holder.checkbox_select_multiple_menu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        positionArray.set(position, true);
                        // if(!selectedCheckBoxId.isEmpty()) {
                        selectedCheckBoxId.add(dishExtrasModel.getDish_extra_id());
                        //  }
                        //dishExtrasModel.se("1");
                        //myInterfaceFavouriteAdded.checkboxFavouriteAdded("1", commodityId);
                    } else {
                        positionArray.set(position, false);
                        selectedCheckBoxId.remove(dishExtrasModel.getDish_extra_id());
                        // completeProfileItem.setCommodity_status("0");
                        //  myInterfaceFavouriteAdded.checkboxFavouriteAdded("0", commodityId);
                    }

                }
            });


        }
        @Override
        public int getItemCount() {
            return requestItems.size();
        }
    }
    //END
    //TODO Quantity : Single radiobuttons
    public class QuantityMenuAdapter extends RecyclerView.Adapter<QuantityMenuAdapter.ViewHolder> {

        private ArrayList<DishExtrasModel> requestItems;
        private Context context;

        public QuantityMenuAdapter(Context context,ArrayList<DishExtrasModel> requestItems) {
            this.context = context;
            this.requestItems = requestItems;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView textView_label;
            public RadioGroup radioGroup;

            public ViewHolder(View itemView) {
                super(itemView);

                textView_label = (TextView) itemView.findViewById(R.id.textview_label);
                radioGroup = (RadioGroup) itemView.findViewById(R.id.radio_grp_select);
                //radioButton = new RadioButton(context);

            }
            @Override
            public void onClick(View v) {
            }
        }

        @Override
        public  QuantityMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View viewCustom = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_extra_menu_popup, parent, false);
            return new QuantityMenuAdapter.ViewHolder(viewCustom);

        }

        @Override
        public void onBindViewHolder(final QuantityMenuAdapter.ViewHolder holder, final int position) {
            //add logic here
            final DishExtrasModel dishExtrasModel = requestItems.get(position);
            //TODO : FOR 'CUSTOM'

            holder.textView_label.setText("Quantity");
            for(int i = 0 ; i < requestItems.size(); i++){

                final RadioButton radioButton = new RadioButton(context);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(context, null);
                params.setMargins(5, 10, 20, 2);
                radioButton.setLayoutParams(params);
                radioButton.setText(requestItems.get(i).getDish_extra_name());
                radioButton.setTextSize(14);
                radioButton.setPadding(5, 0, 7, 0);
                radioButton.setButtonDrawable(getResources().getDrawable(R.drawable.selector_role_radio_button_bacground));
                radioButton.setTextColor(ContextCompat.getColorStateList(context, R.color.white));
                radioButton.setId(Integer.parseInt(requestItems.get(i).getDish_extra_id()));
                holder.radioGroup.addView(radioButton);

                holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int position) {
                        // String selectedRadioButtonId = i + "";
                        selectedQuantityRadioButtonId = radioGroup.getCheckedRadioButtonId()+"";
                        //selectedQuantityRadioButtonId= requestItems.get(i).getDish_extra_id();
                        //System.out.println("radio : "+selectedQuantityRadioButtonId);
                    }
                });
            }
        }
        @Override
        public int getItemCount() {
            return 1;
        }
    }
}
