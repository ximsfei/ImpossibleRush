package com.ximsfei.rush.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ximsfei.rush.R;
import com.ximsfei.rush.util.SPUtils;

/**
 * Created by ximsfei on 17-2-25.
 */

public class SettingsActivity extends BaseDialogActivity {
    private TextView mSoundText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initWindow();

        mSoundText = (TextView) findViewById(R.id.sound);
        mSoundText.setText(SPUtils.getInstance().getSound() ? R.string.sound_on : R.string.sound_off);
        mSoundText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.getInstance().setSound(!SPUtils.getInstance().getSound()).commitEditor();
                mSoundText.setText(SPUtils.getInstance().getSound() ? R.string.sound_on : R.string.sound_off);
            }
        });
    }
}
