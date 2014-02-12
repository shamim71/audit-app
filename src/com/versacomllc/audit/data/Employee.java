package com.versacomllc.audit.data;

import com.versacomllc.audit.model.AuthenticationResult;

public class Employee extends AuthenticationResult{

	private long localId;

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	@Override
	public String toString() {
		
		return  this.getFirstName() + " "+ this.getLastName();
	}

	public String getName(){
		return  this.getFirstName() + " "+ this.getLastName();
	}
	
	
}
