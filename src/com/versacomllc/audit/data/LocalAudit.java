package com.versacomllc.audit.data;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.versacomllc.audit.model.InternalAudit;
import com.versacomllc.audit.model.ScopeOfWork;

public class LocalAudit extends InternalAudit{

	private long id;
	
	private int syn;
	
	private List<LocalScopeOfWork> works;
	

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalAudit() {

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
		this.customerName = audit.getCustomerName();
		this.siteId = audit.getSiteId();
		this.city = audit.getCity();
		this.state = audit.getState();
		this.project = audit.getProject();
		this.projectName = audit.getProjectName();
		this.syn = 1;
		if(TextUtils.isEmpty(audit.getRid())){
			this.syn = 0;
		}
	}

	public InternalAudit toInternalAudit(){
		
		InternalAudit iAudit = new InternalAudit();
		iAudit.setAuditDate(this.auditDate);
		iAudit.setAuditedBy(this.auditedBy);
		iAudit.setAuditedByEmployee(this.auditedByEmployee);
		iAudit.setAuditHour(this.auditHour);
		iAudit.setAuditStatus(this.auditStatus);
		iAudit.setAuditType(this.auditType);
		iAudit.setCustomer(this.customer);
		iAudit.setCustomerName(this.customerName);
		iAudit.setProject(this.project);
		iAudit.setProjectName(this.projectName);
		iAudit.setRid(this.rid);
		iAudit.setSiteId(this.siteId);
		iAudit.setCity(this.getCity());
		iAudit.setState(this.getState());
		
		if(this.works != null && this.works.size()>0){
			iAudit.setScopeOfWorks(new ArrayList<ScopeOfWork>());
			for(LocalScopeOfWork w: this.works){
				iAudit.getScopeOfWorks().add(w.toScopeofWorks());
			}
		}
		

		return iAudit;
	}

	public int getSyn() {
		return syn;
	}

	public void setSyn(int syn) {
		this.syn = syn;
	}

	public List<LocalScopeOfWork> getWorks() {
		return works;
	}

	public void setWorks(List<LocalScopeOfWork> works) {
		this.works = works;
	}



	
}
