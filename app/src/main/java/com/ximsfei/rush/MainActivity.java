package com.ximsfei.rush;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ximsfei.rush.widget.RushView;

public class MainActivity extends AppCompatActivity {

    private RushView mRushView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRushView = (RushView) findViewById(R.id.rush_view);
        ObjectAnimator anim = ObjectAnimator.ofFloat(mRushView, "rotation", mRushView.getRotation(), mRushView.getRotation() + 90);

        anim.setDuration(1000);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
            }
        });

//        anim.start();
    }
}
