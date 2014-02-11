package com.versacomllc.audit.data;

import java.util.List;

public interface AuditDao {

	public static final String TABLE_INTERNAL_AUDITS = "internal_audit";

	public static final String ID = "id";
	public static final String RID = "rid";
	
	// INTERNAL AUDIT Table Columns names
	public static final String AUDIT_TYPE = "type";
	public static final String AUDIT_STATUS = "status";
	public static final String AUDIT_DATE = "audit_date";
	public static final String AUDIT_HOUR = "audit_hour";
	public static final String AUDIT_BY = "audited_by";
	public static final String AUDIT_BY_EMPLOYEE = "audited_by_employee";
	public static final String AUDIT_SITE_ID = "site_id";
	public static final String AUDIT_CUSTOMER = "customer_id";
	public static final String AUDIT_CUSTOMER_NAME = "customer_NAME";
	
	// Create audit table
	public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE " + TABLE_INTERNAL_AUDITS
			+ "(" + ID + " INTEGER PRIMARY KEY," + AUDIT_TYPE + " TEXT,"
			+ AUDIT_STATUS + " TEXT, " + AUDIT_DATE + " INTEGER, "
			+ AUDIT_HOUR + " TEXT, " + AUDIT_BY + " TEXT,"
			+ AUDIT_BY_EMPLOYEE + " TEXT," + AUDIT_SITE_ID + " TEXT,"
			+ AUDIT_CUSTOMER + " TEXT," + AUDIT_CUSTOMER_NAME + " TEXT,"
			+ RID + " TEXT " + ")";
	
	public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS " + TABLE_INTERNAL_AUDITS;
	
	long addInternalAudit(LocalAudit audit);
	
	void updateInternalAudit(LocalAudit audit);
	
	List<LocalAudit> getAllInternalAudits();
	
	LocalAudit getInternalAuditsById(final String id);
	
	List<LocalAudit> getAllInternalAuditsByEmployee(final String userId);
	
}
