package com.ximsfei.rush;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.ximsfei.rush.util.RushConstants.KEY_GAME_MODE_DEFAULT;

/**
 * Created by pengfengwang on 2017/2/23.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mFourBtn;
    private Button mSixBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFourBtn = (Button) findViewById(R.id.four);
        mFourBtn.setOnClickListener(this);
        mSixBtn = (Button) findViewById(R.id.six);
        mSixBtn.setOnClickListener(this);
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
        }
    }
}
