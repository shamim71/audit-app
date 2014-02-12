package com.versacomllc.audit.dao.impl;

import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.versacomllc.audit.dao.EmployeeDao;
import com.versacomllc.audit.data.Employee;


public class EmployeeDaoImpl implements EmployeeDao {

	private SQLiteOpenHelper helper;

	public EmployeeDaoImpl(SQLiteOpenHelper helper) {
		super();
		this.helper = helper;
	}

	@Override
	public List<Employee> getAllEmployees() {

		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<Employee> employees = loadAllEmployees(cursor);
		cursor.close();
		db.close();

		return employees;

	}

	@Override
	public void addEmployeeList(List<Employee> employees) {
		SQLiteDatabase db = helper.getWritableDatabase();
		for (Employee emp : employees) {
			Employee existing = getEmployeeByEmail(db, emp.getEmail());
			emp.setqBaseRef(emp.getId());
			ContentValues values = createContentValues(emp);
			if (existing == null) {
				db.insert(TABLE_NAME, null, values);
			} else {
				int rowEffected = db.update(TABLE_NAME, values, RID + " = ?",
						new String[] { String.valueOf(emp.getqBaseRef()) });
				Log.d(LOG_TAG, "Record updated: " + rowEffected);
			}
		}

		db.close();

	}

	public Employee getEmployeeByEmail(String email) {

		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where " + EMAIL
				+ "='" + email + "'";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		List<Employee> list = loadAllEmployees(cursor);
		cursor.close();
		db.close();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Employee getEmployeeByEmail(SQLiteDatabase db, String email) {

		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where " + EMAIL
				+ "='" + email + "'";

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<Employee> list = loadAllEmployees(cursor);
		cursor.close();

		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private ContentValues createContentValues(Employee emp) {

		ContentValues values = new ContentValues();
		values.put(FIRST_NAME, emp.getFirstName());
		values.put(LAST_NAME, emp.getLastName());
		values.put(EMAIL, emp.getEmail());
		values.put(PASSWORD, emp.getPassword());
		values.put(RID, emp.getqBaseRef());

		return values;
	}

	private List<Employee> loadAllEmployees(Cursor cursor) {
		List<Employee> list = new ArrayList<Employee>();
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				int index = 0;
				Employee emp = new Employee();
				emp.setLocalId(cursor.getLong(index++));
				emp.setFirstName(cursor.getString(index++));
				emp.setLastName(cursor.getString(index++));
				emp.setEmail(cursor.getString(index++));
				emp.setPassword(cursor.getString(index++));
				emp.setqBaseRef(cursor.getString(index++));
				list.add(emp);

			} while (cursor.moveToNext());
		}

		return list;
	}

}
