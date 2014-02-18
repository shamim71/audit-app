package com.versacomllc.audit.data;

import com.versacomllc.audit.model.ScopeOfWork;

public class LocalScopeOfWork extends ScopeOfWork{

	private long id;

	private int sync;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSync() {
		return sync;
	}

	public void setSync(int sync) {
		this.sync = sync;
	}
	public ScopeOfWork toScopeofWorks(){
		ScopeOfWork work = new ScopeOfWork();
		work.setAuditId(auditId);
		work.setDateOfWork(dateOfWork);
		work.setRid(rid);
		work.setTechId(techId);
		work.setTechName(techName);
		work.setWorkType(workType);
		return work;
	}

	@Override
	public String toString() {
		return "LocalScopeOfWork [id=" + id + ", sync=" + sync + ", workType="
				+ workType + ", techName=" + techName + ", techId=" + techId
				+ ", dateOfWork=" + dateOfWork + ", rid=" + rid + ", auditId="
				+ auditId + "]";
	}
	
	
}
