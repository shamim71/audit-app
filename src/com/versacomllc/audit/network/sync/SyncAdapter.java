/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.versacomllc.audit.network.sync;

import static com.versacomllc.audit.utils.Constants.FILE_UPLOAD_PATH;
import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.Employee;
import com.versacomllc.audit.data.LocalAudit;
import com.versacomllc.audit.data.LocalAuditDefect;
import com.versacomllc.audit.data.LocalCustomer;
import com.versacomllc.audit.data.LocalScopeOfWork;
import com.versacomllc.audit.model.AuditDefect;
import com.versacomllc.audit.model.Customer;
import com.versacomllc.audit.model.Defect;
import com.versacomllc.audit.model.InternalAudit;
import com.versacomllc.audit.model.ScopeOfWork;
import com.versacomllc.audit.model.StringResponse;
import com.versacomllc.audit.network.sync.provider.FeedContract;
import com.versacomllc.audit.spice.GenericGetRequest;
import com.versacomllc.audit.spice.GenericPostRequest;
import com.versacomllc.audit.spice.GenericSpiceCallback;
import com.versacomllc.audit.spice.RestCall;
import com.versacomllc.audit.spice.SpiceRestHelper;
import com.versacomllc.audit.utils.Constants;
import com.versacomllc.audit.utils.EndPoints;
import com.versacomllc.audit.utils.Utils;

