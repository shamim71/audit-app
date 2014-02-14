package com.versacomllc.audit;

import static com.versacomllc.audit.utils.Constants.EXTRA_AUDIT_DEFECT_ID;
import static com.versacomllc.audit.utils.Constants.EXTRA_AUDIT_ID;
import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.versacomllc.audit.adapter.DefectAutocompleteListAdapter;
import com.versacomllc.audit.adapter.EmployeeAutocompleteListAdapter;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.Employee;
import com.versacomllc.audit.data.LocalAuditDefect;
import com.versacomllc.audit.data.LocalDefect;
import com.versacomllc.audit.dummy.AuditDefectContent;
import com.versacomllc.audit.utils.FileDataStorageManager;

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
	private EditText mEditTextDefect;
	private EditText mEditTextNote;
	private TextView mTextViewDefectDetails;

	ImageView mImageDefectBefore;
	ImageView mImageDefectAfter;

	String auditId = null;
	long localId = -1;

	static final int REQUEST_IMAGE_CAPTURE_B = 1;
	static final int REQUEST_IMAGE_CAPTURE_A = 2;
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";


	private LocalAuditDefect auditDefect = new LocalAuditDefect();

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AuditDefectDetailFragment() {
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
			mItem = AuditDefectContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}

		if (getArguments().containsKey(EXTRA_AUDIT_ID)) {
			auditId = getArguments().getString(EXTRA_AUDIT_ID);
		} else {
			if (getAppState().getCurrentAudit() != -1) {
				auditId = String.valueOf(getAppState().getCurrentAudit());
			}
		}
		if (getArguments().containsKey(EXTRA_AUDIT_DEFECT_ID)) {
			localId = getArguments().getLong(EXTRA_AUDIT_DEFECT_ID);
		} else {
			if (getAppState().getCurrentAuditDefect() != -1) {
				localId = getAppState().getCurrentAuditDefect();
			}
		}
		auditDefect.setAuditId(auditId);
		auditDefect.setLocalId(localId);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mItem != null && mItem.id.equals("1")) {
			return getDefectMasterView(inflater, container, savedInstanceState);
		}
		if (mItem != null && mItem.id.equals("2")) {
			return getDefectPictureView(inflater, container, savedInstanceState);
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

	private View getDefectPictureView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater
				.inflate(R.layout.fragment_auditdefect_detail_pictures,
						container, false);
		Button btnTakePhotosBefore = (Button) rootView
				.findViewById(R.id.btn_take_defect_photo_b);
		btnTakePhotosBefore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startIntent(REQUEST_IMAGE_CAPTURE_B);

			}
		});
		Button btnTakePhotosAfter = (Button) rootView
				.findViewById(R.id.btn_take_defect_photo_a);
		btnTakePhotosAfter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startIntent(REQUEST_IMAGE_CAPTURE_A);

			}
		});

		mImageDefectBefore = (ImageView) rootView
				.findViewById(R.id.iv_defect_photo_b);
		mImageDefectAfter = (ImageView) rootView
				.findViewById(R.id.iv_defect_photo_a);

		if (localId != -1) {
			LocalAuditDefect existing = dbHandler.getAuditDefectDao()
					.getAuditDefectByLocalId(localId);
			if (existing != null) {
				auditDefect = existing;
				
				loadImageFromFile(mImageDefectBefore, auditDefect.getDefectPicBefore());
				
				loadImageFromFile(mImageDefectAfter, auditDefect.getDefectPicAfter());

			}
		}
		return rootView;
	}
	private void loadImageFromFile(ImageView imgView,String path){
		if(TextUtils.isEmpty(path)){
			return;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		imgView.setImageBitmap(bitmap);
		imgView.setVisibility(View.VISIBLE);
	}

	private void startIntent(int code) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		if (takePictureIntent
				.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, code);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE_B) {

			if (data.getExtras() != null
					&& data.getExtras().get("data") != null) {
				Bitmap photo = (Bitmap) data.getExtras().get("data");

				Log.i(LOG_TAG, "Image captured before: ");

				mImageDefectBefore.setDrawingCacheEnabled(true);
				mImageDefectBefore.setImageBitmap(photo);
				mImageDefectBefore.setVisibility(View.VISIBLE);
				final String path = saveImage(mImageDefectBefore);
				LocalAuditDefect auditDefect =	dbHandler.getAuditDefectDao().getAuditDefectByLocalId(localId);
				final String previous = auditDefect.getDefectPicBefore();
				if(!TextUtils.isEmpty(previous)){
					File f = new File(previous);
					f.delete();
				}

				
				auditDefect.setDefectPicBefore(path);
				dbHandler.getAuditDefectDao().updateAuditDefect(auditDefect);
			}
		}

		if (requestCode == REQUEST_IMAGE_CAPTURE_A) {
			if (data.getExtras() != null
					&& data.getExtras().get("data") != null) {
				Bitmap photo = (Bitmap) data.getExtras().get("data");

				Log.i(LOG_TAG, "Image captured after : ");

				mImageDefectAfter.setDrawingCacheEnabled(true);
				mImageDefectAfter.setImageBitmap(photo);
				mImageDefectAfter.setVisibility(View.VISIBLE);
				final String path = saveImage(mImageDefectAfter);
				
				LocalAuditDefect auditDefect =	dbHandler.getAuditDefectDao().getAuditDefectByLocalId(localId);
				final String previous = auditDefect.getDefectPicAfter();
				if(!TextUtils.isEmpty(previous)){
					File f = new File(previous);
					f.delete();
				}
				
				auditDefect.setDefectPicAfter(path);
				dbHandler.getAuditDefectDao().updateAuditDefect(auditDefect);
			}

		}

	}

	private String saveImage(ImageView v) {

		v.setDrawingCacheEnabled(true);

		// this is the important code :)
		// Without it the view will have a dimension of 0,0 and the bitmap will
		// be null
		v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

		v.buildDrawingCache(true);

		Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
		v.setDrawingCacheEnabled(false);

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_" + localId
				+ "" + JPEG_FILE_SUFFIX;
		File imageF = FileDataStorageManager.createFile(getActivity(),
				imageFileName);

		Log.d(LOG_TAG, "file name: " + imageF.getAbsolutePath());

		final String path = imageF.getAbsolutePath();

		OutputStream oStream = null;
		try {
			oStream = new FileOutputStream(imageF);
			b.compress(Bitmap.CompressFormat.PNG, 100, oStream);
			oStream.flush();
			oStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oStream != null) {
				try {
					oStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	
		return path;
	}

	private View getDefectMasterView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(
				R.layout.fragment_auditdefect_detail_main, container, false);

		mEditTextDefect = (EditText) rootView
				.findViewById(R.id.et_defect_count);
		mEditTextNote = (EditText) rootView.findViewById(R.id.et_defect_notes);

		mTextViewDefectDetails = (TextView) rootView
				.findViewById(R.id.tv_defect_details);

		autoCompleteTextView = (AutoCompleteTextView) rootView
				.findViewById(R.id.atv_defect_list);
		List<LocalDefect> defects = dbHandler.getDefectDao().getAllDefects();

		final DefectAutocompleteListAdapter adapter = new DefectAutocompleteListAdapter(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				defects);

		autoCompleteTextView.setThreshold(3);
		autoCompleteTextView.setAdapter(adapter);
		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				LocalDefect defect = adapter.getItem(position);

				autoCompleteTextView.setText(defect.getCode());
				mTextViewDefectDetails.setText("Severity: ["
						+ defect.getSeverity() + "], Description: "
						+ defect.getDescription());
				mTextViewDefectDetails.setVisibility(View.VISIBLE);

				auditDefect.setDefectCode(defect.getCode());
				auditDefect.setDefectDescription(defect.getDescription());
				auditDefect.setDefectSeverity(defect.getSeverity());
				auditDefect.setDefectId(defect.getId());

			}
		});

		mATextViewTech = (AutoCompleteTextView) rootView
				.findViewById(R.id.atv_responsible_tech);
		List<Employee> employess = dbHandler.getEmployeeDao().getAllEmployees();

		final EmployeeAutocompleteListAdapter employeeAutocompleteListAdapter = new EmployeeAutocompleteListAdapter(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				employess);

		mATextViewTech.setThreshold(2);
		mATextViewTech.setAdapter(employeeAutocompleteListAdapter);
		mATextViewTech.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Employee employee = employeeAutocompleteListAdapter
						.getItem(position);
				auditDefect.setTechId(employee.getqBaseRef());
				auditDefect.setTechName(employee.getName());

			}
		});
		Button btnSave = (Button) rootView.findViewById(R.id.btn_save_defect);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				inputData();

				if (localId != -1) {
					dbHandler.getAuditDefectDao()
							.updateAuditDefect(auditDefect);
					Toast.makeText(getActivity(),
							"Audit defect record updated", Toast.LENGTH_LONG)
							.show();
				} else {
					long id = dbHandler.getAuditDefectDao().addAuditDefect(
							auditDefect);
					Toast.makeText(getActivity(), "Audit defect record added",
							Toast.LENGTH_LONG).show();
					localId = id;
					getAppState().setCurrentAuditDefect(localId);
				}

			}
		});

		if (localId != -1) {
			LocalAuditDefect existing = dbHandler.getAuditDefectDao()
					.getAuditDefectByLocalId(localId);
			if (existing != null) {
				auditDefect = existing;
				renderAuditDefect(auditDefect);
			}
		}
		return rootView;
	}

	private void renderAuditDefect(LocalAuditDefect defect) {

		autoCompleteTextView.setText(defect.getDefectCode());

		mTextViewDefectDetails.setText("Severity: ["
				+ defect.getDefectSeverity() + "], Description: "
				+ defect.getDefectDescription());
		mTextViewDefectDetails.setVisibility(View.VISIBLE);

		mEditTextDefect.setText(defect.getCount());
		mEditTextNote.setText(defect.getNote());
		mATextViewTech.setText(defect.getTechName());

	}

	private void inputData() {
		if (!TextUtils.isEmpty(mEditTextDefect.getText())) {
			auditDefect.setCount(mEditTextDefect.getText().toString());
		}
		auditDefect.setNote(mEditTextNote.getText().toString());
		auditDefect.setAuditId(auditId);

	}
}
