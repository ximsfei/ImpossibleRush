package com.ximsfei.rush.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.res.ResourcesCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.ximsfei.rush.R;
import com.ximsfei.rush.db.DBHelper;
import com.ximsfei.rush.util.RushConstants;
import com.ximsfei.rush.util.SPUtils;
import com.ximsfei.rush.widget.RushView;

import java.io.IOException;
import java.util.Random;

import static com.ximsfei.rush.util.RushConstants.KEY_GAME_MODE_DEFAULT;

public class PlayGameActivity extends BaseActivity {

    private static final int START_GAME = 0;
    private static final int COUNT_DOWN = 1;
    private int mCountDown = 3;
    private RushView mRushView;
    private ImageView mBall;
    private FrameLayout mBannerContainer;
    private BannerView mBannerView;
    private int mScreenHeight;
    private Random mRandom = new Random();
    private int mIndicator;
    private Button mRefresh;
    private ImageView mCountImage;
    private TextView mTotalText;
    private MediaPlayer mMediaPlayer;
    private ObjectAnimator mAnimator;
    private int mScore = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_GAME:
                    mCountDown = 3;
                    setCountDownImage(mCountDown);
                    mCountImage.setVisibility(View.VISIBLE);
                    sendEmptyMessageDelayed(COUNT_DOWN, 1000);
                    break;
                case COUNT_DOWN:
                    mCountDown--;
                    setCountDownImage(mCountDown);
                    if (mCountDown > 0) {
                        sendEmptyMessageDelayed(COUNT_DOWN, 1000);
                    } else {
                        mCountImage.setVisibility(View.GONE);
                        dropDown();
                    }
            }
        }
    };

    private void setCountDownImage(int index) {
        switch (index) {
            case 3:
                mCountImage.setImageResource(R.drawable.count_down_three);
                break;
            case 2:
                mCountImage.setImageResource(R.drawable.count_down_two);
                break;
            case 1:
                mCountImage.setImageResource(R.drawable.count_down_one);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        boolean defaultMode = getIntent().getBooleanExtra(KEY_GAME_MODE_DEFAULT, true);
        mRushView = (RushView) findViewById(R.id.rush_view);
        if (defaultMode) {
            mRushView.initDefault();
        } else {
            mRushView.initSixBorder();
        }
        mBall = (ImageView) findViewById(R.id.ball);
        mIndicator = mRandom.nextInt(mRushView.getBorderNum());
        mBall.setBackgroundColor(ResourcesCompat.getColor(getResources(), mRushView.getAreaColor(mIndicator), getTheme()));
        mBannerContainer = (FrameLayout) findViewById(R.id.bannerContainer);
        initBanner();
        mCountImage = (ImageView) findViewById(R.id.count);
        mRefresh = (Button) findViewById(R.id.refresh);
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefresh.setVisibility(View.GONE);
                mTotalText.setText(String.valueOf(0));
                restartGame();
            }
        });
        mTotalText = (TextView) findViewById(R.id.total);
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mScore = SPUtils.getInstance().getLastScore(mRushView.getBorderNum());
        mTotalText.setText(String.valueOf(mScore));
        startGame();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        SPUtils.getInstance().setLastScore(mRushView.getBorderNum(), mScore).commitEditor();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void startGame() {
        mHandler.sendEmptyMessage(START_GAME);
    }

    private void restartGame() {
        dropDown();
    }

    private void dropDown() {
        Log.d("pengfeng", "mScreenHeight = " + mScreenHeight);
        Log.d("pengfeng", "mTopValue = " + mRushView.getTopValue());

        int dropHeight = (int) (mScreenHeight - mRushView.getTopValue() - mBannerContainer.getMeasuredHeight());
        Log.d("pengfeng", "mDist = " + dropHeight);
//        int dropHeight = (int) (mScreenHeight - mRushView.getTopValue());
        mBall.setVisibility(View.VISIBLE);
        mAnimator = ObjectAnimator.ofFloat(mBall, "translationY", 0, dropHeight);
        mAnimator.setDuration(mRushView.getDuration());
        mAnimator.setRepeatCount(Integer.MAX_VALUE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
//      animator.setInterpolator(value)
        mAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                boolean hit = mRushView.hit(mIndicator);
                if (hit) {
                    mScore++;
                    mTotalText.setText(String.valueOf(mScore));
                    playPointMusic();
                    mIndicator = mRandom.nextInt(mRushView.getBorderNum());
                    mBall.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                            mRushView.getAreaColor(mIndicator), getTheme()));
                    Log.e("pengfeng", "count = " + mScore);
                    if (mScore == Integer.MAX_VALUE) {
                        saveScore();
                    }
                } else {
                    mBall.setVisibility(View.GONE);
                    mRefresh.setVisibility(View.VISIBLE);
                    if (mScore != 0) {
                        saveScore();
                    }
                    playDieMusic();
                    animation.cancel();
                }

            }
        });

        mAnimator.start();
    }

    private void saveScore() {
        ContentValues values = new ContentValues();
        values.put(DBHelper.SCORE, mScore);
        values.put(DBHelper.MODE, mRushView.getBorderNum());
        DBHelper.get().insert(values);
        mScore = 0;
        SPUtils.getInstance().setLastScore(mRushView.getBorderNum(), 0);
    }

    private void playPointMusic() {
        if (!SPUtils.getInstance().getSound()) {
            return;
        }
        AssetFileDescriptor fileDescriptor;
        try {
            fileDescriptor = getAssets().openFd("rush_point.ogg");
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());

            mMediaPlayer.prepare();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        mMediaPlayer.start();
    }

    private void playDieMusic() {
        if (!SPUtils.getInstance().getSound()) {
            return;
        }
        AssetFileDescriptor fileDescriptor;
        try {
            fileDescriptor = getAssets().openFd("rush_die.ogg");
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());

            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initBanner() {
        mBannerView = new BannerView(this, ADSize.BANNER, RushConstants.APPID, RushConstants.BannerPosID);
        // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
        // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
        mBannerView.setRefresh(30);
        mBannerView.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(int arg0) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + arg0);
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        mBannerContainer.addView(mBannerView);
        mBannerView.loadAD();
    }
}
