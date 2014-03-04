package com.versacomllc.audit.model;

public class AuditDefect {

	protected String count;
	
	protected String defectId;
	
	protected String note;

	protected String rid;
	
	protected String defectCode;
	
	protected String defectSeverity;
	
	protected String defectDescription;
	
	protected String fixed;
	
	
	protected String defectPicBeforeOnServer;
	
	protected String defectPicAfteOnServer;
	
	
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getDefectId() {
		return defectId;
	}

	public void setDefectId(String defectId) {
		this.defectId = defectId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


	public String getDefectCode() {
		return defectCode;
	}

	public void setDefectCode(String defectCode) {
		this.defectCode = defectCode;
	}

	public String getDefectSeverity() {
		return defectSeverity;
	}

	public void setDefectSeverity(String defectSeverity) {
		this.defectSeverity = defectSeverity;
	}

	public String getDefectDescription() {
		return defectDescription;
	}

	public void setDefectDescription(String defectDescription) {
		this.defectDescription = defectDescription;
	}


	public String getFixed() {
		return fixed;
	}

	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getDefectPicBeforeOnServer() {
		return defectPicBeforeOnServer;
	}

	public void setDefectPicBeforeOnServer(String defectPicBeforeOnServer) {
		this.defectPicBeforeOnServer = defectPicBeforeOnServer;
	}

	public String getDefectPicAfteOnServer() {
		return defectPicAfteOnServer;
	}

	public void setDefectPicAfteOnServer(String defectPicAfteOnServer) {
		this.defectPicAfteOnServer = defectPicAfteOnServer;
	}
	
}
