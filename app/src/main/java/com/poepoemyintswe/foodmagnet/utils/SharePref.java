package com.poepoemyintswe.foodmagnet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by poepoe on 20/3/15.
 */
public class SharePref {

  private static final String PREF_RANGE = "pref_range";
  private static final int DEFAULT_RANGE = 100;

  private static SharePref pref;
  protected SharedPreferences mSharedPreferences;
  protected SharedPreferences.Editor mEditor;
  protected Context mContext;

  public SharePref(Context context) {
    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    mEditor = mSharedPreferences.edit();
    this.mContext = context;
  }

  public static synchronized SharePref getInstance(Context mContext) {
    if (pref == null) {
      pref = new SharePref(mContext);
    }
    return pref;
  }

  /**
   * Set range passed via argument
   *
   * @param range location argument
   */
  public void setRange(int range) {
    mEditor.putInt(PREF_RANGE, range).apply();
  }

  /**
   * Return a default range if the getInt returns null
   *
   * @return the saved range. If the getInt returns null, return default range.
   */

  public int getRange() {
    return mSharedPreferences.getInt(PREF_RANGE, DEFAULT_RANGE);
  }
}
