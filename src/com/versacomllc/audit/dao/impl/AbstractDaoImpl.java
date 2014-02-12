package com.versacomllc.audit.dao.impl;

import java.text.ParseException;
import java.util.Date;

import com.versacomllc.audit.utils.Constants;

public abstract class AbstractDaoImpl {

	protected long getDateAsInt(String date) {
		try {
			Date dt = Constants.US_DATEFORMAT.parse(date);
			return dt.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	protected String getLongAsDate(long val) {
		Date dt = new Date(val);
		return Constants.US_DATEFORMAT.format(dt);
	}
}
