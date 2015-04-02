package com.poepoemyintswe.foodmagnet.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by poepoe on 10/17/14.
 */
public class NetworkConnectivityCheck {
  protected Context context;

  public NetworkConnectivityCheck(Context context) {
    this.context = context;
  }

  public static NetworkConnectivityCheck getInstance(Context mContext) {
    return new NetworkConnectivityCheck(mContext);
  }

  public boolean isConnected() {
    ConnectivityManager connectivity =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (connectivity != null) {
      NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null) {
        for (NetworkInfo anInfo : info) {
          if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
