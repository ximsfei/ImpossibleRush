package com.ximsfei.rush.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ximsfei.rush.R;

/**
 * Created by ximsfei on 17-2-25.
 */

public class BaseDialogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    protected void initWindow() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (dm.widthPixels * 0.7);
        p.height = (int) (dm.heightPixels * 0.5);
        getWindow().setAttributes(p);
    }
}
