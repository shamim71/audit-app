package com.versacomllc.audit.data;

import java.util.ArrayList;
import java.util.List;

import com.versacomllc.audit.model.AuditDefect;
import com.versacomllc.audit.model.ScopeOfWork;
import com.versacomllc.audit.model.ScopeOfWorkTechnician;

public class LocalScopeOfWork extends ScopeOfWork{

	private long id;

	private int sync;
	
	private List<LocalScopeOfWorkTech> lWorkTechs;
	
	private List<LocalAuditDefect> lAuditDefects;
	
	
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
		work.setWorkType(workType);
		
		if(this.lWorkTechs != null){
			work.setTechnicians(new ArrayList<ScopeOfWorkTechnician>());
			for(LocalScopeOfWorkTech t: this.lWorkTechs){
				work.getTechnicians().add(t.toScopeOfWorkTechnician());
			}
		}
		
		if(this.lAuditDefects != null && this.lAuditDefects.size()>0){
			work.setAuditDefects(new ArrayList<AuditDefect>());
			for(LocalAuditDefect w: this.lAuditDefects){
				work.getAuditDefects().add(w.toAuditDefect());
			}
		}
		
		return work;
	}

	public List<LocalScopeOfWorkTech> getlWorkTechs() {
		return lWorkTechs;
	}

	public void setlWorkTechs(List<LocalScopeOfWorkTech> lWorkTechs) {
		this.lWorkTechs = lWorkTechs;
	}

	public List<LocalAuditDefect> getlAuditDefects() {
		return lAuditDefects;
	}

	public void setlAuditDefects(List<LocalAuditDefect> lAuditDefects) {
		this.lAuditDefects = lAuditDefects;
	}

	@Override
	public String toString() {
		return "LocalScopeOfWork [id=" + id + ", sync=" + sync
				+ ", lWorkTechs=" + lWorkTechs + ", lAuditDefects="
				+ lAuditDefects + ", workType=" + workType + ", dateOfWork="
				+ dateOfWork + ", rid=" + rid + ", auditId=" + auditId
				+ ", technicians=" + technicians + ", auditDefects="
				+ auditDefects + "]";
	}


	
	
}
