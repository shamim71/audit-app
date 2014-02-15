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
	
}
