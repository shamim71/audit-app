package com.versacomllc.audit;

import java.util.List;

import com.versacomllc.audit.adapter.DefectAutocompleteListAdapter;
import com.versacomllc.audit.adapter.EmployeeAutocompleteListAdapter;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.Employee;
import com.versacomllc.audit.data.LocalDefect;
import com.versacomllc.audit.dummy.AuditDefectContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;



/**
 * A fragment representing a single AuditDefect detail screen. This fragment is
 * either contained in a {@link AuditDefectListActivity} in two-pane mode (on
 * tablets) or a {@link AuditDefectDetailActivity} on handsets.
 */
public class AuditDefectDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private AuditDefectContent.DefectTab mItem;

	private DatabaseHandler dbHandler;
	
	private AutoCompleteTextView autoCompleteTextView;
	private AutoCompleteTextView mATextViewTech;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AuditDefectDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dbHandler = new DatabaseHandler(getActivity());
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = AuditDefectContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (mItem != null && mItem.id.equals("1")) {
			return getDefectMasterView(inflater, container, savedInstanceState);
		}
		
		View rootView = inflater.inflate(R.layout.fragment_auditdefect_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.auditdefect_detail))
					.setText(mItem.content);
		}

		return rootView;
	}

	private View getDefectMasterView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_auditdefect_detail_main,
				container, false);
		
		final TextView defectDetails = (TextView) rootView.findViewById(R.id.tv_defect_details);
		
		autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.atv_defect_list);
		List<LocalDefect> defects = dbHandler.getDefectDao().getAllDefects();

		final DefectAutocompleteListAdapter adapter = new DefectAutocompleteListAdapter(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				defects);

		autoCompleteTextView.setThreshold(3);
		autoCompleteTextView.setAdapter(adapter);
		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
			LocalDefect defect = adapter.getItem(position);
			
			autoCompleteTextView.setText(defect.getCode());
			defectDetails.setText("Severity: ["+ defect.getSeverity() + "], Description: "+ defect.getDescription() );
			defectDetails.setVisibility(View.VISIBLE);
			
			}
		});
		
		mATextViewTech = (AutoCompleteTextView) rootView.findViewById(R.id.atv_responsible_tech);
		List<Employee> employess = dbHandler.getEmployeeDao().getAllEmployees();
		

		final EmployeeAutocompleteListAdapter employeeAutocompleteListAdapter = new EmployeeAutocompleteListAdapter(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				employess);

		mATextViewTech.setThreshold(2);
		mATextViewTech.setAdapter(employeeAutocompleteListAdapter);
		mATextViewTech.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Employee employee = employeeAutocompleteListAdapter.getItem(position);
			
			}
		});
		Button btnSave = (Button) rootView.findViewById(R.id.btn_save_defect);
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				
			}
		});
		return rootView;
	}
}
