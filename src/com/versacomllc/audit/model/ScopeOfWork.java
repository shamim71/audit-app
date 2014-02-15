package com.versacomllc.audit.model;

public class ScopeOfWork {
	
	protected String workType;
	
	protected String techName;
	
	protected String techId;
	
	protected String dateOfWork;
	
	protected String rid;
	
	protected long auditId;

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getTechName() {
		return techName;
	}

	public void setTechName(String techName) {
		this.techName = techName;
	}

	public String getTechId() {
		return techId;
	}

	public void setTechId(String techId) {
		this.techId = techId;
	}

	public String getDateOfWork() {
		return dateOfWork;
	}

	public void setDateOfWork(String dateOfWork) {
		this.dateOfWork = dateOfWork;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public long getAuditId() {
		return auditId;
	}

	public void setAuditId(long auditId) {
		this.auditId = auditId;
	}

	


	
}
