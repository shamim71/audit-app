package com.versacomllc.audit.data;

import com.versacomllc.audit.model.AuditDefect;

public class LocalAuditDefect extends AuditDefect{

	private long localId;

	private String auditId;
	
	private String techName;
	
	private String defectPicBefore;
	
	private String defectPicAfter;
	
	private int sync;
	
	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getTechName() {
		return techName;
	}

	public void setTechName(String techName) {
		this.techName = techName;
	}

	public String getDefectPicBefore() {
		return defectPicBefore;
	}

	public void setDefectPicBefore(String defectPicBefore) {
		this.defectPicBefore = defectPicBefore;
	}

	public String getDefectPicAfter() {
		return defectPicAfter;
	}

	public void setDefectPicAfter(String defectPicAfter) {
		this.defectPicAfter = defectPicAfter;
	}

	public int getSync() {
		return sync;
	}

	public void setSync(int sync) {
		this.sync = sync;
	}


		
	
}
