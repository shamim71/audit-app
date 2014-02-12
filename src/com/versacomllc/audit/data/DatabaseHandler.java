package com.versacomllc.audit.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.versacomllc.audit.dao.AuditDao;
import com.versacomllc.audit.dao.DefectDao;
import com.versacomllc.audit.dao.EmployeeDao;
import com.versacomllc.audit.dao.ScopeOfWorkDao;
import com.versacomllc.audit.dao.SiteWorkTypeDao;
import com.versacomllc.audit.dao.impl.AuditDaoImpl;
import com.versacomllc.audit.dao.impl.DefectDaoImpl;
import com.versacomllc.audit.dao.impl.EmployeeDaoImpl;
import com.versacomllc.audit.dao.impl.ScopeOfWorkDaoImpl;
import com.versacomllc.audit.dao.impl.SiteWorkTypeDaoImpl;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "AuditManagement";

	// tableS name
	private static final String TABLE_DEFECTS = "defect";
	private static final String TABLE_CUSTOMERS = "customer";


	private static final String ID = "id";
	private static final String RID = "rid";


	// CUSTOMER Table Columns names
	private static final String CUSTOMER_NAME = "name";


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {


		String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMERS + "("
				+ ID + " INTEGER PRIMARY KEY," + CUSTOMER_NAME + " TEXT," + RID
				+ " TEXT " + ")";
		db.execSQL(CREATE_TABLE_CUSTOMER);

		// Create audit table

		db.execSQL(AuditDao.CREATE_TABLE_SCRIPT);
		db.execSQL(SiteWorkTypeDao.CREATE_TABLE_SCRIPT);
		db.execSQL(EmployeeDao.CREATE_TABLE_SCRIPT);
		db.execSQL(ScopeOfWorkDao.CREATE_TABLE_SCRIPT);
		db.execSQL(DefectDao.CREATE_TABLE_SCRIPT);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEFECTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
		db.execSQL(AuditDao.DROP_TABLE_SCRIPT);
		db.execSQL(SiteWorkTypeDao.DROP_TABLE_SCRIPT);
		db.execSQL(EmployeeDao.DROP_TABLE_SCRIPT);
		db.execSQL(ScopeOfWorkDao.DROP_TABLE_SCRIPT);
		db.execSQL(DefectDao.DROP_TABLE_SCRIPT);
		// Create tables again
		onCreate(db);
	}



	public void addCustomer(LocalCustomer customer) {
		SQLiteDatabase db = this.getWritableDatabase();

		boolean exist = isCustomerExist(db, customer.getRid());
		if(!exist){
			ContentValues values = new ContentValues();
			/* values.put(CUSTOMER_ID, customer.getId()); */
			values.put(CUSTOMER_NAME, customer.getName());
			values.put(RID, customer.getRid());
			// Inserting Row
			db.insert(TABLE_CUSTOMERS, null, values);
		}

		db.close();
	}

	public boolean isCustomerExist(SQLiteDatabase db, String rid) {

		String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMERS + " where " + RID
				+ "='" + rid + "'";
		boolean found = false;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			found = true;
		}
		cursor.close();

		return found;
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
		cursor.close();
		db.close();
		return customerList;

	}

	public AuditDao getAuditDao(){
		return new AuditDaoImpl(this.getWritableDatabase());
	}
	
	SiteWorkTypeDao siteWorkTypeDao;
	public SiteWorkTypeDao getSiteWorkDao(){
		if(siteWorkTypeDao == null){
			siteWorkTypeDao = new SiteWorkTypeDaoImpl(this);
		}
		return siteWorkTypeDao;
	}
	
	EmployeeDao employeeDao;
	public EmployeeDao getEmployeeDao(){
		if(employeeDao == null){
			employeeDao = new EmployeeDaoImpl(this);
		}
		return employeeDao;
	}

	ScopeOfWorkDao scopeOfWorkDao;
	public ScopeOfWorkDao getScopeOfWorkDao() {
		if(scopeOfWorkDao == null){
			scopeOfWorkDao = new ScopeOfWorkDaoImpl(this);
		}
		return scopeOfWorkDao;
	}
	
	DefectDao defectDao;
	public DefectDao getDefectDao() {
		if(defectDao == null){
			defectDao = new DefectDaoImpl(this);
		}
		return defectDao;
	}

	


}
