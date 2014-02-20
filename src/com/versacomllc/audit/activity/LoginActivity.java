package com.versacomllc.audit.activity;

import static com.versacomllc.audit.utils.Constants.LOG_TAG;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.versacomllc.audit.R;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.Employee;
import com.versacomllc.audit.model.AuthenticationRequest;
import com.versacomllc.audit.model.AuthenticationResponse;
import com.versacomllc.audit.model.AuthenticationResult;
import com.versacomllc.audit.model.StringResponse;
import com.versacomllc.audit.network.sync.SyncUtils;
import com.versacomllc.audit.spice.GenericPostRequest;
import com.versacomllc.audit.spice.RestCall;
import com.versacomllc.audit.spice.RetrySpiceCallback;
import com.versacomllc.audit.utils.EndPoints;
import com.versacomllc.audit.utils.Utils;

public class LoginActivity extends BaseActivity {

	private EditText etEmailAddress;
	private EditText etPassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		dbHandler = new DatabaseHandler(this);
		
		// Create account, if needed
		SyncUtils.CreateSyncAccount(this);

		initComponents();

		registerActivityFinishSignal();

		verifyExistingLogin();

	}

	private void verifyExistingLogin() {
		AuthenticationResponse response = getApplicationState()
				.getAuthentication();
		if (response != null) {
			processResult();
		}
	}

	private void initComponents() {

		etEmailAddress = (EditText) findViewById(R.id.et_emailAddress);
		etPassword = (EditText) findViewById(R.id.et_Password);

		TextView tvAppName = (TextView) findViewById(R.id.tv_appName);
		Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),
				"fonts/Roboto-Medium.ttf");

		tvAppName.setTypeface(myTypeface);

		etEmailAddress.setText("shamim71@gmail.com");
	}

	public void authenticateUser(View v) {
		final String email = etEmailAddress.getText().toString();
		final String password = etPassword.getText().toString();

		if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
			final String emptyMessage = getString(R.string.login_enter_authentication);
			Toast.makeText(this, emptyMessage, Toast.LENGTH_LONG).show();
			return;
		}

		AuthenticationRequest request = new AuthenticationRequest(email,
				password);

		if (Utils.isOnline(getApplicationContext())) {
			authenticateWithServer(request);
		} else {
			Log.d(LOG_TAG, "Should works from local storage");

			Employee emp = dbHandler.getEmployeeDao().findEmployeeByEmail(
					request.getEmail());
			if (emp != null && password.equals(emp.getPassword())) {
				AuthenticationResponse response = new AuthenticationResponse();

				AuthenticationResult result = new AuthenticationResult();
				result.setEmail(emp.getEmail());
				result.setFirstName(emp.getFirstName());
				result.setLastName(emp.getLastName());
				result.setqBaseRef(emp.getqBaseRef());
				result.setId(emp.getqBaseRef());

				response.setResult(result);

				getApplicationState().saveAuthentication(response);

				processResult();
			}
			else{
				Toast.makeText(LoginActivity.this,
						"Invalid credential or User never logged in while connected with Internet.",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	private void authenticateWithServer(final AuthenticationRequest request) {

		String endPoint = EndPoints.REST_CALL_POST_AUTHENTICATE
				.getSimpleAddress();

		restHelper.execute(
				new GenericPostRequest<AuthenticationResponse>(
						AuthenticationResponse.class, endPoint, request),
				new RetrySpiceCallback<AuthenticationResponse>(this) {

					@Override
					public void onSpiceSuccess(AuthenticationResponse response) {

						Log.d(LOG_TAG, response + "");
						if (response != null) {

							getApplicationState().saveAuthentication(response);

							Employee emp = dbHandler.getEmployeeDao()
									.findEmployeeByEmail(
											response.getResult().getEmail());

							emp.setPassword(request.getPassword());

							dbHandler.getEmployeeDao().update(emp);

							processResult();
						}
					}

					@Override
					public void onSpiceError(
							RestCall<AuthenticationResponse> restCall,
							StringResponse response) {
						Toast.makeText(LoginActivity.this,
								"Error = " + response.getMessage(),
								Toast.LENGTH_LONG).show();
					}

				},
				new com.versacomllc.audit.spice.DefaultProgressIndicatorState(
						getString(R.string.processing)));

	}

	private void processResult() {

		Intent intent = new Intent(getBaseContext(), HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
