package com.versacomllc.audit.data;

import com.versacomllc.audit.model.Defect;

public class LocalDefect extends Defect{

	private long localId;

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getDescription();
	}
	
	
}
