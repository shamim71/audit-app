package com.versacomllc.audit.data;

import com.versacomllc.audit.model.InternalAudit;

public class LocalAudit extends InternalAudit{

	private int id;

	private String customerName;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalAudit() {
		super();
	}
	public LocalAudit(InternalAudit audit) {
		this.rid = audit.getRid();
		this.auditDate = audit.getAuditDate();
		this.auditedBy = audit.getAuditedBy();
		this.auditedByEmployee = audit.getAuditedByEmployee();
		this.auditHour = audit.getAuditHour();
		this.auditStatus = audit.getAuditStatus();
		this.auditType = audit.getAuditType();
		this.customer = audit.getCustomer();
		this.siteId = audit.getSiteId();
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	
}
