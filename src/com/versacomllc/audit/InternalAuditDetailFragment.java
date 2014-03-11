package com.versacomllc.audit;

import static com.versacomllc.audit.utils.Constants.EXTRA_AUDIT_ID;
import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.versacomllc.audit.adapter.AuditDefectListAdapter;
import com.versacomllc.audit.adapter.ProjectDropDownListAdapter;
import com.versacomllc.audit.adapter.ScopeOfWorkListAdapter;
import com.versacomllc.audit.adapter.SimpleDropDownListAdapter;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.LocalAudit;
import com.versacomllc.audit.data.LocalCustomer;
import com.versacomllc.audit.data.LocalProject;
import com.versacomllc.audit.data.LocalScopeOfWork;
import com.versacomllc.audit.dummy.AuditContent;
import com.versacomllc.audit.model.AuthenticationResult;
import com.versacomllc.audit.model.Customer;
import com.versacomllc.audit.utils.Constants;

/**
 * A fragment representing a single InternalAudit detail screen. This fragment
 * is either contained in a {@link InternalAuditListActivity} in two-pane mode
 * (on tablets) or a {@link InternalAuditDetailActivity} on handsets.
 */
public class InternalAuditDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private AuditContent.Tab mItem;

	DatabaseHandler dbHandler = null;
	EditText mEditSiteId;
	EditText mEditCity;
	EditText mEditState;
	EditText mEditZip;
	
	EditText mEditAuditHour;
	EditText mEditAuditDate;
	Spinner auditTypeSpinner;
	Spinner auditStatusSpinner;
	Spinner auditResultSpinner;
	Spinner customerSpinner;
	Spinner projectSpinner;
	TextView mTextAuditor;

	LocalAudit audit = new LocalAudit();

	String auditId = null;
	SimpleDropDownListAdapter customerAdapter;
	ProjectDropDownListAdapter projectDropDownListAdapter;
	ScopeOfWorkListAdapter scopeOfWorkListAdapter;

	AuditDefectListAdapter defectListAdapter;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public InternalAuditDetailFragment() {
	}

	private AuditManagement getAppState() {
		return (AuditManagement) getActivity().getApplication();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dbHandler = new DatabaseHandler(getActivity());

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = AuditContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
		if (getArguments().containsKey(EXTRA_AUDIT_ID)) {
			auditId = getArguments().getString(EXTRA_AUDIT_ID);
		} else {
			if (getAppState().getCurrentAudit() != -1) {
				auditId = String.valueOf(getAppState().getCurrentAudit());
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mItem != null && mItem.id.equals("1")) {
			return getAuditMasterView(inflater, container, savedInstanceState);
		}
		if (mItem != null && mItem.id.equals("2")) {
			return getSOWView(inflater, container, savedInstanceState);
		}

		View rootView = inflater.inflate(
				R.layout.fragment_internalaudit_detail, container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.internalaudit_detail))
					.setText(mItem.content);
		}

		return rootView;
	}

	
	private View getSOWView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_internalaudit_detail_sow, container, false);

		if (TextUtils.isEmpty(auditId)) {
			return rootView;
		}
		Button btnSOW = (Button) rootView.findViewById(R.id.btn_Add_SOW);
		btnSOW.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//loadScopeOfWorkDialog(false, -1);
							
				Log.d(LOG_TAG, "Adding scope of work for audit id: "+ auditId);

				Intent intent = new Intent(getActivity(),
						ScopeOfWorkListActivity.class);
				getAppState().setCurrentSowId(-1);
				intent.putExtra(EXTRA_AUDIT_ID, Long.valueOf(auditId));
			/*	intent.putExtra(Constants.EXTRA_SOW_ID, -1L);*/
				startActivity(intent);
				
			}
		});

		ListView lstScopeOfWork = (ListView) rootView
				.findViewById(R.id.lv_sow_list);

		List<LocalScopeOfWork> works = dbHandler.getScopeOfWorkDao()
				.getScopeOfWorkByAuditId(Long.parseLong(auditId));

		scopeOfWorkListAdapter = new ScopeOfWorkListAdapter(getActivity(),
				R.layout.sow_list_item, works);
		lstScopeOfWork.setAdapter(scopeOfWorkListAdapter);
		lstScopeOfWork.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LocalScopeOfWork item = (LocalScopeOfWork) parent.getAdapter().getItem(
						position);

				if (item != null) {

					Log.d(LOG_TAG,
							"Loading scope of work with id: " + item.getId()
									+ " audit id: " + item.getAuditId());
					
					
					getAppState().setCurrentSowId(item.getId());
					Intent intent = new Intent(getActivity(),
							ScopeOfWorkListActivity.class);
					intent.putExtra(Constants.EXTRA_SOW_ID, item.getId());
					intent.putExtra(EXTRA_AUDIT_ID, Long.valueOf(auditId));
					startActivity(intent);
					
				}

			}
		});

		lstScopeOfWork
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(final AdapterView<?> parent,
							View view, final int position, long id) {

						final AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setMessage(R.string.delete_confirmation_message)
								.setTitle(R.string.delete_confirmation_title);
						builder.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										LocalScopeOfWork item = (LocalScopeOfWork) parent
												.getAdapter().getItem(position);

										if (item != null) {

											dbHandler.getScopeOfWorkDao()
													.deleteSOW(item.getId());
											scopeOfWorkListAdapter.clear();
											List<LocalScopeOfWork> works = dbHandler
													.getScopeOfWorkDao()
													.getScopeOfWorkByAuditId(
															Long.parseLong(auditId));
											scopeOfWorkListAdapter
													.addAll(works);
											scopeOfWorkListAdapter
													.notifyDataSetChanged();
										}
									}
								});
						builder.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

									}
								});

						AlertDialog dialog = builder.create();
						dialog.show();
						return true;
					}
				});

		return rootView;
	}


	private View getAuditMasterView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_internalaudit_detail_main, container, false);

		mEditAuditDate = (EditText) rootView.findViewById(R.id.et_audit_date);
		ImageButton btnPicker = (ImageButton) rootView
				.findViewById(R.id.ibtn_date_selector);

		btnPicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogFragment newFragment = new SelectDateFragment();
				newFragment.show(getFragmentManager(), "DatePicker");
			}
		});

		OnItemSelectedListener typeListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Object item = parent.getAdapter().getItem(position);
				audit.setAuditType(item.toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
		OnItemSelectedListener auditStatusListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Object item = parent.getAdapter().getItem(position);
				audit.setAuditStatus(item.toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
		OnItemSelectedListener auditResultListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Object item = parent.getAdapter().getItem(position);
				audit.setAuditResult(item.toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
		auditTypeSpinner = (Spinner) rootView.findViewById(R.id.sp_auditType);
		auditStatusSpinner = (Spinner) rootView
				.findViewById(R.id.sp_auditStatus);
		customerSpinner = (Spinner) rootView
				.findViewById(R.id.sp_customer_list);
		projectSpinner = (Spinner) rootView
				.findViewById(R.id.sp_project_list);
		
		auditResultSpinner = (Spinner) rootView.findViewById(R.id.sp_auditResult);
		
		mEditAuditDate = (EditText) rootView.findViewById(R.id.et_audit_date);
		mEditAuditHour = (EditText) rootView.findViewById(R.id.et_auditHour);
		mEditSiteId = (EditText) rootView.findViewById(R.id.et_customerSiteID);
		mEditCity = (EditText) rootView.findViewById(R.id.et_site_city);
		mEditState = (EditText) rootView.findViewById(R.id.et_site_state);
		mEditZip = (EditText) rootView.findViewById(R.id.et_site_zip);
		
		Button btnSave = (Button) rootView
				.findViewById(R.id.btn_save_audit_master);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				saveAuditMaster(v);

			}
		});

		mTextAuditor = (TextView) rootView.findViewById(R.id.tv_auditor);

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		populateSetDate(year, month + 1, day);

		populateSpinner(auditTypeSpinner, R.array.array_audit_type,
				typeListener);
		
		populateSpinner(auditStatusSpinner, R.array.array_audit_status,
				auditStatusListener);
		
		populateSpinner(auditResultSpinner, R.array.array_audit_result,
				auditResultListener);

		AuditManagement app = (AuditManagement) getActivity().getApplication();
		AuthenticationResult user = app.getAuthentication().getResult();

		final String auditUser = " Audited by: " + user.getFirstName() + " "
				+ user.getLastName();
		mTextAuditor.setText(auditUser);

		audit.setAuditedByEmployee(user.getqBaseRef());
		audit.setAuditedBy(user.getFirstName() + " " + user.getLastName());

		List<LocalCustomer> customers = dbHandler.getAllCustomers();

		populateCustomerList(rootView,
				customers.toArray(new Customer[customers.size()]));
		
		List<LocalProject> projects = dbHandler.getProjectDao().getAllProjects();
	
		populateProjectList(rootView, projects.toArray(new LocalProject[projects.size()]) );
		
		Log.d(LOG_TAG, "Loading audit id with id: "+ auditId);
		
		LocalAudit existingAudit = dbHandler.getAuditDao()
				.getInternalAuditsById(auditId);

		updateAuditMasterUI(existingAudit);

		return rootView;
	}

	private void updateAuditMasterUI(LocalAudit lAudit) {
		if (lAudit == null) {
			return;
		}
		audit = lAudit;
		projectSpinner.setSelection(getProjectIndex(lAudit.getProject()));
		customerSpinner.setSelection(getCustomerIndex(lAudit.getCustomer()));
		mEditSiteId.setText(lAudit.getSiteId());
		mEditCity.setText(lAudit.getCity());
		mEditState.setText(lAudit.getState());
		mEditZip.setText(lAudit.getZip());
		
		try {
			Date auditDate = Constants.US_DATEFORMAT.parse(lAudit
					.getAuditDate());
			Calendar cal = Calendar.getInstance();
			cal.setTime(auditDate);
			populateSetDate(cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		mEditAuditHour.setText(lAudit.getAuditHour());

		int index = getItemIndex(lAudit.getAuditType(),
				(ArrayAdapter) auditTypeSpinner.getAdapter());
		auditTypeSpinner.setSelection(index);

		int statusIndex = getItemIndex(lAudit.getAuditStatus(),
				(ArrayAdapter) auditStatusSpinner.getAdapter());
		auditStatusSpinner.setSelection(statusIndex);

		int resultIndex = getItemIndex(lAudit.getAuditResult(),
				(ArrayAdapter) auditResultSpinner.getAdapter());
		auditResultSpinner.setSelection(statusIndex);
		
		mTextAuditor.setText(lAudit.getAuditedBy());

	}

	private int getItemIndex(String value, ArrayAdapter adapter) {
		int size = adapter.getCount();
		for (int i = 0; i < size; i++) {
			String itm = (String) adapter.getItem(i);
			if (itm.equals(value)) {
				return i;
			}
		}
		return 0;
	}

	private int getCustomerIndex(String customerId) {
		int size = customerAdapter.getCount();
		for (int i = 0; i < size; i++) {
			Customer customer = customerAdapter.getItem(i);
			if (customer.getRid().equals(customerId)) {
				return i;
			}
		}
		return 0;
	}
	private int getProjectIndex(String projectId) {
		int size = projectDropDownListAdapter.getCount();
		for (int i = 0; i < size; i++) {
			LocalProject project = projectDropDownListAdapter.getItem(i);
			if (project.getRid().equals(projectId)) {
				return i;
			}
		}
		return 0;
	}
	private void populateCustomerList(View rootView, Customer[] objects) {

		List<Customer> list = Arrays.asList(objects);
		customerAdapter = new SimpleDropDownListAdapter(getActivity(),
				android.R.layout.simple_spinner_item, list);
		// Specify the layout to use when the list of choices appears
		customerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		customerSpinner.setAdapter(customerAdapter);

		customerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				Customer customer = (Customer) parent.getAdapter().getItem(
						position);

				Log.d(LOG_TAG, customer.getName());

				audit.setCustomer(customer.getRid());
				audit.setCustomerName(customer.getName());

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}
	private void populateProjectList(View rootView, LocalProject[] objects) {

		List<LocalProject> list = Arrays.asList(objects);
		projectDropDownListAdapter = new ProjectDropDownListAdapter(getActivity(),
				android.R.layout.simple_spinner_item, list);
		// Specify the layout to use when the list of choices appears
		projectDropDownListAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		projectSpinner.setAdapter(projectDropDownListAdapter);

		projectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				LocalProject obj = (LocalProject) parent.getAdapter().getItem(
						position);


				audit.setProject(obj.getRid());
				audit.setProjectName(obj.getName());

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}
	public void populateSetDate(int year, int month, int day) {
		mEditAuditDate.setText(month + "/" + day + "/" + year);
	}

	@SuppressLint("ValidFragment")
	public class SelectDateFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm + 1, dd);
		}
	}

	private void populateSpinner(Spinner spinner, int arrayId,
			OnItemSelectedListener listener) {

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), arrayId, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(listener);
	}

	private void createLocalAudit() {
		Log.i(LOG_TAG, "Adding records to internal audit table ");

		audit.setRid("-1");
		audit.setSyn(0);
		long id = dbHandler.getAuditDao().addInternalAudit(audit);

		auditId = String.valueOf(id);

		getAppState().setCurrentAudit(id);

		Toast.makeText(getActivity(), "Audit master record created",
				Toast.LENGTH_LONG).show();

	}

	private void saveAuditMaster(View view) {

		if (!TextUtils.isEmpty(mEditAuditHour.getText())) {
			audit.setAuditHour(mEditAuditHour.getText().toString());
		}
		if (!TextUtils.isEmpty(mEditAuditDate.getText())) {
			audit.setAuditDate(mEditAuditDate.getText().toString());
		}
		if (!TextUtils.isEmpty(mEditSiteId.getText())) {
			audit.setSiteId(mEditSiteId.getText().toString());
		}
		if (!TextUtils.isEmpty(mEditCity.getText())) {
			audit.setCity(mEditCity.getText().toString());
		}
		if (!TextUtils.isEmpty(mEditState.getText())) {
			audit.setState(mEditState.getText().toString());
		}
		if (!TextUtils.isEmpty(mEditZip.getText())) {
			audit.setZip(mEditZip.getText().toString());
		}
		Log.d(LOG_TAG, audit.toString());

		if (TextUtils.isEmpty(auditId)) {

			Log.d(LOG_TAG, "Adding new audit");

			this.createLocalAudit();
		} else {

			Log.d(LOG_TAG, "Updating existing audit");
			audit.setSyn(0);
			dbHandler.getAuditDao().updateInternalAudit(audit);

			Toast.makeText(getActivity(), "Audit master record updated",
					Toast.LENGTH_LONG).show();
		}

	}

	
	@Override
	public void onStart() {
		super.onStart();
		Log.d(LOG_TAG, "Reloading tab Id: "+ mItem.id);

		if (mItem != null && mItem.id.equals("2")) {
			if(scopeOfWorkListAdapter != null){
				scopeOfWorkListAdapter.clear();
				List<LocalScopeOfWork> works = dbHandler.getScopeOfWorkDao()
						.getScopeOfWorkByAuditId(Long.parseLong(auditId));
				scopeOfWorkListAdapter.addAll(works);
				scopeOfWorkListAdapter.notifyDataSetChanged();
			}
		}
	}

}
