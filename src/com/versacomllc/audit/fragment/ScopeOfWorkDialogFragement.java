package com.versacomllc.audit.fragment;

import static com.versacomllc.audit.utils.Constants.EXTRA_AUDIT_ID;
import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.versacomllc.audit.R;
import com.versacomllc.audit.adapter.EmployeeAutocompleteListAdapter;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.Employee;
import com.versacomllc.audit.data.ScopeOfWork;


public abstract class ScopeOfWorkDialogFragement extends DialogFragment {

	Spinner spWorkList;
	EditText mEditDateOfWork;

	
	AutoCompleteTextView autoCompleteTextView;
	DatabaseHandler dbHandler;
	private ScopeOfWork scopeOfWork = null;
	private long mAid;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);

		dbHandler = new DatabaseHandler(getActivity());
		if (getArguments().containsKey(EXTRA_AUDIT_ID)) {
			String aid = getArguments().getString(EXTRA_AUDIT_ID);
			mAid = Long.parseLong(aid);
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View contentView = inflater.inflate(R.layout.fragment_sow, null);
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(contentView)

				.setTitle(R.string.title_scope_of_work)
				.setPositiveButton(R.string.save,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ScopeOfWork sow = getScopeOfWork();

								Log.d(LOG_TAG, sow.toString());
								
								dbHandler.getScopeOfWorkDao().addSOW(sow);
								
								onSave(sow);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
		Dialog dlg = builder.create();

		spWorkList = (Spinner) contentView.findViewById(R.id.sp_work_type_list);

		List<String> workTypes = dbHandler.getSiteWorkDao().getWorkTypes();
		ArrayAdapter<String> workTypeAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, workTypes);
		workTypeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spWorkList.setAdapter(workTypeAdapter);

		OnItemSelectedListener typeListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Object item = parent.getAdapter().getItem(position);
				getScopeOfWork().setWorkType(item.toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
		spWorkList.setOnItemSelectedListener(typeListener);

		autoCompleteTextView = (AutoCompleteTextView) contentView
				.findViewById(R.id.sp_employee_list);
		List<Employee> employees = dbHandler.getEmployeeDao().getAllEmployees();

		EmployeeAutocompleteListAdapter adapter = new EmployeeAutocompleteListAdapter(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				employees);

		autoCompleteTextView.setThreshold(2);
		autoCompleteTextView.setAdapter(adapter);

		OnItemClickListener techChangeListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Log.d("AutocompleteContacts", "Position:" + position
						+ " Employee :" + parent.getItemAtPosition(position));
				Employee item = (Employee) parent.getItemAtPosition(position);
				getScopeOfWork().setTechName(item.getName());
				getScopeOfWork().setTechId(item.getqBaseRef());
			}
		};
		autoCompleteTextView.setOnItemClickListener(techChangeListener);
		
		mEditDateOfWork = (EditText) contentView
				.findViewById(R.id.et_work_date);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		populateSetDate(year, month + 1, day);

		ImageButton btnPicker = (ImageButton) contentView
				.findViewById(R.id.ibtn_work_date);

		btnPicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogFragment newFragment = new SelectDateFragment();
				newFragment.show(getActivity().getSupportFragmentManager(),
						"DatePicker");
			}
		});

		return dlg;
	}

	public void populateSetDate(int year, int month, int day) {
		mEditDateOfWork.setText(month + "/" + day + "/" + year);
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

	private ScopeOfWork getScopeOfWork() {
		if (scopeOfWork == null) {
			scopeOfWork = new ScopeOfWork();
		}
		if (!TextUtils.isEmpty(mEditDateOfWork.getText())) {
			scopeOfWork.setDateOfWork(mEditDateOfWork.getText().toString());
		}
		scopeOfWork.setAuditId(mAid);
		return scopeOfWork;
	}


	public abstract void onSave(ScopeOfWork work);

}
