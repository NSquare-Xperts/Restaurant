package com.nsquare.restaurant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.nsquare.restaurant.model.vegMenuList.DishCustomExtrasModel;
import com.nsquare.restaurant.model.vegMenuList.VegMenuListModel;
import com.nsquare.restaurant.model.vegMenuList.VegMenuList_NewModel;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;
import com.nsquare.restaurant.util.InternetConnection;
import com.nsquare.restaurant.util.RecyclerItemClickListener;
import com.nsquare.restaurant.util.RobotoRegular.CheckBoxTextRoboto;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class VegMenuListFragmentBackup extends Fragment implements VegMenuListAdapter.UpdateValuesInterface, MenuListActivity.UpdateDataOnTabChangeVeg
{
    private Button fragment_common_list_recycler_button_view_cart;
    private VegMenuListAdapter yourUpcomingBookingAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private TextView textview_no_record_found;
    private RelativeLayout relative_layout_common_list_recycler;
    private RecyclerView fragment_recent_jobs_recycler_view;
    private RelativeLayout relative_layout_checkout;
    private ArrayList<VegMenuList_NewModel> upcomingYourBookingModelArrayList_new = new ArrayList<VegMenuList_NewModel>();
    private InternetConnection internetConnection = new InternetConnection();
    public static VegMenuListFragmentBackup vegMenuListFragment;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private String tableNumberId;
    private Context context;
    private boolean isRepeatClicable;
    private int currentQty=0;
    private CustomExtraMenuAdapter customExtraMenuAdapter;
    public RadioButton radioButton;
    //CustomMenuParamterDetailsItem customMenuParamterDetailsItem;
    private int totalQuantityCount = 0;
    private String[] quantityArray;
    private int tempGroupIdCustom ;

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
                showCoordinatorLayout_with_custome_menu_if_available(position, upcomingYourBookingModelArrayList_new.get(position).getDish_id(),"2");
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
        relative_layout_common_list_recycler = (RelativeLayout) view.findViewById(R.id.relative_layout_common_list_recycler);
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
    public static VegMenuListFragmentBackup getInstance(){
        return vegMenuListFragment;
    }

    private void getMenuListByType() {

        swipe_refresh_layout.setRefreshing(true);

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getActivity().getResources().getString(R.string.field_subCategory), MenuListActivity.key_subCategoryId);
        postParams.put(getActivity().getResources().getString(R.string.field_menuType), Constants.veg); //1 = Veg, 2 = Non Veg

        APIManager.requestPostMethod(getActivity(), getResources().getString(R.string.getMenuListByType), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
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
        RecyclerView recyclerView_list_popup = (RecyclerView)view1.findViewById(R.id.recyclerView_list_popup);
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
    public class CustomExtraMenuAdapter extends RecyclerView.Adapter<CustomExtraMenuAdapter.ViewHolder> {

        //private List<DishCustomExtrasModel> requestItems;
        private DishCustomExtrasModel requestItems;
        private Context context;
        private int size;

        public CustomExtraMenuAdapter(Context context,DishCustomExtrasModel requestItems, int size) {
            this.context = context;
            this.requestItems = requestItems;
            this.size = size;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView textView_label;
            public TextView textViewNewsCreateDate;
            public RadioGroup radioGroup;
            CheckBoxTextRoboto checkbox_select_multiple_menu;
            //AdapterInterface listener;

            public ViewHolder(View itemView) {
                super(itemView);
                textView_label = (TextView) itemView.findViewById(R.id.textview_label);
                checkbox_select_multiple_menu = (CheckBoxTextRoboto) itemView.findViewById(R.id.checkbox_select_multiple_menu);
                radioGroup =(RadioGroup)itemView.findViewById(R.id.radio_grp_select);
                radioButton = new RadioButton(context);
                //checkBox = new CheckBox(context);
            }
            @Override
            public void onClick(View v) {
            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customise_menu_popup, parent, false);
            RadioGroup radioGroup = new RadioGroup(context);
            radioGroup.setOrientation(RadioGroup.VERTICAL);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CustomExtraMenuAdapter.ViewHolder holder, final int position) {
            final DishCustomExtrasModel dishExtrasModel = requestItems;
            //get(position);
            //System.out.println("******onBindViewHolder******** : "+homeRecyclerView.getTitle());
            //holder.textView_label.setText(dishExtrasModel.g());
            //holder.textView_label.setTag(position);

            //show view for all 3 types
            //1. Extra if available
            //2. Custom
            //3.Quantity
            //TODO : FOR 'CUSTOM'



                        //create radio button
                        /*radioButton = new RadioButton(context);
                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(context, null);
                        params.setMargins(10, 20, 20, 5);
                        radioButton.setLayoutParams(params);
                        radioButton.setText(requestItems.get(i).getCustom().get(i).getDish_extra_name());
                        radioButton.setTextSize(14);
                        radioButton.setPadding(5,0,7,0);
                        radioButton.setButtonDrawable(getResources().getDrawable(R.drawable.selector_role_radio_button_bacground));
                        radioButton.setTextColor(ContextCompat.getColorStateList(context, R.color.white));
                        radioButton.setId(Integer.parseInt(requestItems.get(i).getCustom().get(i).getDish_extra_id()));
                        holder.radioGroup.addView(radioButton);*/



           // }




            //TODO : Extras will be in two types
            // 1 : Single choice
            /*if(requestItems.get(position).getDish_extra_selection_type().equals(Constants.single_choice)){

                holder.radioGroup.setVisibility(View.VISIBLE);
                holder.checkbox_select_multiple_menu.setVisibility(View.GONE);
               for(int i=0 ; i< requestItems.size() ; i++){

                    radioButton = new RadioButton(context);
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(context, null);
                    params.setMargins(10, 20, 20, 5);
                    radioButton.setLayoutParams(params);
                    radioButton.setText(requestItems.get(i).getDish_extra_name());
                    radioButton.setTextSize(14);
                    radioButton.setPadding(5,0,7,0);
                    radioButton.setButtonDrawable(getResources().getDrawable(R.drawable.selector_role_radio_button_bacground));
                    radioButton.setTextColor(ContextCompat.getColorStateList(context, R.color.white));
                    radioButton.setId(Integer.parseInt(requestItems.get(i).getDish_extra_id()));
                    holder.radioGroup.addView(radioButton);
                }

                holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        String selectedRadioButtonId = i + "";
                   *//* for(RoleSignUpItem roleSignUpItem: arraylistroleNames){
                        if(roleSignUpItem.getId().equals(selectedId)) {
                            //found it!
                            spinner_role_signup.setText(roleSignUpItem.getRoleName());
                        }
                    }*//*
                    }
                });

            }else{
                //TODO : 2 - Multi choice
                //call adapter of multi choice
                holder.radioGroup.setVisibility(View.GONE);
                holder.checkbox_select_multiple_menu.setVisibility(View.VISIBLE);
                holder.checkbox_select_multiple_menu.setText(getActivity().getResources().getString(R.string.Rs)+" "+dishExtrasModel.getDish_extra_price());

                holder.checkbox_select_multiple_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       *//* if(dishExtrasModel.getDish_extra_is_selected().equalsIgnoreCase("0")){
                            holder.checkbox_select_multiple_menu.setChecked(true);
                            dishExtrasModel.setDish_extra_is_selected("1");
                        }else if(dishExtrasModel.getDish_extra_is_selected().equalsIgnoreCase("1")){
                            holder.checkbox_select_multiple_menu.setChecked(false);
                            dishExtrasModel.setDish_extra_is_selected("0");
                        }*//*

                    }
                });
//                holder.checkbox_select_multiple_menu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    }
//                });
            }*/
        }
        @Override
        public int getItemCount() {
            return size;
        }
    }


    //open new co-ordinator layout
    public void showCoordinatorLayout_with_custome_menu_if_available(final int position, final String dish_id, final String selected_quantity){

        // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.show();

        Button button_add_to_card = (Button)view.findViewById(R.id.button_add_to_card);
        TextView textview_cancel = (TextView) view.findViewById(R.id.textview_cancel);
        RecyclerView recyclerView_list_popup = (RecyclerView)view.findViewById(R.id.recyclerView_list_popup);
        ImageView list_item_veg_menu_list_imageview_plus = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_plus);
        ImageView list_item_veg_menu_list_imageview_minus = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_minus);
        final TextView list_item_veg_menu_list_textview_quantity = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_quantity);

        int size = upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getCustom().size() +
                upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getExtra().size() +
                upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel().getQuantity().size();
        customExtraMenuAdapter = new CustomExtraMenuAdapter(context, upcomingYourBookingModelArrayList_new.get(position).getDishCustomExtrasModel(),size);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView_list_popup.setLayoutManager(linearLayoutManager);
        //fragment_recent_jobs_recycler_view.addItemDecoration(new MyDividerItemDecoration(activity, LinearLayoutManager.VERTICAL, 8));
        recyclerView_list_popup.setAdapter(customExtraMenuAdapter);


        // if already present in cart
        final int[] quantityCount = {0};
        quantityArray = new String[upcomingYourBookingModelArrayList_new.size()];

        if(Integer.parseInt(selected_quantity) > 1 ){

            quantityCount[0] = quantityCount[0] + Integer.parseInt(selected_quantity);
            totalQuantityCount = Integer.parseInt(selected_quantity);

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
               String dishes_extra_id = "";

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

                    if(Integer.parseInt(selected_quantity) > 1){
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
        postParams.put(getActivity().getResources().getString(R.string.field_table),table_no);
        // send ids by comma seperated and it is optional
        postParams.put(getActivity().getResources().getString(R.string.field_dishes_extra_id), dish_extra_id);

        APIManager.requestPostMethod(getActivity(), getResources().getString(R.string.addCart), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
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
    //add items to cart
    private void removeMenuFromCart(String dish_id, final BottomSheetDialog bottomSheetDialog) {

        ((ParentActivity)getActivity()).showProcessingDialog();

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getActivity().getResources().getString(R.string.field_cart_id),dish_id);

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
       alert11.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

}

