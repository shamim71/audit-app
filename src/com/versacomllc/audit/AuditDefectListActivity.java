package com.versacomllc.audit;

import static com.versacomllc.audit.AuditDefectDetailFragment.ARG_ITEM_ID;
import static com.versacomllc.audit.utils.Constants.EXTRA_AUDIT_DEFECT_ID;
import static com.versacomllc.audit.utils.Constants.EXTRA_AUDIT_ID;
import static com.versacomllc.audit.utils.Constants.EXTRA_SOW_ID;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

import com.versacomllc.audit.utils.Constants;

/**
 * An activity representing a list of AuditDefects. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link AuditDefectDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link AuditDefectListFragment} and the item details (if present) is a
 * {@link AuditDefectDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link AuditDefectListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class AuditDefectListActivity extends FragmentActivity implements
		AuditDefectListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private long auditId = -1;
	private long localId = -1;
	private long sowId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auditdefect_list);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		if (intent != null && intent.getExtras() != null
				&& intent.getExtras().containsKey(EXTRA_AUDIT_ID)) {

			auditId = intent.getExtras().getLong(EXTRA_AUDIT_ID);
		}
		if (intent != null && intent.getExtras() != null
				&& intent.getExtras().containsKey(EXTRA_AUDIT_DEFECT_ID)) {

			localId = intent.getExtras().getLong(EXTRA_AUDIT_DEFECT_ID);
		}
		if (intent != null && intent.getExtras() != null
				&& intent.getExtras().containsKey(EXTRA_SOW_ID)) {

			sowId = intent.getExtras().getLong(EXTRA_SOW_ID);
		}
		
		if (findViewById(R.id.auditdefect_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((AuditDefectListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.auditdefect_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			finish();
			Log.d(Constants.LOG_TAG, "Navigating back...");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link AuditDefectListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ARG_ITEM_ID, id);
	
			arguments.putLong(EXTRA_AUDIT_ID, auditId);
			
			if(localId != -1L){
				arguments.putLong(EXTRA_AUDIT_DEFECT_ID, localId);
			}
			if(sowId != -1L){
				arguments.putLong(EXTRA_SOW_ID, sowId);
			}
		
		
			
			AuditDefectDetailFragment fragment = new AuditDefectDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.auditdefect_detail_container, fragment)
					.commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this,	AuditDefectDetailActivity.class);

			detailIntent.putExtra(EXTRA_AUDIT_ID, auditId);
			
			
			
			if(localId != -1){
				detailIntent.putExtra(EXTRA_AUDIT_DEFECT_ID, localId);
			}
			if(sowId != -1){
				detailIntent.putExtra(EXTRA_SOW_ID, sowId);
			}
			
			detailIntent.putExtra(ARG_ITEM_ID, id);
			
			startActivity(detailIntent);
		}
	}
}
