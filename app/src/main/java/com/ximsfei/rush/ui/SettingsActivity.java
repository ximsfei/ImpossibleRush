package com.ximsfei.rush.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.ximsfei.rush.R;
import com.ximsfei.rush.util.RushConstants;
import com.ximsfei.rush.util.SPUtils;

/**
 * Created by ximsfei on 17-2-25.
 */

public class SettingsActivity extends BaseDialogActivity {
    private TextView mSoundText;
    private InterstitialAD iad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initWindow();
        showAD();

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

    private InterstitialAD getIAD() {
        if (iad == null) {
            iad = new InterstitialAD(this, RushConstants.APPID, RushConstants.InterteristalPosID);
        }
        return iad;
    }

    private void showAD() {
        getIAD().setADListener(new AbstractInterstitialADListener() {

            @Override
            public void onNoAD(int arg0) {
                Log.i("AD_DEMO", "LoadInterstitialAd Fail:" + arg0);
            }

            @Override
            public void onADReceive() {
                Log.i("AD_DEMO", "onADReceive");
                iad.show();
            }
        });
        iad.loadAD();
    }
}