/**
 * Define a sync adapter for the app.
 * 
 * <p>
 * This class is instantiated in {@link SyncService}, which also binds
 * SyncAdapter to the system. SyncAdapter should only be initialized in
 * SyncService, never anywhere else.
 * 
 * <p>
 * The system calls onPerformSync() via an RPC call through the IBinder object
 * supplied by SyncService.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {
	public static final String TAG = "SyncAdapter";

	protected SpiceRestHelper restHelper = new SpiceRestHelper();

	/**
	 * URL to fetch content from during a sync.
	 * 
	 * <p>
	 * This points to the Android Developers Blog. (Side note: We highly
	 * recommend reading the Android Developer Blog to stay up to date on the
	 * latest Android platform developments!)
	 */
	private static final String FEED_URL = "http://android-developers.blogspot.com/atom.xml";

	/**
	 * Network connection timeout, in milliseconds.
	 */
	private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000; // 15 seconds

	/**
	 * Network read timeout, in milliseconds.
	 */
	private static final int NET_READ_TIMEOUT_MILLIS = 10000; // 10 seconds

	/**
	 * Content resolver, for performing database operations.
	 */
	private final ContentResolver mContentResolver;

	/**
	 * Project used when querying content provider. Returns all known fields.
	 */
	private DatabaseHandler dbHandler = null;
	private static final String[] PROJECTION = new String[] {
			FeedContract.Entry._ID, FeedContract.Entry.COLUMN_NAME_ENTRY_ID,
			FeedContract.Entry.COLUMN_NAME_TITLE,
			FeedContract.Entry.COLUMN_NAME_LINK,
			FeedContract.Entry.COLUMN_NAME_PUBLISHED };

	// Constants representing column positions from PROJECTION.
	public static final int COLUMN_ID = 0;
	public static final int COLUMN_ENTRY_ID = 1;
	public static final int COLUMN_TITLE = 2;
	public static final int COLUMN_LINK = 3;
	public static final int COLUMN_PUBLISHED = 4;

	/**
	 * Constructor. Obtains handle to content resolver for later use.
	 */
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContentResolver = context.getContentResolver();
		dbHandler = new DatabaseHandler(context);
	}

	/**
	 * Constructor. Obtains handle to content resolver for later use.
	 */
	public SyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
		mContentResolver = context.getContentResolver();
		dbHandler = new DatabaseHandler(context);
	}

	/**
	 * Called by the Android system in response to a request to run the sync
	 * adapter. The work required to read data from the network, parse it, and
	 * store it in the content provider is done here. Extending
	 * AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
	 * run on a background thread. For this reason, blocking I/O and other
	 * long-running tasks can be run <em>in situ</em>, and you don't have to set
	 * up a separate thread for them. .
	 * 
	 * <p>
	 * This is where we actually perform any work required to perform a sync.
	 * {@link AbstractThreadedSyncAdapter} guarantees that this will be called
	 * on a non-UI thread, so it is safe to peform blocking I/O here.
	 * 
	 * <p>
	 * The syncResult argument allows you to pass information back to the method
	 * that triggered the sync.
	 */
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		Log.i(TAG, "Beginning network synchronization");
		try {

			Log.i(TAG, "Streaming data from network: ");

			/** Should sync when Internet connection is available */
			if (Utils.isOnline(getContext())) {

				// Sync customer
				 this.loadCustomerList(getContext());

				// Add stie work types
				 this.loadSiteWorkTypesList(getContext());

				 this.loadEmployeeList(getContext());

				 this.loadDefectList(getContext());

				this.synchronizeAuditRecords(getContext());

			}

		} catch (RuntimeException e) {
			Log.e(TAG, "Error updating database: " + e.toString());
			syncResult.databaseError = true;
			return;
		}
		Log.i(TAG, "Network synchronization complete");
	}
	  private String uploadFile(String path) {
		  	

		  	String downloadPath = Constants.FILE_CONTENT_PATH;
		   
		    if(TextUtils.isEmpty(path)){
		    	Log.d(LOG_TAG, "File path is empty.");
		    	return null;
		    }
		
		    HttpClient httpclient = new DefaultHttpClient();
		    try {
		    	File file = new File(path);
		    
		      HttpPost httppost = new HttpPost(FILE_UPLOAD_PATH);
		      String mimeType = URLConnection.guessContentTypeFromName(path);
		      FileBody bin = new FileBody(file, mimeType);

		      MultipartEntity reqEntity = new MultipartEntity();
		      reqEntity.addPart(file.getName(), bin);

		      httppost.setEntity(reqEntity);

		      Log.i(TAG, "executing request " + httppost.getRequestLine());
		      HttpResponse response = httpclient.execute(httppost);
		      HttpEntity resEntity = response.getEntity();

		      if (resEntity != null) {
		        Log.i(TAG, "Response content length: " + resEntity.getContentLength());
		      }
		      downloadPath = downloadPath  + file.getName();

		    }
		    catch (ClientProtocolException e) {

		      e.printStackTrace();
		    }
		    catch (IOException e) {

		      e.printStackTrace();
		    }
		    finally {
		      try {
		        httpclient.getConnectionManager().shutdown();
		      }
		      catch (Exception ignore) {
		      }
		    }
		    return downloadPath;
		  }
	private void synchronizeAuditRecords(Context context) {
		final List<LocalAudit> lAudits = dbHandler.getAuditDao()
				.getAllPendingInternalAudits();

		
		if (lAudits == null || lAudits.size() == 0) {

			Log.d(LOG_TAG, "No new or changes records to submit on server");
			return;
		}

		for(LocalAudit audit: lAudits){
			if(audit.getRid().equals("-1")){
				
				loadChildRecords(audit);
				
				addLocalAuditToServer(audit, context);	
			}
			else{
				
				loadChildRecords(audit);
				
				updateLocalAuditToServer(audit, context);
			}
		}


	}
	private void loadChildRecords(LocalAudit audit){
		/** Scope of works */
		List<LocalScopeOfWork> localScopeOfWorks = dbHandler.getScopeOfWorkDao().getPendingScopeOfWorkByAuditId(audit.getId());
		audit.setWorks(localScopeOfWorks);
		
		/** Audit defects */
		List<LocalAuditDefect> auditDefects = dbHandler.getAuditDefectDao().getPendingAuditDefectsByAuditId(audit.getId());
		audit.setLocalAuditDefects(auditDefects);
		
		uploadAuditDefectPictures(auditDefects);
		
	}
	
	private void uploadAuditDefectPictures(List<LocalAuditDefect> auditDefects){
		
		for(LocalAuditDefect defect: auditDefects){
			
			final String picBeforePath = defect.getDefectPicBefore();
			
			final String picAfterPath = defect.getDefectPicAfter();
			
			final String downloadPathB = uploadFile(picBeforePath);
			
			final String downloadPathA = uploadFile(picAfterPath);
			
			defect.setDefectPicBeforeOnServer(downloadPathB);
			defect.setDefectPicAfteOnServer(downloadPathA);
			
		}
	}
	
	private void addLocalAuditToServer(final LocalAudit audit, Context context){
		String endPoint = EndPoints.REST_CALL_POST_AUDITS.getSimpleAddress();

		GenericSpiceCallback<InternalAudit> callBackInterface = new GenericSpiceCallback<InternalAudit>(
				context) {

			@Override
			public void onSpiceSuccess(InternalAudit response) {
				if (response != null) {
					Log.d(LOG_TAG,
							"Response from the server: "
									+ response.getRid());

					updateResonse(audit, response);

				}

			}

			@Override
			public void onSpiceError(RestCall<InternalAudit> restCall,
					StringResponse response) {
			}

			@Override
			public void onSpiceError(RestCall<InternalAudit> restCall,
					int reason, Throwable exception) {
			}

		};
		restHelper.execute(new GenericPostRequest<InternalAudit>(
				InternalAudit.class, endPoint, audit.toInternalAudit()), callBackInterface);
		
	}
	private void updateResonse(final LocalAudit audit, InternalAudit response){
		audit.setRid(response.getRid());
		audit.setSyn(1);
		
		dbHandler.getAuditDao().updateInternalAudit(audit);
		
		//Update Scope of works
		if(audit.getWorks() != null && response.getSiteWorks() != null){
			for(int i=0; i< audit.getWorks().size() ; i++){
				LocalScopeOfWork w = audit.getWorks().get(i);
				ScopeOfWork work = response.getSiteWorks().get(i);
				w.setRid(work.getRid());
				w.setSync(1);
				dbHandler.getScopeOfWorkDao().updateSOW(w);

			}
			List<LocalScopeOfWork> works = 	dbHandler.getScopeOfWorkDao().getPendingScopeOfWorkByAuditId(audit.getId());
			for(LocalScopeOfWork w: works){
				Log.d(LOG_TAG, w.toString());
			}
		}
		
		//Update audit Defects
		if(audit.getLocalAuditDefects() != null && response.getAuditDefects() != null){
			for(int i=0; i< audit.getLocalAuditDefects().size() ; i++){
				LocalAuditDefect d = audit.getLocalAuditDefects().get(i);
				AuditDefect defect = response.getAuditDefects().get(i);
				d.setRid(defect.getRid());
				d.setSync(1);
				dbHandler.getAuditDefectDao().updateAuditDefect(d);
			}
		}
	}
	private void updateLocalAuditToServer(final LocalAudit audit, Context context){
		String endPoint = EndPoints.REST_CALL_POST_UPDATE_AUDIT.getAddress(audit.getRid());

		GenericSpiceCallback<InternalAudit> callBackInterface = new GenericSpiceCallback<InternalAudit>(
				context) {

			@Override
			public void onSpiceSuccess(InternalAudit response) {
				if (response != null) {
					Log.d(LOG_TAG,
							"Response from the server: "
									+ response.getRid());
						
					updateResonse(audit, response);
				}

			}

			@Override
			public void onSpiceError(RestCall<InternalAudit> restCall,
					StringResponse response) {
			}

			@Override
			public void onSpiceError(RestCall<InternalAudit> restCall,
					int reason, Throwable exception) {
			}

		};
		restHelper.execute(new GenericPostRequest<InternalAudit>(
				InternalAudit.class, endPoint, audit.toInternalAudit()), callBackInterface);
		
	}


	private void loadAuditList(Context context) {
		String endPoint = EndPoints.REST_CALL_GET_QBASE_AUDITS
				.getSimpleAddress();
		Log.d(TAG, "Importing audits;");
		GenericSpiceCallback<InternalAudit[]> callBackInterface = new GenericSpiceCallback<InternalAudit[]>(
				context) {

			@Override
			public void onSpiceSuccess(InternalAudit[] response) {
				if (response != null) {
					
					Log.d(LOG_TAG, "Total records received from server: "+ response.length);
					
					for (InternalAudit audit : response) {
						LocalAudit lAudit = new LocalAudit(audit);
						int rowEffected = dbHandler.getAuditDao()
								.updateInternalAuditByRid(lAudit);
						if (rowEffected == 0) {
							dbHandler.getAuditDao().addInternalAudit(lAudit);
						}
					}

				}
			}

			@Override
			public void onSpiceError(RestCall<InternalAudit[]> restCall,
					StringResponse response) {
			}

			@Override
			public void onSpiceError(RestCall<InternalAudit[]> restCall,
					int reason, Throwable exception) {
			}
		};
		restHelper.execute(new GenericGetRequest<InternalAudit[]>(
				InternalAudit[].class, endPoint), callBackInterface);
	}

	private void loadEmployeeList(Context context) {
		String endPoint = EndPoints.REST_CALL_GET_QBASE_EMPLOYEES
				.getSimpleAddress();
		Log.d(TAG, "Importing employees;");
		GenericSpiceCallback<Employee[]> callBackInterface = new GenericSpiceCallback<Employee[]>(
				context) {

			@Override
			public void onSpiceSuccess(Employee[] response) {
				if (response != null) {
					dbHandler.getEmployeeDao().addEmployeeList(
							Arrays.asList(response));
				}
			}

			@Override
			public void onSpiceError(RestCall<Employee[]> restCall,
					StringResponse response) {
			}

			@Override
			public void onSpiceError(RestCall<Employee[]> restCall, int reason,
					Throwable exception) {
			}
		};
		restHelper.execute(new GenericGetRequest<Employee[]>(Employee[].class,
				endPoint), callBackInterface);
	}

	private void loadDefectList(Context context) {
		String endPoint = EndPoints.REST_CALL_GET_QBASE_DEFECTS
				.getSimpleAddress();
		Log.d(TAG, "Importing defect list;");
		GenericSpiceCallback<Defect[]> callBackInterface = new GenericSpiceCallback<Defect[]>(
				context) {

			@Override
			public void onSpiceSuccess(Defect[] response) {
				if (response != null) {
					dbHandler.getDefectDao().addDefectList(
							Arrays.asList(response));
				}
			}

			@Override
			public void onSpiceError(RestCall<Defect[]> restCall,
					StringResponse response) {
			}

			@Override
			public void onSpiceError(RestCall<Defect[]> restCall, int reason,
					Throwable exception) {
			}
		};
		restHelper.execute(new GenericGetRequest<Defect[]>(Defect[].class,
				endPoint), callBackInterface);
	}

	private void loadSiteWorkTypesList(Context context) {
		String endPoint = EndPoints.REST_CALL_GET_QBASE_SITE_WORK_TYPES
				.getSimpleAddress();
		Log.d(TAG, "Importing site work type;");
		GenericSpiceCallback<String[]> callBackInterface = new GenericSpiceCallback<String[]>(
				context) {

			@Override
			public void onSpiceSuccess(String[] response) {
				if (response != null) {
					dbHandler.getSiteWorkDao().addWorkTypes(
							Arrays.asList(response));
				}
			}

			@Override
			public void onSpiceError(RestCall<String[]> restCall,
					StringResponse response) {
			}

			@Override
			public void onSpiceError(RestCall<String[]> restCall, int reason,
					Throwable exception) {
			}
		};
		restHelper.execute(new GenericGetRequest<String[]>(String[].class,
				endPoint), callBackInterface);
	}

	/**
	 * @param context
	 */
	private void loadCustomerList(Context context) {
		String endPoint = EndPoints.REST_CALL_GET_QBASE_CUSTOMERS
				.getSimpleAddress();

		List<LocalCustomer> customers = dbHandler.getAllCustomers();
		final Map<String, LocalCustomer> customerMap = new HashMap<String, LocalCustomer>();

		for (LocalCustomer customer : customers) {
			customerMap.put(customer.getRid(), customer);
		}

		GenericSpiceCallback<Customer[]> callBackInterface = new GenericSpiceCallback<Customer[]>(
				context) {

			@Override
			public void onSpiceSuccess(Customer[] response) {
				if (response != null) {
					for (Customer customer : response) {
						if (!customerMap.containsKey(customer.getRid())) {
							Log.d(TAG,
									" Adding customer to local db with rid: "
											+ customer.getRid());
							dbHandler.addCustomer(new LocalCustomer(customer));
						}
					}
				}

			}

			@Override
			public void onSpiceError(RestCall<Customer[]> restCall,
					StringResponse response) {
			}

			@Override
			public void onSpiceError(RestCall<Customer[]> restCall, int reason,
					Throwable exception) {
			}

		};

		restHelper.execute(new GenericGetRequest<Customer[]>(Customer[].class,
				endPoint), callBackInterface);

	}

	/**
	 * Read XML from an input stream, storing it into the content provider.
	 * 
	 * <p>
	 * This is where incoming data is persisted, committing the results of a
	 * sync. In order to minimize (expensive) disk operations, we compare
	 * incoming data with what's already in our database, and compute a merge.
	 * Only changes (insert/update/delete) will result in a database write.
	 * 
	 * <p>
	 * As an additional optimization, we use a batch operation to perform all
	 * database writes at once.
	 * 
	 * <p>
	 * Merge strategy: 1. Get cursor to all items in feed<br/>
	 * 2. For each item, check if it's in the incoming data.<br/>
	 * a. YES: Remove from "incoming" list. Check if data has mutated, if so,
	 * perform database UPDATE.<br/>
	 * b. NO: Schedule DELETE from database.<br/>
	 * (At this point, incoming database only contains missing items.)<br/>
	 * 3. For any items remaining in incoming list, ADD to database.
	 */
	public void updateLocalFeedData(final InputStream stream,
			final SyncResult syncResult) throws IOException,
			XmlPullParserException, RemoteException,
			OperationApplicationException, ParseException {

	}

}
