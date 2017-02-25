package com.ximsfei.rush;

import android.app.Application;

import com.ximsfei.rush.db.DBHelper;
import com.ximsfei.rush.util.SPUtils;

/**
 * Created by ximsfei on 17-2-25.
 */

public class RushApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SPUtils.init(this);
        DBHelper.init(this);
    }
}
