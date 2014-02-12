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

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

















import org.xmlpull.v1.XmlPullParserException;

import com.versacomllc.audit.R;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.Employee;
import com.versacomllc.audit.data.LocalCustomer;
import com.versacomllc.audit.model.Customer;
import com.versacomllc.audit.model.Defect;
import com.versacomllc.audit.model.StringResponse;
import com.versacomllc.audit.network.sync.provider.FeedContract;
import com.versacomllc.audit.spice.DefaultProgressIndicatorState;
import com.versacomllc.audit.spice.GenericGetRequest;
import com.versacomllc.audit.spice.GenericSpiceCallback;
import com.versacomllc.audit.spice.RestCall;
import com.versacomllc.audit.spice.RetrySpiceCallback;
import com.versacomllc.audit.spice.SpiceCallbackInterface;
import com.versacomllc.audit.spice.SpiceRestHelper;
import com.versacomllc.audit.utils.EndPoints;
import com.versacomllc.audit.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";

	protected SpiceRestHelper restHelper = new SpiceRestHelper();
	
    /**
     * URL to fetch content from during a sync.
     *
     * <p>This points to the Android Developers Blog. (Side note: We highly recommend reading the
     * Android Developer Blog to stay up to date on the latest Android platform developments!)
     */
    private static final String FEED_URL = "http://android-developers.blogspot.com/atom.xml";

    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private DatabaseHandler dbHandler = null;
    private static final String[] PROJECTION = new String[] {
            FeedContract.Entry._ID,
            FeedContract.Entry.COLUMN_NAME_ENTRY_ID,
            FeedContract.Entry.COLUMN_NAME_TITLE,
            FeedContract.Entry.COLUMN_NAME_LINK,
            FeedContract.Entry.COLUMN_NAME_PUBLISHED};

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
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        dbHandler = new DatabaseHandler(context);
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        try {

                Log.i(TAG, "Streaming data from network: ");

                //updateLocalFeedData(stream, syncResult);
                
                /** Should sync when Internet connection is available */
                if(Utils.isOnline(getContext())){
                	
                	//Sync customer 
                	 this.loadCustomerList(getContext());
                	 
                	 //Add stie work types
                	 this.loadSiteWorkTypesList(getContext());
                	 
                	 this.loadEmployeeList(getContext());
                	 
                	 this.loadDefectList(getContext());
                }
               
        
        } catch (RuntimeException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }
        Log.i(TAG, "Network synchronization complete");
    }
    
    private void loadEmployeeList(Context context){
		String endPoint = EndPoints.REST_CALL_GET_QBASE_EMPLOYEES
				.getSimpleAddress();
		Log.d(TAG, "Importing employees;");
		GenericSpiceCallback<Employee[]> callBackInterface = new GenericSpiceCallback<Employee[]>(context) {

			@Override
			public void onSpiceSuccess(Employee[] response) {
				if(response != null){
						dbHandler.getEmployeeDao().addEmployeeList(Arrays.asList(response));
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
		restHelper.execute(new GenericGetRequest<Employee[]>(Employee[].class, endPoint), callBackInterface);
    }
    
    private void loadDefectList(Context context){
		String endPoint = EndPoints.REST_CALL_GET_QBASE_DEFECTS
				.getSimpleAddress();
		Log.d(TAG, "Importing defect list;");
		GenericSpiceCallback<Defect[]> callBackInterface = new GenericSpiceCallback<Defect[]>(context) {

			@Override
			public void onSpiceSuccess(Defect[] response) {
				if(response != null){
					dbHandler.getDefectDao().addDefectList(Arrays.asList(response));
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
		restHelper.execute(new GenericGetRequest<Defect[]>(Defect[].class, endPoint), callBackInterface);
    }
    
    private void loadSiteWorkTypesList(Context context){
		String endPoint = EndPoints.REST_CALL_GET_QBASE_SITE_WORK_TYPES
				.getSimpleAddress();
		Log.d(TAG, "Importing site work type;");
		GenericSpiceCallback<String[]> callBackInterface = new GenericSpiceCallback<String[]>(context) {

			@Override
			public void onSpiceSuccess(String[] response) {
				if(response != null){
						dbHandler.getSiteWorkDao().addWorkTypes(Arrays.asList(response));
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
		restHelper.execute(new GenericGetRequest<String[]>(String[].class, endPoint), callBackInterface);
    }
    /**
     * @param context
     */
    private void loadCustomerList(Context context){
		String endPoint = EndPoints.REST_CALL_GET_QBASE_CUSTOMERS
				.getSimpleAddress();

		List<LocalCustomer> customers = dbHandler.getAllCustomers();
		final Map<String, LocalCustomer> customerMap = new HashMap<String, LocalCustomer>();
		
		for(LocalCustomer customer: customers){
			customerMap.put(customer.getRid(), customer);
		}
		
		GenericSpiceCallback<Customer[]> callBackInterface = new GenericSpiceCallback<Customer[]>(context) {

			@Override
			public void onSpiceSuccess(Customer[] response) {
				if(response != null){
					for(Customer customer: response){
						if(!customerMap.containsKey(customer.getRid())){
							Log.d(TAG, " Adding customer to local db with rid: "+ customer.getRid());
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
		
		restHelper.execute(new GenericGetRequest<Customer[]>(Customer[].class, endPoint), callBackInterface);
		
    }
    /**
     * Read XML from an input stream, storing it into the content provider.
     *
     * <p>This is where incoming data is persisted, committing the results of a sync. In order to
     * minimize (expensive) disk operations, we compare incoming data with what's already in our
     * database, and compute a merge. Only changes (insert/update/delete) will result in a database
     * write.
     *
     * <p>As an additional optimization, we use a batch operation to perform all database writes at
     * once.
     *
     * <p>Merge strategy:
     * 1. Get cursor to all items in feed<br/>
     * 2. For each item, check if it's in the incoming data.<br/>
     *    a. YES: Remove from "incoming" list. Check if data has mutated, if so, perform
     *            database UPDATE.<br/>
     *    b. NO: Schedule DELETE from database.<br/>
     * (At this point, incoming database only contains missing items.)<br/>
     * 3. For any items remaining in incoming list, ADD to database.
     */
    public void updateLocalFeedData(final InputStream stream, final SyncResult syncResult)
            throws IOException, XmlPullParserException, RemoteException,
            OperationApplicationException, ParseException {
       
    }


}
