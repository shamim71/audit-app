package com.versacomllc.audit.data;

import com.versacomllc.audit.model.AuthenticationResult;

public class Employee extends AuthenticationResult{

	private int localId;

	public int getLocalId() {
		return localId;
	}

	public void setLocalId(int localId) {
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
