package com.versacomllc.audit.data;

import com.versacomllc.audit.model.ScopeOfWorkTechnician;

public class LocalScopeOfWorkTech extends ScopeOfWorkTechnician{

	private long id;

	private int sync;
		
	private long sowId;
	
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
	public ScopeOfWorkTechnician toScopeOfWorkTechnician(){
		ScopeOfWorkTechnician work = new ScopeOfWorkTechnician();

		work.setRid(rid);
		work.setTechId(techId);
		work.setTechName(techName);

		return work;
	}

	public long getSowId() {
		return sowId;
	}

	public void setSowId(long sowId) {
		this.sowId = sowId;
	}


	
}
