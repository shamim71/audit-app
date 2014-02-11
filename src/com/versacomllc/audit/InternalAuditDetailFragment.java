package com.versacomllc.audit;

import static com.versacomllc.audit.utils.Constants.EXTRA_AUDIT_ID;
import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.LocaleUtils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.versacomllc.audit.activity.LoginActivity;
import com.versacomllc.audit.adapter.SimpleDropDownListAdapter;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.LocalAudit;
import com.versacomllc.audit.data.LocalCustomer;
import com.versacomllc.audit.data.ScopeOfWork;
import com.versacomllc.audit.dummy.DummyContent;
import com.versacomllc.audit.fragment.ScopeOfWorkDialogFragement;
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
	private DummyContent.DummyItem mItem;

	DatabaseHandler dbHandler = null;
	EditText mEditSiteId;
	EditText mEditAuditHour;
	EditText mEditAuditDate;
	Spinner auditTypeSpinner;
	Spinner auditStatusSpinner;
	Spinner customerSpinner;
	TextView mTextAuditor;
	
	LocalAudit audit = new LocalAudit();

	String auditId = null;
	SimpleDropDownListAdapter customerAdapter;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public InternalAuditDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dbHandler = new DatabaseHandler(getActivity());

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
		if (getArguments().containsKey(EXTRA_AUDIT_ID)) {
			auditId = getArguments().getString(EXTRA_AUDIT_ID);
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
	private View getSOWView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_internalaudit_detail_sow, container, false);
		
		Button btnSOW = (Button) rootView.findViewById(R.id.btn_Add_SOW);
		btnSOW.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment dialog = new ScopeOfWorkDialogFragement() {
					
					@Override
					public void onSave(ScopeOfWork work) {
						Toast.makeText(getActivity(), "Scope of work added",
								Toast.LENGTH_LONG).show();
						
					}
				};
				Bundle arguments = new Bundle();
				arguments.putString(
						EXTRA_AUDIT_ID,
						auditId);
				dialog.setArguments(arguments);
		        dialog.show(getActivity().getSupportFragmentManager(), "ScopeOfWorkDialogFragement");

				
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
		OnItemSelectedListener statusListener = new OnItemSelectedListener() {

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

		auditTypeSpinner = (Spinner) rootView.findViewById(R.id.sp_auditType);
		auditStatusSpinner = (Spinner) rootView
				.findViewById(R.id.sp_auditStatus);
		customerSpinner = (Spinner) rootView
				.findViewById(R.id.sp_customer_list);

		mEditAuditDate = (EditText) rootView.findViewById(R.id.et_audit_date);
		mEditAuditHour = (EditText) rootView.findViewById(R.id.et_auditHour);
		mEditSiteId = (EditText) rootView.findViewById(R.id.et_customerSiteID);
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
				statusListener);

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

		LocalAudit existingAudit = dbHandler.getAuditDao().getInternalAuditsById(auditId);

		updateAuditMasterUI(existingAudit);

		return rootView;
	}

	private void updateAuditMasterUI(LocalAudit lAudit) {
		if (lAudit == null) {
			return;
		}
		audit = lAudit;
		
		customerSpinner.setSelection(getCustomerIndex(lAudit.getCustomer()));
		mEditSiteId.setText(lAudit.getSiteId());

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
		
		int index = getItemIndex(lAudit.getAuditType(), (ArrayAdapter)auditTypeSpinner.getAdapter());
		auditTypeSpinner.setSelection(index);
		
		int statusIndex = getItemIndex(lAudit.getAuditStatus(), (ArrayAdapter)auditStatusSpinner.getAdapter());
		auditStatusSpinner.setSelection(statusIndex);
		
		mTextAuditor.setText(lAudit.getAuditedBy());

	}
	private int getItemIndex(String value,ArrayAdapter adapter){
		int size = adapter.getCount();
		for (int i = 0; i < size; i++) {
			String  itm = (String) adapter.getItem(i);
			if (itm.equals(value)){
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

		long id = dbHandler.getAuditDao().addInternalAudit(audit);
		
		auditId = String.valueOf(id);
		
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
		Log.d(LOG_TAG, audit.toString());

		if (TextUtils.isEmpty(auditId)) {

			Log.d(LOG_TAG, "Adding new audit");

			this.createLocalAudit();
		} else {

			Log.d(LOG_TAG, "Updating existing audit");
			dbHandler.getAuditDao().updateInternalAudit(audit);

			Toast.makeText(getActivity(), "Audit master record updated",
					Toast.LENGTH_LONG).show();
		}

	}
}
