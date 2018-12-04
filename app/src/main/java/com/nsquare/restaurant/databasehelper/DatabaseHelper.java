package com.nsquare.restaurant.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nsquare.restaurant.model.CartModel;
import com.nsquare.restaurant.model.CustomMenuParamterDetailsItem;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
	// Logcat tag
	private static final String LOG = "DatabaseHelper";
	// Database Version
	private static final int DATABASE_VERSION = 6;
	// Database Name
	private static final String DATABASE_NAME = "Restaurant";
	// Table Names
	//TODO : Table-1 menuList
	private static final String TABLE_CART= "cart";
	private static final String TABLE_CUSTOMIZATION= "menu_customization";
	//Table-2 : As per order Id
	private static final String TABLE_ORER= "order_menu";

	//Table-1 columnnames
	private static final String database_unique_id = "database_unique_id";
	private static final String database_order_menus_id = "database_order_menus_id";
	private static final String database_user_id = "database_user_id";
	private static final String database_menu_id = "database_menu_id";
	private static final String database_menu_name = "database_menu_name";
	private static final String database_menu_price = "database_menu_price";
	private static final String database_menu_image = "database_menu_image";
	private static final String database_menu_quantity = "database_menu_quantity";
	private static final String database_menu_status = "database_menu_status";
	private static final String database_menu_is_customised = "database_menu_is_customised";
	private static final String database_is_menu_custom = "database_is_menu_custom";
	private static final String database_menu_is = "database_menu_is";

	// Table-2 columnnames : OREDR MENU
	private static final String getDatabase_order_id = "getDatabase_order_id";
	private static final String getDatabase_fk_order_id = "getDatabase_fk_order_id";
	private static final String getDatabase_order_menu_custom_title_id = "getDatabase_order_menu_custom_title_id";
	private static final String getDatabase_order_menu_custom_title = "getDatabase_order_menu_custom_title";
	private static final String getDatabase_order_menu_custom_title_values_id = "getDatabase_order_menu_custom_title_values_id";
	private static final String getDatabase_order_menu_custom_title_values = "getDatabase_order_menu_custom_title_values";
	private static final String getDatabase_order_menu_custom_title_prices = "getDatabase_order_menu_custom_title_prices";
	private static final String dish_extra_id = "dish_extra_id";
	private static final String dish_extra_name = "dish_extra_name";
	private static final String dish_extra_price = "dish_extra_price";
	private static final String dish_extra_is_chargeable = "dish_extra_is_chargeable";
	private static final String dish_extra_selection_type = "dish_extra_selection_type";
	private static final String dish_extra_is_selected = "dish_extra_is_selected";

	// Table-1 Create Statements
	// Todo:  table create statement
	private static final String CREATE_TABLE_TODO = "CREATE TABLE "
			+ TABLE_CART + "("
			+ database_unique_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ database_order_menus_id + " INTEGER,"
			+ database_user_id + " INTEGER,"
			+ database_menu_id + " INTEGER,"
			+ database_menu_name+ " TEXT,"
			+ database_menu_price+ " TEXT,"
			+ database_menu_image+ " TEXT,"
			+ database_menu_quantity+ " TEXT,"
			+ database_menu_status+ " TEXT,"
			+ database_menu_is_customised+ " TEXT"+
			")";


	// Table-1 Create Customization
	// Todo:  table create statement
	private static final String CREATE_TABLE_CUSTOMIZATION = "CREATE TABLE "
			+ TABLE_CUSTOMIZATION + "("
			+ database_unique_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ database_user_id + " INTEGER,"
			+ database_menu_id + " INTEGER,"
			+ dish_extra_id+ " TEXT,"
			+ dish_extra_name+ " TEXT,"
			+ dish_extra_price+ " TEXT,"
			+ dish_extra_is_chargeable+ " TEXT,"
			+ dish_extra_selection_type+ " TEXT,"
			+ dish_extra_is_selected+ " TEXT"+
			")";
//
//	//Table-2 : oredr menu with custom
//	private static final String CREATE_TABLE_ORDER_MENU = "create table "
//			+ TABLE_ORER + " ("
//			+ getDatabase_order_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//			+ database_user_id + " INTEGER, "
//			+ getDatabase_order_menu_custom_title_id + " TEXT,"
//			+ getDatabase_order_menu_custom_title + " TEXT,"
//			+ getDatabase_order_menu_custom_title_values_id + " TEXT,"
//			+ getDatabase_order_menu_custom_title_values + " TEXT,"
//			+ getDatabase_order_menu_custom_title_prices + " TEXT,"
//			+ " FOREIGN KEY ("+getDatabase_fk_order_id+") REFERENCES "+TABLE_CART+"("+database_unique_id+"));";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_TABLE_TODO);
		db.execSQL(CREATE_TABLE_CUSTOMIZATION);
//		db.execSQL(CREATE_TABLE_ORDER_MENU);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMIZATION);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORER);
		onCreate(db);
	}
	//Add into table-1
	public long addInTo(CartModel cartModel) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
