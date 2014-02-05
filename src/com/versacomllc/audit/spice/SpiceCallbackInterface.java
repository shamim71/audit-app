package com.versacomllc.audit.spice;

import com.versacomllc.audit.model.StringResponse;



/**
 * Interface to be implemented in activities.
 * 
 * 
 * @param <T>
 */
public interface SpiceCallbackInterface<T> {

  void onSpiceSuccess(T response);

  void onSpiceError(RestCall<T> restCall, int reason, Throwable exception);

  void onSpiceError(RestCall<T> restCall, StringResponse response);

}
