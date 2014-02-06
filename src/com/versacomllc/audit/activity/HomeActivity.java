package com.versacomllc.audit.activity;

import static com.versacomllc.audit.utils.Constants.ACTION_FINISH;
import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.versacomllc.audit.model.ItemInventory;
import com.versacomllc.audit.network.sync.SyncUtils;
import com.versacomllc.audit.network.sync.accounts.GenericAccountService;
import com.versacomllc.audit.network.sync.provider.FeedContract;
import com.versacomllc.audit.utils.Constants;
import com.versacomllc.audit.R;
import com.versacomllc.audit.UserAuditListActivity;

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
		Intent intent = new Intent(this, UserAuditListActivity.class);

		startActivity(intent);
	}

	public void synchronizeData(View v) {
		Log.i(Constants.LOG_TAG, "Synchronizing data...");
		SyncUtils.TriggerRefresh();
	}

	public void signOut(View v) {

		getApplicationState().saveAuthentication(null);

		getApplicationState().saveInventoryItems(new ItemInventory[0]);

		// getApplicationState().saveInventorySites(new InventorySite[0]);

		sendBroadcast(new Intent(ACTION_FINISH));

		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		mSyncStatusObserver.onStatusChanged(0);

		// Watch for sync state changes
		final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING
				| ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
		// mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask,
		// mSyncStatusObserver);
	}

	@Override
	public void onPause() {
		super.onPause();
		/*
		 * if (mSyncObserverHandle != null) {
		 * ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
		 * mSyncObserverHandle = null; }
		 */
	}

	public void setRefreshActionButtonState(boolean refreshing) {

		Button btnSync = (Button) findViewById(R.id.btn_sysnchronize_data);
		if (refreshing) {
			btnSync.setEnabled(false);
			btnSync.setText("Synchronization in progress...");
		} else {
			btnSync.setEnabled(true);
			btnSync.setText("Synchronize Data");
		}
	}

	/**
	 * Crfate a new anonymous SyncStatusObserver. It's attached to the app's
	 * ContentResolver in onResume(), and removed in onPause(). If status
	 * changes, it sets the state of the Refresh button. If a sync is active or
	 * pending, the Refresh button is replaced by an indeterminate ProgressBar;
	 * otherwise, the button itself is displayed.
	 */
	private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
		/** Callback invoked with the sync adapter status changes. */
		@Override
		public void onStatusChanged(int which) {
			runOnUiThread(new Runnable() {
				/**
				 * The SyncAdapter runs on a background thread. To update the
				 * UI, onStatusChanged() runs on the UI thread.
				 */
				@Override
				public void run() {
					// Create a handle to the account that was created by
					// SyncService.CreateSyncAccount(). This will be used to
					// query the system to
					// see how the sync status has changed.
					Account account = GenericAccountService.GetAccount();
					if (account == null) {
						// GetAccount() returned an invalid value. This
						// shouldn't happen, but
						// we'll set the status to "not refreshing".
						setRefreshActionButtonState(false);
						return;
					}

					// Test the ContentResolver to see if the sync adapter is
					// active or pending.
					// Set the state of the refresh button accordingly.
					boolean syncActive = ContentResolver.isSyncActive(account,
							FeedContract.CONTENT_AUTHORITY);
					boolean syncPending = ContentResolver.isSyncPending(
							account, FeedContract.CONTENT_AUTHORITY);
					 setRefreshActionButtonState(syncActive || syncPending);
				}
			});
		}
	};
}