//		values.put(KEY_UNIQUE_ID, cartItemSqlite.getKEY_UNIQUE_ID());
		values.put(database_user_id, cartModel.getDatabase_user_id());
		values.put(database_order_menus_id, cartModel.getDatabase_order_menus_id());
		values.put(database_menu_id, cartModel.getDatabase_menu_id());
		values.put(database_menu_name, cartModel.getDatabase_menu_name());
		values.put(database_menu_price, cartModel.getDatabase_menu_price());
		values.put(database_menu_image, cartModel.getDatabase_menu_image());
		values.put(database_menu_quantity, cartModel.getDatabase_menu_quantity());
		values.put(database_menu_status, cartModel.getDatabase_menu_status());
		// insert row
		long tag_id = db.insert(TABLE_CART, null, values);
		return tag_id;
	}

	//Add into table-2
	public long addInToOrderMenu(CustomMenuParamterDetailsItem customMenuParamterDetailsItem) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(database_user_id, customMenuParamterDetailsItem.getId());
		values.put(getDatabase_order_menu_custom_title, customMenuParamterDetailsItem.getSelectedLabel());
		values.put(getDatabase_order_menu_custom_title_values, customMenuParamterDetailsItem.getPrice());
		values.put(getDatabase_order_menu_custom_title_prices, customMenuParamterDetailsItem.getName());
		// insert row
		long tag_id = db.insert(TABLE_ORER, null, values);
		return tag_id;
	}

	public ArrayList<CartModel> getCartItemsByUserId(String userId)
	{
		ArrayList<CartModel> tags = new ArrayList<CartModel>();
//		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION+" where "+Constants.user_id+"="+userId;
		String selectQuery = "SELECT  * FROM " + TABLE_CART+" where "+database_user_id+"="+userId;

		Log.e(LOG, selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				CartModel t = new CartModel();
				t.setDatabase_user_id(c.getString((c.getColumnIndex(database_user_id))));
				t.setDatabase_order_menus_id(c.getString((c.getColumnIndex(database_order_menus_id))));
				t.setDatabase_menu_id(c.getString((c.getColumnIndex(database_menu_id))));
				t.setDatabase_menu_name(c.getString((c.getColumnIndex(database_menu_name))));
				t.setDatabase_menu_price(c.getString((c.getColumnIndex(database_menu_price))));
				t.setDatabase_menu_image(c.getString((c.getColumnIndex(database_menu_image))));
				t.setDatabase_menu_quantity(c.getString((c.getColumnIndex(database_menu_quantity))));
				t.setDatabase_menu_status(c.getString((c.getColumnIndex(database_menu_status))));
				// adding to tags list
				tags.add(t);
			} while (c.moveToNext());
		}
		return tags;
	}
	//from table-1
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CART,null,null);
	}


	//To delete specific item Table-1
	public void deleteItem(int menuId, String userId) {

		System.out.println("id*****"+menuId);
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CART, database_menu_id+ " = ? AND " + database_user_id + " = ?",
				new String[]{String.valueOf(menuId), userId });
	}
	//Table-1
	public void updateRecordById(int menuId, String userId, CartModel cartModel) {

		System.out.println("id*****"+menuId);

		ContentValues values = new ContentValues();
		values.put(database_user_id, cartModel.getDatabase_user_id());
		values.put(database_order_menus_id, cartModel.getDatabase_order_menus_id());
		values.put(database_menu_id, cartModel.getDatabase_menu_id());
		values.put(database_menu_name, cartModel.getDatabase_menu_name());
		values.put(database_menu_price, cartModel.getDatabase_menu_price());
		values.put(database_menu_image, cartModel.getDatabase_menu_image());
		values.put(database_menu_quantity, cartModel.getDatabase_menu_quantity());
		values.put(database_menu_status, cartModel.getDatabase_menu_status());

		SQLiteDatabase db = this.getWritableDatabase();

		db.update(TABLE_CART, values, database_menu_id + " = ? AND " + database_user_id + " = ?", new String[]{String.valueOf(menuId), userId});
	}

	public int getCountOfMenuById(int menuId, String userId) {
		String countQuery = "SELECT  * FROM " + TABLE_CART + " where database_menu_id="+menuId+" and database_user_id="+userId;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public int getCartCount(int flag) {
		String countQuery = "";
		if(flag == 0){
			countQuery = "SELECT  * FROM " + TABLE_CART+ " where "+ database_menu_status + "=0";
		}else{
			countQuery = "SELECT  * FROM " + TABLE_CART;
		}

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public ArrayList<CartModel> getAllCartItem(int flag)
	{
		ArrayList<CartModel> tags = new ArrayList<CartModel>();
		String selectQuery = "";

		if (flag == 0) {
			selectQuery = "SELECT  * FROM " + TABLE_CART;
		}else{
			selectQuery = "SELECT  * FROM " + TABLE_CART + " where "+ database_menu_status + "=0";
		}
		Log.e(LOG, selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				CartModel t = new CartModel();
				t.setDatabase_user_id(c.getString((c.getColumnIndex(database_user_id))));
				t.setDatabase_order_menus_id(c.getString((c.getColumnIndex(database_order_menus_id))));
				t.setDatabase_menu_id(c.getString((c.getColumnIndex(database_menu_id))));
				t.setDatabase_menu_name(c.getString((c.getColumnIndex(database_menu_name))));
				t.setDatabase_menu_price(c.getString((c.getColumnIndex(database_menu_price))));
				t.setDatabase_menu_image(c.getString((c.getColumnIndex(database_menu_image))));
				t.setDatabase_menu_quantity(c.getString((c.getColumnIndex(database_menu_quantity))));
				t.setDatabase_menu_status(c.getString((c.getColumnIndex(database_menu_status))));

				// adding to tags list
				tags.add(t);
			} while (c.moveToNext());
		}
		return tags;
	}
}
