package com.versacomllc.audit.model;

import java.util.List;

public class InternalAudit {

	protected String rid;
	
	protected String auditType;
	
	protected String auditDate;
	
	protected String auditHour;

	protected String auditStatus;
	
	protected String auditedBy;
	
	protected String auditedByEmployee;
	
	protected String supervisedBy;

	protected String supervisedByEmployee;
	
	protected String managedBy;

	protected String managedByEmployee;
	
	protected String customer;
	
	protected String customerName;
	
	protected String project;
	
	protected String projectName;
	
	protected String siteId;
	
	protected String city;
	
	protected String state;
	
	protected String zip;
	
	protected String auditResult;
	
	private List<ScopeOfWork> scopeOfWorks;
	
	
	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditHour() {
		return auditHour;
	}

	public void setAuditHour(String auditHour) {
		this.auditHour = auditHour;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditedBy() {
		return auditedBy;
	}

	public void setAuditedBy(String auditedBy) {
		this.auditedBy = auditedBy;
	}

	public String getAuditedByEmployee() {
		return auditedByEmployee;
	}

	public void setAuditedByEmployee(String auditedByEmployee) {
		this.auditedByEmployee = auditedByEmployee;
	}

	public String getSupervisedBy() {
		return supervisedBy;
	}

	public void setSupervisedBy(String supervisedBy) {
		this.supervisedBy = supervisedBy;
	}

	public String getSupervisedByEmployee() {
		return supervisedByEmployee;
	}

	public void setSupervisedByEmployee(String supervisedByEmployee) {
		this.supervisedByEmployee = supervisedByEmployee;
	}

	public String getManagedBy() {
		return managedBy;
	}

	public void setManagedBy(String managedBy) {
		this.managedBy = managedBy;
	}

	public String getManagedByEmployee() {
		return managedByEmployee;
	}

	public void setManagedByEmployee(String managedByEmployee) {
		this.managedByEmployee = managedByEmployee;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}



	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}




	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<ScopeOfWork> getScopeOfWorks() {
		return scopeOfWorks;
	}

	public void setScopeOfWorks(List<ScopeOfWork> scopeOfWorks) {
		this.scopeOfWorks = scopeOfWorks;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}


	
}
