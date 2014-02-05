package com.versacomllc.audit.activity;

import static com.versacomllc.audit.utils.Constants.ACTION_FINISH;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.versacomllc.audit.AuditManagement;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.model.AuthenticationResult;
import com.versacomllc.audit.spice.SpiceRestHelper;

public class BaseActivity extends Activity {

	protected SpiceRestHelper restHelper = new SpiceRestHelper();
	
	private FinishReceiver finishReceiver;
	
	protected DatabaseHandler dbHandler = null;
	
	protected AuditManagement getApplicationState() {
		return (AuditManagement) getApplication();
	}
	protected AuthenticationResult currentUser(){
		return getApplicationState().getAuthentication().getResult();
		
	}
	protected void registerActivityFinishSignal(){
        finishReceiver= new FinishReceiver();
        registerReceiver(finishReceiver, new IntentFilter(ACTION_FINISH));
	}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(finishReceiver != null){
        	unregisterReceiver(finishReceiver);
        }
    }

    private final class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_FINISH)) 
                finish();
        }
    }
}
