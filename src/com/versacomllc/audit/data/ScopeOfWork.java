package com.versacomllc.audit.data;

public class ScopeOfWork {

	private String workType;
	
	private String techName;
	
	private String techId;
	
	private String dateOfWork;
	
	private int id;
	
	private String rid;
	
	private long auditId;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "ScopeOfWork [workType=" + workType + ", techName=" + techName
				+ ", techId=" + techId + ", dateOfWork=" + dateOfWork + ", id="
				+ id + ", rid=" + rid + ", auditId=" + auditId + "]";
	}
	
}
