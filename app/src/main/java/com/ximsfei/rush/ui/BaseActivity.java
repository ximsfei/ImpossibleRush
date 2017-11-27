package com.ximsfei.rush.ui;

import android.app.Activity;
import android.os.Bundle;

import com.ximsfei.rush.R;

/**
 * Created by ximsfei on 17-2-25.
 */

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setWindowAnimations(R.style.ActivityAnimation);
    }
}
