package com.versacomllc.audit.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "AuditManagement";

	// tableS name
	private static final String TABLE_DEFECTS = "defect";
	private static final String TABLE_CUSTOMERS = "customer";
	
	// Defect Table Columns names
	private static final String DEFECT_ID = "id";
	private static final String DEFECT_CODE = "code";
	private static final String DEFECT_CATEGORY = "category";
	private static final String DEFECT_SUBCATEGORY = "sub_category";
	private static final String DEFECT_SEVERITY = "severity";
	private static final String DEFECT_WORKTYPE = "work_type";
	private static final String DEFECT_RECORDID = "rid";

	// Defect Table Columns names
	private static final String CUSTOMER_ID = "id";
	private static final String CUSTOMER_NAME = "name";
	private static final String CUSTOMER_RECORDID = "rid";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String CREATE_TABLE_DEFECTS = "CREATE TABLE " + TABLE_DEFECTS + "("
				+ DEFECT_ID + " INTEGER PRIMARY KEY," + DEFECT_CODE + " TEXT,"
				+ DEFECT_CATEGORY + " TEXT, " + DEFECT_SUBCATEGORY + " TEXT, "
				+ DEFECT_SEVERITY + " TEXT, " + DEFECT_WORKTYPE + " TEXT,"+  DEFECT_RECORDID + " TEXT " +")";
		db.execSQL(CREATE_TABLE_DEFECTS);
		
		String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMERS + "("
				+ CUSTOMER_ID + " INTEGER PRIMARY KEY," + CUSTOMER_NAME + " TEXT "+" )";
		db.execSQL(CREATE_TABLE_CUSTOMER);
		
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEFECTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
		// Create tables again
		onCreate(db);
	}

    void addDefect(Defect defect) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(DEFECT_CODE, defect.getCode()); 
        values.put(DEFECT_CATEGORY, defect.getCategory()); 
        values.put(DEFECT_SUBCATEGORY, defect.getSubCategory()); 
        values.put(DEFECT_SEVERITY, defect.getSeverity()); 
        values.put(DEFECT_CODE, defect.getCode()); 
        values.put(DEFECT_WORKTYPE, defect.getWorkType()); 
        values.put(DEFECT_RECORDID, defect.getRid());
        // Inserting Row
        db.insert(TABLE_DEFECTS, null, values);
        db.close(); 
    }
 
     
    // Getting All Contacts
    public List<Defect> getAllDefects() {
    	
        List<Defect> defectList = new ArrayList<Defect>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DEFECTS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	
            	Defect defect = new Defect();
                defect.setId(Integer.parseInt(cursor.getString(0)));
                defect.setCode(cursor.getString(1));
                defect.setCategory(cursor.getString(2));
                defect.setSubCategory(cursor.getString(3));
                defect.setSeverity(cursor.getString(4));
                defect.setWorkType(cursor.getString(5));
                defect.setRid(cursor.getString(6));
                defectList.add(defect);
            } while (cursor.moveToNext());
        }
 
        return defectList;
    }
    

    void addCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_ID, customer.getId()); 
        values.put(CUSTOMER_NAME, customer.getName()); 
        values.put(CUSTOMER_RECORDID, customer.getRid()); 
        // Inserting Row
        db.insert(TABLE_CUSTOMERS, null, values);
        db.close(); 
    }
    
    public List<Customer> getAllCustomers() {
    	
        List<Customer> customerList = new ArrayList<Customer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMERS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	
            	Customer customer = new Customer();
                customer.setId(Integer.parseInt(cursor.getString(0)));
                customer.setName(cursor.getString(1));
                customer.setRid(cursor.getString(2));
                customerList.add(customer);
                
            } while (cursor.moveToNext());
        }

        return customerList;
    }
    
}
