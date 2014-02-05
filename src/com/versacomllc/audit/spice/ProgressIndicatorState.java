package com.versacomllc.audit.spice;


public interface ProgressIndicatorState {

  String getProgressMessage();

  boolean showProgress();

  boolean hideProgressOnFinished();

}
