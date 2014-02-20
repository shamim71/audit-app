package com.versacomllc.audit.dao;

import java.util.List;

import com.versacomllc.audit.data.Employee;

public interface EmployeeDao {
	public static final String TABLE_NAME = "employee";

	public static final String ID = "id";
	public static final String RID = "rid";

	// INTERNAL AUDIT Table Columns names
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";

	// Create audit table
	public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE "
			+ TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + FIRST_NAME
			+ " TEXT," + LAST_NAME + " TEXT, " + EMAIL + " TEXT, " + PASSWORD
			+ " TEXT, " + RID + " TEXT " + ")";

	public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;



	List<Employee> getAllEmployees();
	
	void addEmployeeList(List<Employee> employees);


	Employee findEmployeeByEmail(final String email);
	
	int update(Employee employee);
}
