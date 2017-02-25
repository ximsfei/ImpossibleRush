package com.ximsfei.rush.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ximsfei on 17-2-25.
 */

public class SPUtils {
    private static final String FILE_NAME = "meta-data";
    private static final String KEY_SOUND = "key-sound";
    private static final String KEY_LAST_SCORE = "key-last-score-mode-";

    private static SPUtils sInstance;
    private final Context mApp;
    private final SharedPreferences mPref;
    private final SharedPreferences.Editor mEditor;

    public static void init(Context context) {
        if (sInstance == null) {
            synchronized (SPUtils.class) {
                if (sInstance == null) {
                    sInstance = new SPUtils(context.getApplicationContext());
                }
            }
        }
    }

    public static SPUtils getInstance() {
        return sInstance;
    }

    private SPUtils(Context applicationContext) {
        mApp = applicationContext;
        mPref = mApp.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    public SPUtils setSound(boolean on) {
        mEditor.putBoolean(KEY_SOUND, on);
        return this;
    }

    public boolean getSound() {
        return mPref.getBoolean(KEY_SOUND, true);
    }

    public SPUtils setLastScore(int mode, int lastScore) {
        mEditor.putInt(KEY_LAST_SCORE + mode, lastScore);
        return this;
    }

    public int getLastScore(int mode) {
        return mPref.getInt(KEY_LAST_SCORE + mode, 0);
    }

    public void commitEditor() {
        mEditor.apply();
    }
}
