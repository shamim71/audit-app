package com.versacomllc.audit;

import static com.versacomllc.audit.utils.Constants.EXTRA_AUDIT_DEFECT_ID;
import static com.versacomllc.audit.utils.Constants.EXTRA_AUDIT_ID;
import static com.versacomllc.audit.utils.Constants.EXTRA_SOW_ID;
import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.versacomllc.audit.adapter.AuditDefectListAdapter;
import com.versacomllc.audit.adapter.EmployeeAutocompleteListAdapter;
import com.versacomllc.audit.adapter.SowTechListAdapter;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.Employee;
import com.versacomllc.audit.data.LocalAudit;
import com.versacomllc.audit.data.LocalAuditDefect;
import com.versacomllc.audit.data.LocalScopeOfWork;
import com.versacomllc.audit.data.LocalScopeOfWorkTech;
import com.versacomllc.audit.dummy.ScopeOfWorkContent;
import com.versacomllc.audit.fragment.DeleteConfirmationDialogFragment;
import com.versacomllc.audit.fragment.InformationDialogFragment;
import com.versacomllc.audit.utils.Constants;

/**
 * A fragment representing a single ScopeOfWork detail screen. This fragment is
 * either contained in a {@link ScopeOfWorkListActivity} in two-pane mode (on
 * tablets) or a {@link ScopeOfWorkDetailActivity} on handsets.
 */
