package com.versacomllc.audit.activity;

import static com.versacomllc.audit.utils.Constants.ACTION_FINISH;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.versacomllc.audit.model.ItemInventory;
import com.versacomllc.audit.utils.Constants;
import com.versacomllc.audit.R;

public class HomeActivity extends BaseActivity {

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setTitle(R.string.app_name);
		sendBroadcast(new Intent(ACTION_FINISH));

		registerActivityFinishSignal();
		

	}


	public void launchCreateAudit(View v) {

		Intent intent = new Intent(this, SiteAuditActivity.class);
		intent.putExtra(Constants.EXTRA_TRANSACTION_TYPE, false);
		startActivity(intent);
	}

	public void launchShowAudits(View v) {
		Intent intent = new Intent(this, SiteAuditActivity.class);
		intent.putExtra(Constants.EXTRA_TRANSACTION_TYPE, true);
		startActivity(intent);
	}

	public void signOut(View v) {

	

		getApplicationState().saveAuthentication(null);
		
		getApplicationState().saveInventoryItems(new ItemInventory[0]);
		
		//getApplicationState().saveInventorySites(new InventorySite[0]);
		
		sendBroadcast(new Intent(ACTION_FINISH));
		
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

}
