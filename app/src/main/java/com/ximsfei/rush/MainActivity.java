package com.ximsfei.rush;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ximsfei.rush.ui.BaseActivity;
import com.ximsfei.rush.ui.PlayGameActivity;
import com.ximsfei.rush.ui.RankingsActivity;
import com.ximsfei.rush.ui.SettingsActivity;
import com.ximsfei.rush.widget.RushView;

import static com.ximsfei.rush.util.RushConstants.KEY_GAME_MODE_DEFAULT;

/**
 * Created by pengfengwang on 2017/2/23.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button mFourBtn;
    private Button mSixBtn;
    private Button mSettings;
    private Button mRankings;
    private int mScreenWidth;
    private RushView mRushView;
    private ObjectAnimator mAnimator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRushView = (RushView) findViewById(R.id.rush_view);
        mRushView.initSixBorder();
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mFourBtn = (Button) findViewById(R.id.four);
        mFourBtn.setOnClickListener(this);
        mFourBtn.setWidth(mScreenWidth / 2);
        mSixBtn = (Button) findViewById(R.id.six);
        mSixBtn.setOnClickListener(this);
        mSixBtn.setWidth(mScreenWidth / 2);
        mSettings = (Button) findViewById(R.id.settings);
        mSettings.setOnClickListener(this);
        mSettings.setWidth(mScreenWidth / 2);
        mRankings = (Button) findViewById(R.id.ranking);
        mRankings.setOnClickListener(this);
        mRankings.setWidth(mScreenWidth / 2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnim();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAnim();
    }

    private void stopAnim() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    private void startAnim() {
        mAnimator = ObjectAnimator.ofFloat(mRushView, "rotation", 0, 360);
        mAnimator.setDuration(mRushView.getDuration());
        mAnimator.setRepeatCount(Integer.MAX_VALUE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.four:
                Intent intent4 = new Intent(this, PlayGameActivity.class);
                intent4.putExtra(KEY_GAME_MODE_DEFAULT, true);
                startActivity(intent4);
                break;
            case R.id.six:
                Intent intent6 = new Intent(this, PlayGameActivity.class);
                intent6.putExtra(KEY_GAME_MODE_DEFAULT, false);
                startActivity(intent6);
                break;
            case R.id.settings:
                Intent settingIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.ranking:
                Intent rankingIntent = new Intent(this, RankingsActivity.class);
                startActivity(rankingIntent);
        }
    }
}
