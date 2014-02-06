package com.versacomllc.audit;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;




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
				audits = dbHandler.getAllInternalAuditsByEmployee(userId);
			}
			else{
				audits = dbHandler.getAllInternalAudits();
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
			
			ListView lstView = (ListView) rootView.findViewById(R.id.lv_audit_list);
			
			AuditListAdapter adapter = new AuditListAdapter(getActivity(), R.layout.audit_list_item,
					audits);
			lstView.setAdapter(adapter);
			
		}

		return rootView;
	}
}
