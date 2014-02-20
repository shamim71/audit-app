package com.versacomllc.audit.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class Constants {

	//public static final String SERVER_ROOT = "http://dalqbase1.versacomllc.com:8080/quickbooks-gateway-server";
	public static final String SERVER_ROOT = "http://d1mnzch1.versacomllc.com:9080/quickbooks-gateway-server";

	public static final String FILE_UPLOAD_PATH  = "http://d1mnzch1.versacomllc.com:9080/easy-ecm-service/FileUploadServlet?workspace=EasyECM&path=AuditAppPictures";
	public static final String FILE_CONTENT_PATH = "http://d1mnzch1.versacomllc.com:9080/easy-ecm-service/service/file/content?path=/AuditAppPictures/";
	
	public static final String LOG_TAG = "AuditManagement";

	public static final String REST_LOG_TAG = "REST";

	public static final int ACTIVITY_RESULT_FAILED = 2;

	public static final int EMPTY_RESOURCE_ID = 0;

	public static final String EXTRA_BARCODE = "com.versacomllc.audit.EXTRA_BARCODE";
	public static final String EXTRA_ITEM_ADDED = "com.versacomllc.audit.EXTRA_ITEM_ADDED";
	public static final String EXTRA_AUDIT_ID = "com.versacomllc.audit.EXTRA_AUDIT_ID";
	public static final String EXTRA_AUDIT_DEFECT_ID = "com.versacomllc.audit.EXTRA_AUDIT_DEFECT_ID";
	public static final String EXTRA_SOW_ID = "com.versacomllc.audit.EXTRA_SOW_ID";
	
	public static final String ACTION_FINISH = "com.versacomllc.audit.activity.LoginActivity.ACTION_FINISH";
	
	public static final DateFormat US_DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy");
}
