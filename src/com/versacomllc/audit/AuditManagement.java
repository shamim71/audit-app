package com.versacomllc.audit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Application;

import com.google.gson.Gson;
import com.versacomllc.audit.model.AuthenticationResponse;

import com.versacomllc.audit.utils.FileDataStorageManager;
import com.versacomllc.audit.utils.FileDataStorageManager.StorageFile;

/**
 * In-memory state cache
 */
public class AuditManagement extends Application {




	private Gson jsonHelper = new Gson();
	
	private static long currentAudit = -1;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}



	public void saveAuthentication(AuthenticationResponse object) {
		final String jsonContent = jsonHelper.toJson(object);
		FileDataStorageManager.saveContentToFile(getBaseContext(),
				StorageFile.USER_AUTHENTICATION, jsonContent);
	}

	public AuthenticationResponse getAuthentication(){
		return loadObject(AuthenticationResponse.class,
				StorageFile.USER_AUTHENTICATION);
	}

	

	private <T> T loadObject(Class<T> type, StorageFile name) {
		String jsonContent = FileDataStorageManager.getContentFromFile(
				getBaseContext(), name);

		if (StringUtils.isEmpty(jsonContent))
			return null;

		T dtos = jsonHelper.fromJson(jsonContent, type);

		return dtos;
	}

	public  long getCurrentAudit() {
		return currentAudit;
	}

	public  void setCurrentAudit(long currentAudit) {
		AuditManagement.currentAudit = currentAudit;
	}

	private static long currentAuditDefect = -1;

	public  long getCurrentAuditDefect() {
		return currentAuditDefect;
	}



	public  void setCurrentAuditDefect(long currentAuditDefect) {
		AuditManagement.currentAuditDefect = currentAuditDefect;
	}
	

}
