package com.versacomllc.audit.utils;

import static com.versacomllc.audit.utils.Constants.SERVER_ROOT;

import java.text.MessageFormat;

/**
 * @author SAhmmed
 *
 */
public enum EndPoints {

	
	
	REST_CALL_POST_AUTHENTICATE("/authenticate"),
	/**
	 * Load all inventory sites.
	 */
	REST_CALL_GET_QBASE_CUSTOMERS("/qbase/customers"),
	
	REST_CALL_GET_QBASE_SITE_WORK_TYPES("/qbase/audit/siteworks"),
	
	REST_CALL_GET_QBASE_DEFECTS("/qbase/defects"),
	
	REST_CALL_GET_QBASE_EMPLOYEES("/qbase/employees"),
	
	REST_CALL_GET_ADJUSTMENT_CONF("/configurations"),

	REST_CALL_GET_QBASE_AUDITS("/qbase/audits"),
	
	REST_CALL_POST_UPDATE_AUDIT("/qbase/audits/{0}"),
	
	REST_CALL_POST_AUDITS("/qbase/audits");
	
	private final String address;

	private EndPoints(String address) {
		this.address = address;
	}

	public String getSimpleAddress() {
		return SERVER_ROOT
				+ address;
	}

	/**
	 * Return the formatted URL variables
	 * 
	 * @param args
	 *            URL arguments
	 * 
	 * @return the formatted string
	 */
	public String getAddress(Object... args) {
		return MessageFormat.format(getSimpleAddress(), args);
	}
	
	
}
