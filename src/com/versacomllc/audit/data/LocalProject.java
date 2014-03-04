package com.versacomllc.audit.data;

import com.versacomllc.audit.model.Project;

public class LocalProject extends Project{
	
	private long id;
	
	private long sync;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSync() {
		return sync;
	}

	public void setSync(long sync) {
		this.sync = sync;
	}

	@Override
	public String toString() {
		return  name ;
	}



}
