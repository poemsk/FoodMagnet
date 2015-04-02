package com.poepoemyintswe.foodmagnet.utils;

import android.content.Context;
import com.poepoemyintswe.foodmagnet.R;
import com.poepoemyintswe.foodmagnet.model.ErrorResponse;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import timber.log.Timber;

/**
 * Created by poepoe on 7/3/15.
 */
public class RetrofitErrorHandler implements ErrorHandler {

  private final Context ctx;

  public RetrofitErrorHandler(Context ctx) {
    this.ctx = ctx;
    Timber.tag("RetrofitErrorHandler");
  }

  @Override public Throwable handleError(RetrofitError cause) {
    String errorDescription;

    if (cause.isNetworkError()) {
      errorDescription = ctx.getString(R.string.please_check_your_connection);
    } else {
      if (cause.getResponse() == null) {
        errorDescription = ctx.getString(R.string.error_no_response);
      } else {

        // Error message handling - return a simple error to Retrofit handlers..
        try {
          ErrorResponse errorResponse = (ErrorResponse) cause.getBodyAs(ErrorResponse.class);
          errorDescription = errorResponse.error.data.message;
        } catch (Exception ex) {
          try {
            errorDescription = ctx.getString(R.string.please_check_your_connection,
                cause.getResponse().getStatus());
          } catch (Exception ex2) {
            Timber.e("handleError: " + ex2.getLocalizedMessage());
            errorDescription = ctx.getString(R.string.error_unknown);
          }
        }
      }
    }

    return new Exception(errorDescription);
  }
}