package com.ximsfei.rush;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ximsfei.rush.widget.RushView;

import java.util.Random;

import static com.ximsfei.rush.util.RushConstants.KEY_GAME_MODE_DEFAULT;

public class PlayGameActivity extends AppCompatActivity {

    private RushView mRushView;
    private ImageView mBall;
    private Button mStartBtn;
    private int mScreenHeight;
    private Random mRandom = new Random();
    private int mIndicator;

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
        mStartBtn = (Button) findViewById(R.id.btn);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDown();
            }
        });
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void dropDown() {
        Log.d("pengfeng", "mScreenHeight = " + mScreenHeight);
        Log.d("pengfeng", "mTopValue = " + mRushView.getTopValue());
        Log.d("pengfeng", "mDist = " + (mScreenHeight - mRushView.getTopValue()));

        ObjectAnimator animator = ObjectAnimator.ofFloat(mBall, "translationY",
                0, (int)(mScreenHeight - mRushView.getTopValue()));
        animator.setDuration(3000);
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
                    mIndicator = mRandom.nextInt(mRushView.getBorderNum());
                    mBall.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                            mRushView.getAreaColor(mIndicator), getTheme()));
                    Log.e("pengfeng", "count = " +count);
                } else {
                    animation.cancel();
                }

            }
        });

        animator.start();
    }
}
