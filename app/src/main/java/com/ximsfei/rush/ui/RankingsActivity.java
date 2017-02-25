package com.ximsfei.rush.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ximsfei.rush.R;

/**
 * Created by ximsfei on 17-2-25.
 */

public class RankingsActivity extends BaseDialogActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        initWindow();
    }
}
