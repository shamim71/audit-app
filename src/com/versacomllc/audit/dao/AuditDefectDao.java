package com.versacomllc.audit.dao;

import java.util.List;

import com.versacomllc.audit.data.LocalAuditDefect;

public interface AuditDefectDao {

	public static final String TABLE_NAME = "audit_defect";

	public static final String ID = "local_id";
	public static final String AUDIT_ID = "a_id";
	public static final String SOW_ID = "s_id";
	public static final String DEFECT_RID = "d_rid";


	public static final String DEFECT_COUNT = "d_count";
	public static final String DEFECT_NOTE = "d_note";
	public static final String CODE = "d_code";
	public static final String DESCRIPTION = "d_description";
	public static final String SEVERITY = "severity";
	public static final String PIC_BEFORE = "pic_b";
	public static final String PIC_AFTER = "pic_a";
	public static final String RID = "rid";
	public static final String FIXED = "fixed";
	public static final String SYNC = "sync";

	// Create audit table
	public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE "
			+ TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + AUDIT_ID
			+ " INTEGER," + SOW_ID	+ " INTEGER," + DEFECT_RID + " TEXT, " +  DEFECT_COUNT + " INTEGER," + DEFECT_NOTE
			+ " TEXT, " + CODE + " TEXT, " + DESCRIPTION + " TEXT, " + SEVERITY
			+ " TEXT, " + PIC_BEFORE + " TEXT, " + PIC_AFTER + " TEXT, "
			+ FIXED + " TEXT, " + SYNC + " INTEGER, " + RID + " TEXT " + ")";

	public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	public List<LocalAuditDefect> getAuditDefectBySowId(final long sowId);
	
	public long addAuditDefect(LocalAuditDefect auditDefect);

	public long updateAuditDefect(LocalAuditDefect auditDefect);

	public int deleteAuditDefect(long localId);

	public LocalAuditDefect getAuditDefectByLocalId(long localId);
	
	public List<LocalAuditDefect> getPendingAuditDefectsBySowId(final long sowId);
	
	public int deleteAuditDefectBySowId(long sowId);
	
	public int deleteAuditDefectByAuditId(long auditId);
	
}
