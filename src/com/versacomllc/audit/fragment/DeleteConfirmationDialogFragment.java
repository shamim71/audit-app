package com.versacomllc.audit.fragment;

import java.util.List;

import com.versacomllc.audit.R;
import com.versacomllc.audit.adapter.AuditDefectListAdapter;
import com.versacomllc.audit.data.LocalAuditDefect;
import com.versacomllc.audit.utils.Constants;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Deactivate confirmation dialog fragment
 * 
 * @author shamim
 * 
 */
public abstract class DeleteConfirmationDialogFragment extends DialogFragment {

	@Override
	@SuppressLint("NewApi")
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setMessage(R.string.delete_confirmation_message)
				.setTitle(R.string.delete_confirmation_title)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								onDismissed(dialog);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					dialog.dismiss();
				}
			});
		}

		return builder.create();
	}

	protected abstract void onDismissed(DialogInterface dialog);

}
