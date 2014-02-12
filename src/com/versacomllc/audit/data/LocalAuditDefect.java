package com.versacomllc.audit.data;

import com.versacomllc.audit.model.AuditDefect;

public class LocalAuditDefect extends AuditDefect{

	private long localId;

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}
		
	
}
