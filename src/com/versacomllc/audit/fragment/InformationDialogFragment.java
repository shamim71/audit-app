package com.versacomllc.audit.fragment;

import com.versacomllc.audit.R;
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
public abstract class InformationDialogFragment extends DialogFragment {

  @Override
  @SuppressLint("NewApi")
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    String message = getArguments().getString(Constants.REQUEST_TOKEN_KEY);

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder
        .setTitle(R.string.info_dialog_title)
        /* .setMessage(R.string.deactivate_confirmation) */
        .setMessage(message)
        .setPositiveButton(R.string.button_ok,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {

                onDismissed(dialog);

              }
            });

    builder.setInverseBackgroundForced(true);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
      builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
          onDismissed(dialog);
        }
      });
    }

    return builder.create();
  }

  protected abstract void onDismissed(DialogInterface dialog);

}
