package com.versacomllc.audit;

import static com.versacomllc.audit.utils.Constants.EXTRA_AUDIT_ID;
import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;




import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.versacomllc.audit.activity.LoginActivity;
import com.versacomllc.audit.adapter.AuditListAdapter;
import com.versacomllc.audit.data.AuditListContent;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.LocalAudit;
import com.versacomllc.audit.utils.Constants;



/**
 * A fragment representing a single UserAudit detail screen. This fragment is
 * either contained in a {@link UserAuditListActivity} in two-pane mode (on
 * tablets) or a {@link UserAuditDetailActivity} on handsets.
 */
public class UserAuditDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private AuditListContent.AuditListPanel mItem;

	private List<LocalAudit> audits;
	private DatabaseHandler dbHandler = null;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public UserAuditDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dbHandler = new DatabaseHandler(getActivity());
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			final String tabKey = getArguments().getString(ARG_ITEM_ID);
			//TODO should load audit based on tab key (recent/user audits)
			
			mItem = AuditListContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
			Log.d(Constants.LOG_TAG, "Selected tab id: "+ tabKey);
			if(tabKey.equals("2")){
				AuditManagement app =	(AuditManagement)	getActivity().getApplication();
				final String userId = app.getAuthentication().getResult().getqBaseRef();
				Log.d(Constants.LOG_TAG, "Loading audits by user id: "+ userId);
				audits = dbHandler.getAuditDao().getAllInternalAuditsByEmployee(userId);
			}
			else{
				audits = dbHandler.getAuditDao().getAllInternalAudits();
			}
			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_useraudit_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			
			ListView listViewItems = (ListView) rootView.findViewById(R.id.lv_audit_list);
			
			AuditListAdapter adapter = new AuditListAdapter(getActivity(), R.layout.audit_list_item,
					audits);
			listViewItems.setAdapter(adapter);
			
			listViewItems.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					LocalAudit item = (LocalAudit) parent.getAdapter().getItem(position);

					if (item != null) {
						Log.d(LOG_TAG, "Loading audit with internal id: "+ item.getId());
						
						Intent intent = new Intent(getActivity(), InternalAuditListActivity.class);
						intent.putExtra(EXTRA_AUDIT_ID,String.valueOf(item.getId()));
						startActivity(intent);
					}

				}
			});
			listViewItems.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(final AdapterView<?> parent, View view,
						final int position, long id) {
					
					
					
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage(R.string.delete_confirmation_message)
							.setTitle(R.string.delete_confirmation_title);
					builder.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									LocalAudit item = (LocalAudit) parent.getAdapter()
											.getItem(position);

									if (item != null) {
						
										//.notifyDataSetChanged();

									}
								}
							});
					builder.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

								}
							});

					AlertDialog dialog = builder.create();
					dialog.show();
					return true;
				}
			});
		}

		return rootView;
	}
}
