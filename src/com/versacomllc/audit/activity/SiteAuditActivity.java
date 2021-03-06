package com.versacomllc.audit.activity;

import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.versacomllc.audit.R;
import com.versacomllc.audit.adapter.SimpleDropDownListAdapter;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.LocalAudit;
import com.versacomllc.audit.data.LocalCustomer;
import com.versacomllc.audit.model.Customer;


public class SiteAuditActivity extends BaseActivity implements OnItemSelectedListener {

	ArrayAdapter<Customer> adapter = null;
	EditText mEditSiteId;
	EditText mEditAuditHour;
	EditText mEditAuditDate;
	
	LocalAudit audit = new LocalAudit();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site_audit);
		setTitle(getString(R.string.app_name));
		
		dbHandler = new DatabaseHandler(this);
		
		this.initComponents();
		
		this.initCustomerData();
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		populateSetDate(year, month+1, day);
		
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
		
		populateSpinner(R.id.sp_auditType, R.array.array_audit_type, typeListener);
		populateSpinner(R.id.sp_auditStatus, R.array.array_audit_status, statusListener);
		
		TextView auditor =	(TextView)findViewById(R.id.tv_auditor);
		final String auditUser = " Audited by: "+ currentUser().getFirstName() + " "+ currentUser().getLastName();
		auditor.setText(auditUser);
		
		audit.setAuditedByEmployee(currentUser().getqBaseRef());
		audit.setAuditedBy(currentUser().getFirstName() + " "+ currentUser().getLastName());
		
	}
	private void initComponents(){
		mEditAuditDate = (EditText)findViewById(R.id.editText1);
		mEditAuditHour = (EditText)findViewById(R.id.et_auditHour);
		mEditSiteId = (EditText)findViewById(R.id.et_customerSiteID);
	}
	public void saveAuditMaster(View view){
		

		if(!TextUtils.isEmpty(mEditAuditHour.getText())){
			audit.setAuditHour(mEditAuditHour.getText().toString());
		}
		if(!TextUtils.isEmpty(mEditAuditDate.getText())){
			audit.setAuditDate(mEditAuditDate.getText().toString());
		}
		if(!TextUtils.isEmpty(mEditSiteId.getText())){
			audit.setSiteId(mEditSiteId.getText().toString());
		}
		Log.d(LOG_TAG, audit.toString());
			
		
		this.createLocalAudit();
		
	}
    public void selectDate(View view) {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }
    public void populateSetDate(int year, int month, int day) {
    	mEditAuditDate.setText(month+"/"+day+"/"+year);
    }
    @SuppressLint("ValidFragment")
	public  class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    	@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    	}
    	
    	public void onDateSet(DatePicker view, int yy, int mm, int dd) {
    		populateSetDate(yy, mm+1, dd);
    	}
    }
	private void populateCustomerList(Customer[] objects){
		Spinner spinner = (Spinner) findViewById(R.id.sp_siteList);

		List<Customer> list = Arrays.asList(objects);
		
		adapter = new SimpleDropDownListAdapter(this, android.R.layout.simple_spinner_item, list); 
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}
	private void populateSpinner(int spinnerId, int arrayId, OnItemSelectedListener listener){
		Spinner spinner = (Spinner) findViewById(spinnerId);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        arrayId, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(listener);
	}

	private void initCustomerData(){
		List<LocalCustomer> customers = 	dbHandler.getAllCustomers();
		populateCustomerList(customers.toArray(new Customer [customers.size()]));
	}


	private void createLocalAudit(){
		Log.i(LOG_TAG, "Adding records to internal audit table ");

		dbHandler.getAuditDao().addInternalAudit(audit);
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int position, long id) {
	
		Customer customer =  adapter.getItem(position);

		Log.d(LOG_TAG, customer.getName());
		
		audit.setCustomer(customer.getRid());
		audit.setCustomerName(customer.getName());

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		Log.d(LOG_TAG, "Nothing selected...");
		
	}

}
