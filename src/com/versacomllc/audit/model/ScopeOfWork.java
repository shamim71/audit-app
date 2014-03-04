package com.versacomllc.audit.model;

import java.util.List;

public class ScopeOfWork {
	
	protected String workType;
		
	protected String dateOfWork;
	
	protected String rid;
	
	protected long auditId;

	protected List<ScopeOfWorkTechnician> technicians;
	
	protected List<AuditDefect> auditDefects;
	
	
	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
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

	public List<ScopeOfWorkTechnician> getTechnicians() {
		return technicians;
	}

	public void setTechnicians(List<ScopeOfWorkTechnician> technicians) {
		this.technicians = technicians;
	}

	public List<AuditDefect> getAuditDefects() {
		return auditDefects;
	}

	public void setAuditDefects(List<AuditDefect> auditDefects) {
		this.auditDefects = auditDefects;
	}

	


	
}
