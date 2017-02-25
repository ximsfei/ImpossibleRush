package com.ximsfei.rush;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ximsfei.rush.util.SPUtils;
import com.ximsfei.rush.widget.RushView;

import java.io.IOException;
import java.util.Random;

import static com.ximsfei.rush.util.RushConstants.KEY_GAME_MODE_DEFAULT;

public class PlayGameActivity extends AppCompatActivity {

    private static final int START_GAME = 0;
    private static final int COUNT_DOWN = 1;
    private int mCountDown = 3;
    private RushView mRushView;
    private ImageView mBall;
    private int mScreenHeight;
    private Random mRandom = new Random();
    private int mIndicator;
    private ImageView mRefresh;
    private ImageView mCountImage;
    private TextView mTotalText;
    private MediaPlayer mMediaPlayer;

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
        mCountImage = (ImageView) findViewById(R.id.count);
        mRefresh = (ImageView) findViewById(R.id.refresh);
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefresh.setVisibility(View.GONE);
                mTotalText.setText(String.valueOf(0));
                resetMusic();
                restartGame();
            }
        });
        mTotalText = (TextView) findViewById(R.id.total);
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        resetMusic();
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
        Log.d("pengfeng", "mDist = " + (mScreenHeight - mRushView.getTopValue()));

        mBall.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBall, "translationY",
                0, (int) (mScreenHeight - mRushView.getTopValue()));
        animator.setDuration(mRushView.getDuration());
        animator.setRepeatCount(Integer.MAX_VALUE);
        animator.setRepeatMode(ValueAnimator.RESTART);
//      animator.setInterpolator(value)
        animator.addListener(new Animator.AnimatorListener() {
            int count = 0;

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
                    count++;
                    mTotalText.setText(String.valueOf(count));
                    playPointMusic();
                    mIndicator = mRandom.nextInt(mRushView.getBorderNum());
                    mBall.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                            mRushView.getAreaColor(mIndicator), getTheme()));
                    Log.e("pengfeng", "count = " + count);
                } else {
                    mBall.setVisibility(View.GONE);
                    mRefresh.setVisibility(View.VISIBLE);
                    playDieMusic();
                    animation.cancel();
                }

            }
        });

        animator.start();
    }

    private void resetMusic() {
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
    }

    private void playPointMusic() {
        if (!SPUtils.getInstance().getSound()) {
            return;
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
}