public class ScopeOfWorkDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	private static final String TECH_ADD_CONFIRMATION_DIALOG = "TECH_ADD_CONFIRMATION_DIALOG";

	protected static final String TECH_DELETE_CONFIRMATION_DIALOG = "TECH_DELETE_CONFIRMATION_DIALOG";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private ScopeOfWorkContent.Tab mItem;

	long mScopeOfWorkId = -1;

	long auditId =-1;
	
	DatabaseHandler dbHandler = null;
	AuditDefectListAdapter defectListAdapter;

	private TextView mEditDateOfWork;

	private AutoCompleteTextView autoCompleteTextView;

	private SowTechListAdapter techAdapter;


	private LocalScopeOfWork localScopeOfWork = null;
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ScopeOfWorkDetailFragment() {
	}

	private AuditManagement getAppState() {
		return (AuditManagement) getActivity().getApplication();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = ScopeOfWorkContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}

		dbHandler = new DatabaseHandler(getActivity());

		if(getArguments().containsKey(EXTRA_SOW_ID)){
			mScopeOfWorkId = getArguments().getLong(EXTRA_SOW_ID);
		}
		else{
			if(getAppState().getCurrentSowId() != -1){
				mScopeOfWorkId = getAppState().getCurrentSowId();
			}
		}
		
		if (getArguments().containsKey(EXTRA_AUDIT_ID)) {
			auditId = getArguments().getLong(EXTRA_AUDIT_ID);
		} 
		else {
			if (getAppState().getCurrentAudit() != -1) {
				auditId = getAppState().getCurrentAudit();
			}
		}
		

		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mItem != null && mItem.id.equals("1")) {
			return getMainView(inflater, container, savedInstanceState);
		}
		if (mItem != null && mItem.id.equals("2")) {
			return getDefectListView(inflater, container, savedInstanceState);
		}

		View rootView = inflater.inflate(R.layout.fragment_scopeofwork_detail,
				container, false);
		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.scopeofwork_detail))
					.setText(mItem.content);
		}

		return rootView;
	}

	private View getDefectListView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_internalaudit_detail_defects, container,
				false);

		if (auditId == -1) {
			return rootView;
		}
		if(mScopeOfWorkId == -1){
			Log.d(LOG_TAG, "Scope of work id is empty: "+ mScopeOfWorkId);
			return rootView;
		}

		Button btnAddDefect = (Button) rootView
				.findViewById(R.id.btn_add_defect);
		btnAddDefect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getAppState().setCurrentAuditDefect(-1);
				Intent intent = new Intent(getActivity(),
						AuditDefectListActivity.class);
				intent.putExtra(Constants.EXTRA_SOW_ID, mScopeOfWorkId);
				intent.putExtra(Constants.EXTRA_AUDIT_ID, auditId);
				startActivity(intent);
			}
		});

		List<LocalAuditDefect> localAuditDefects = dbHandler
				.getAuditDefectDao().getAuditDefectBySowId(mScopeOfWorkId);

		ListView listViewDefects = (ListView) rootView
				.findViewById(R.id.lv_defect_list);

		defectListAdapter = new AuditDefectListAdapter(getActivity(),
				R.layout.audit_defect_list_item, localAuditDefects);
		listViewDefects.setAdapter(defectListAdapter);

		listViewDefects.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LocalAuditDefect item = (LocalAuditDefect) parent.getAdapter()
						.getItem(position);

				if (item != null) {
					Log.d(LOG_TAG, "Loading audit defect with internal id: "
							+ item.getLocalId());

					Intent intent = new Intent(getActivity(),
							AuditDefectListActivity.class);
					intent.putExtra(Constants.EXTRA_AUDIT_ID,
							auditId);
					intent.putExtra(EXTRA_SOW_ID,
							item.getSowId());
					intent.putExtra(EXTRA_AUDIT_DEFECT_ID, item.getLocalId());
					startActivity(intent);
				}

			}
		});
		listViewDefects
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
										LocalAuditDefect item = (LocalAuditDefect) parent
												.getAdapter().getItem(position);

										if (item != null) {
											dbHandler.getAuditDefectDao()
													.deleteAuditDefect(
															item.getLocalId());
											AuditDefectListAdapter adapter = (AuditDefectListAdapter) parent
													.getAdapter();
											adapter.clear();
											List<LocalAuditDefect> localAuditDefects = dbHandler
													.getAuditDefectDao()
													.getAuditDefectBySowId(mScopeOfWorkId);
											adapter.addAll(localAuditDefects);
											adapter.notifyDataSetChanged();

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

	private View getMainView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_scopeofwork_detail_main, container, false);

		if(mScopeOfWorkId != -1){
			localScopeOfWork = dbHandler.getScopeOfWorkDao().getScopeOfWorkById(mScopeOfWorkId);
			
			List<LocalScopeOfWorkTech> techs = dbHandler.getScopeOfWorkTechDao().getScopeOfWorkTechBySOWId(mScopeOfWorkId);
			localScopeOfWork.setlWorkTechs(techs);
			
		}
		
		Spinner spWorkList = (Spinner) rootView
				.findViewById(R.id.sp_work_type_list);

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

		// ------------------Date picker-----------------------
		mEditDateOfWork = (EditText) rootView.findViewById(R.id.et_work_date);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		populateSetDate(year, month + 1, day);

		ImageButton btnPicker = (ImageButton) rootView
				.findViewById(R.id.ibtn_work_date);

		btnPicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogFragment newFragment = new SelectDateFragment();
				newFragment.show(getActivity().getSupportFragmentManager(),
						"DatePicker");
			}
		});

		// --------------Tech selection auto complate box--------------

		autoCompleteTextView = (AutoCompleteTextView) rootView
				.findViewById(R.id.sp_employee_list);
		List<Employee> employees = dbHandler.getEmployeeDao().getAllEmployees();

		EmployeeAutocompleteListAdapter employeeAdapter = new EmployeeAutocompleteListAdapter(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				employees);

		autoCompleteTextView.setThreshold(2);
		autoCompleteTextView.setAdapter(employeeAdapter);

		OnItemClickListener techChangeListener = new OnItemClickListener() {

	

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Log.d("AutocompleteContacts", "Position:" + position
						+ " Employee :" + parent.getItemAtPosition(position));
				final Employee item = (Employee) parent
						.getItemAtPosition(position);


				InformationDialogFragment fragment = new InformationDialogFragment() {
					@Override
					protected void onDismissed(DialogInterface dialog) {
						dismiss();
						LocalScopeOfWorkTech tech = new LocalScopeOfWorkTech();
						tech.setSowId(mScopeOfWorkId);
						tech.setSync(0);
						tech.setTechId(item.getqBaseRef());
						tech.setTechName(item.getName());
						tech.setRid("-1");
						techAdapter.add(tech);
						techAdapter.notifyDataSetChanged();
						
						autoCompleteTextView.setText("");
						InputMethodManager imm = (InputMethodManager) getActivity()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								autoCompleteTextView.getWindowToken(), 0);
						autoCompleteTextView.clearFocus();
						
					}
				};
				Bundle args = new Bundle();
				args.putString(
						Constants.REQUEST_TOKEN_KEY,
						getResources().getString(
								R.string.tech_add_confirmation, item.getName()));
				fragment.setArguments(args);

				fragment.show(getFragmentManager(),
						TECH_ADD_CONFIRMATION_DIALOG);

			}
		};
		autoCompleteTextView.setOnItemClickListener(techChangeListener);

		/** Load scope of work technician adapter */
		ListView listViewTechList = (ListView) rootView
				.findViewById(R.id.lv_tech_list);

		List<LocalScopeOfWorkTech> techs = new ArrayList<LocalScopeOfWorkTech>();
		if(localScopeOfWork != null && localScopeOfWork.getlWorkTechs() != null){
			techs = localScopeOfWork.getlWorkTechs();
		}
		techAdapter = new SowTechListAdapter(getActivity(),
				R.layout.sow_tech_list_item, techs);
		listViewTechList.setAdapter(techAdapter);
		listViewTechList
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(final AdapterView<?> parent,
							View view, final int position, long id) {

						DeleteConfirmationDialogFragment fragment = new DeleteConfirmationDialogFragment() {

							@Override
							protected void onDismissed(DialogInterface dialog) {
								dialog.dismiss();

								LocalScopeOfWorkTech dTech = techAdapter.getItem(position);
								if(dTech.getId() != -1){
									dbHandler.getScopeOfWorkTechDao().deleteSOWTech(dTech.getId());
								}
								techAdapter.remove(dTech);
								techAdapter.notifyDataSetChanged();

							}
						};

						fragment.show(getFragmentManager(),
								TECH_DELETE_CONFIRMATION_DIALOG);

						return true;
					}
				});
		//----------------Save button ----------------------------
		Button btnSave = (Button) rootView.findViewById(R.id.btn_save_sow_master);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				localScopeOfWork = getScopeOfWork();
				
				Log.d(LOG_TAG, localScopeOfWork.toString());
				localScopeOfWork.setSync(0);
				
				if(mScopeOfWorkId == -1){
					
					mScopeOfWorkId = dbHandler.getScopeOfWorkDao().addSOW(localScopeOfWork);
					
					 getAppState().setCurrentSowId(mScopeOfWorkId);
					 
				 ////Add sow techs
				 List<LocalScopeOfWorkTech> techs =	techAdapter.getAllItems();
				 for(LocalScopeOfWorkTech t: techs){
					 t.setSowId(mScopeOfWorkId);
					 t.setSync(0);
					 t.setRid("-1");
					dbHandler.getScopeOfWorkTechDao().addSOWTech(t);

				 }
					
					Toast.makeText(getActivity(),
							"Audit SOW is added.", Toast.LENGTH_LONG)
							.show();

					List<LocalScopeOfWork> works = dbHandler.getScopeOfWorkDao()
							.getScopeOfWorkByAuditId(auditId);
					Log.d(LOG_TAG, "item saved "+works.size());
				}
				else{
					
					dbHandler.getScopeOfWorkDao().updateSOW(localScopeOfWork);
					
					////Add/Update technician adapter
					List<LocalScopeOfWorkTech> techs =	techAdapter.getAllItems();
					 for(LocalScopeOfWorkTech t: techs){
						 t.setSowId(mScopeOfWorkId);
						 t.setSync(0);
						 if(t.getId() == 0){
							long tid =  dbHandler.getScopeOfWorkTechDao().addSOWTech(t);
							t.setId(tid);
						 }

						 
					 }
					Toast.makeText(getActivity(),
							"Audit SOW is updated.", Toast.LENGTH_LONG)
							.show();
					
				}
				LocalAudit audit = dbHandler.getAuditDao().getInternalAuditsById(String.valueOf(auditId));
				if(audit != null){
					audit.setSyn(0);
					dbHandler.getAuditDao().updateInternalAudit(audit);					
				}
				


			}
		});		
		
		updateUI(localScopeOfWork, spWorkList);
		
		return rootView;
	}

	private LocalScopeOfWork getScopeOfWork() {

		if (localScopeOfWork == null) {
			localScopeOfWork = new LocalScopeOfWork();
		}
		
		localScopeOfWork.setId(mScopeOfWorkId);
		localScopeOfWork.setAuditId(auditId);
		
		if (!TextUtils.isEmpty(mEditDateOfWork.getText())) {
			localScopeOfWork.setDateOfWork(mEditDateOfWork.getText().toString());
		}
		
		
		return localScopeOfWork;
	}
	private void updateUI(LocalScopeOfWork work,Spinner spWorkList){
		if(work == null){
			return;
		}
		int position = getItemIndex(work.getWorkType(), (ArrayAdapter<?>) spWorkList.getAdapter());
		spWorkList.setSelection(position);
		
		//String empName = work.getTechName();
		//autoCompleteTextView.setText(empName);
		
		try {
			Date auditDate = Constants.US_DATEFORMAT.parse(work.getDateOfWork());
			Calendar cal = Calendar.getInstance();
			cal.setTime(auditDate);
			populateSetDate(cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		

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

	public void populateSetDate(int year, int month, int day) {
		mEditDateOfWork.setText(month + "/" + day + "/" + year);
	}
	@Override
	public void onStart() {
		super.onStart();
		Log.d(LOG_TAG, "Reloading tab Id: "+ mItem.id);
	if (mItem != null && mItem.id.equals("2")) {
		
			//return getDefectListView(inflater, container, savedInstanceState);
			if(defectListAdapter != null){
				defectListAdapter.clear();
				

				List<LocalAuditDefect> localAuditDefects = dbHandler
						.getAuditDefectDao().getAuditDefectBySowId(mScopeOfWorkId);
				defectListAdapter.addAll(localAuditDefects);
				defectListAdapter.notifyDataSetChanged();
			}
		}

	}
}
