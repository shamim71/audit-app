package com.versacomllc.audit.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.versacomllc.audit.model.InternalAudit;
import com.versacomllc.audit.utils.Constants;

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
	private static final String TABLE_INTERNAL_AUDITS = "internal_audit";
	
	private static final String ID = "id";
	private static final String RID = "rid";
	// Defect Table Columns names
	private static final String DEFECT_CODE = "code";
	private static final String DEFECT_CATEGORY = "category";
	private static final String DEFECT_SUBCATEGORY = "sub_category";
	private static final String DEFECT_SEVERITY = "severity";
	private static final String DEFECT_WORKTYPE = "work_type";
	private static final String DEFECT_RECORDID = "rid";

	// CUSTOMER Table Columns names
	private static final String CUSTOMER_NAME = "name";

	// INTERNAL AUDIT Table Columns names
	private static final String AUDIT_TYPE = "type";
	private static final String AUDIT_STATUS = "status";
	private static final String AUDIT_DATE = "audit_date";
	private static final String AUDIT_HOUR = "audit_hour";
	private static final String AUDIT_BY = "audited_by";
	private static final String AUDIT_SITE_ID = "site_id";
	private static final String AUDIT_CUSTOMER = "customer_id";
	private static final String AUDIT_CUSTOMER_NAME = "customer_NAME";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String CREATE_TABLE_DEFECTS = "CREATE TABLE " + TABLE_DEFECTS + "("
				+ ID + " INTEGER PRIMARY KEY," + DEFECT_CODE + " TEXT,"
				+ DEFECT_CATEGORY + " TEXT, " + DEFECT_SUBCATEGORY + " TEXT, "
				+ DEFECT_SEVERITY + " TEXT, " + DEFECT_WORKTYPE + " TEXT,"+  RID + " TEXT " +")";
		db.execSQL(CREATE_TABLE_DEFECTS);
		
		String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMERS + "("
				+ ID + " INTEGER PRIMARY KEY," + CUSTOMER_NAME + " TEXT,"+  RID + " TEXT " +")";
		db.execSQL(CREATE_TABLE_CUSTOMER);
		
		// Create audit table
		String CREATE_TABLE_AUDITS = "CREATE TABLE " + TABLE_INTERNAL_AUDITS + "("
				+ ID + " INTEGER PRIMARY KEY," + AUDIT_TYPE + " TEXT,"
				+ AUDIT_STATUS + " TEXT, " + AUDIT_DATE + " INTEGER, "
				+ AUDIT_HOUR + " TEXT, " + AUDIT_BY + " TEXT,"+ AUDIT_SITE_ID + " TEXT,"+ AUDIT_CUSTOMER + " TEXT,"+ AUDIT_CUSTOMER_NAME + " TEXT,"+ RID + " TEXT " +")";
		db.execSQL(CREATE_TABLE_AUDITS);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEFECTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERNAL_AUDITS);
		// Create tables again
		onCreate(db);
	}

   public void addDefect(Defect defect) {
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
    

   public void addCustomer(LocalCustomer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
/*        values.put(CUSTOMER_ID, customer.getId()); */
        values.put(CUSTOMER_NAME, customer.getName()); 
        values.put(RID, customer.getRid()); 
        // Inserting Row
        db.insert(TABLE_CUSTOMERS, null, values);
        db.close(); 
    }
    
    public List<LocalCustomer> getAllCustomers() {
    	
        List<LocalCustomer> customerList = new ArrayList<LocalCustomer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMERS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	LocalCustomer customer = new LocalCustomer();
                customer.setId(Integer.parseInt(cursor.getString(0)));
                customer.setName(cursor.getString(1));
                customer.setRid(cursor.getString(2));
                customerList.add(customer);
                
            } while (cursor.moveToNext());
        }

        return customerList;
    }
    public void addInternalAudit(LocalAudit audit) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        
        values.put(AUDIT_TYPE, audit.getAuditType()); 
        values.put(AUDIT_STATUS, audit.getAuditStatus()); 
        values.put(AUDIT_HOUR, audit.getAuditHour());
        values.put(AUDIT_DATE, String.valueOf(getDateAsInt(audit.getAuditDate())));
        values.put(AUDIT_BY, audit.getAuditedBy());
        values.put(AUDIT_SITE_ID, audit.getSiteId());
        values.put(AUDIT_CUSTOMER, audit.getCustomer());
        values.put(AUDIT_CUSTOMER_NAME, audit.getCustomerName());
        values.put(RID, audit.getRid()); 
        // Inserting Row
        db.insert(TABLE_INTERNAL_AUDITS, null, values);
        db.close(); 
    }
    private long getDateAsInt(String date){
    	try {
		Date dt = 	Constants.US_DATEFORMAT.parse(date);
		return dt.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
    }
    
    public List<LocalAudit> getAllInternalAudits() {
    	
        List<LocalAudit> auditList = new ArrayList<LocalAudit>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INTERNAL_AUDITS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	LocalAudit audit = new LocalAudit();
            	audit.setId(Integer.parseInt(cursor.getString(0)));
            	audit.setAuditType(cursor.getString(1));
            	audit.setAuditStatus(cursor.getString(2));
            	audit.setRid(cursor.getString(2));
                auditList.add(audit);
                
            } while (cursor.moveToNext());
        }

        return auditList;
    }
    
}
