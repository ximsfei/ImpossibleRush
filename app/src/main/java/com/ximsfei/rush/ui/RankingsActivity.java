package com.ximsfei.rush.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.ximsfei.rush.R;
import com.ximsfei.rush.db.DBHelper;
import com.ximsfei.rush.util.RushConstants;

import java.util.List;

/**
 * Created by ximsfei on 17-2-25.
 */

public class RankingsActivity extends BaseDialogActivity {

    private ListView mListView;
    private TextView mGlobalText;
    private InterstitialAD iad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        initWindow();
        showAD();

        mGlobalText = (TextView) findViewById(R.id.global);
        mGlobalText.setText("全网最高分: " + Integer.MAX_VALUE);
        mListView = (ListView) findViewById(R.id.ranking_list);
        mListView.setAdapter(new RankingAdapter(DBHelper.get().queryTop()));
    }

    private class RankingAdapter extends BaseAdapter {
        private final List<DBHelper.Score> mScores;

        public RankingAdapter(List<DBHelper.Score> scores) {
            mScores = scores;
        }

        @Override
        public int getCount() {
            return mScores.size();
        }

        @Override
        public DBHelper.Score getItem(int position) {
            return mScores.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) getLayoutInflater().inflate(R.layout.list_item, null);
            tv.setText(getItem(position).toString());
            return tv;
        }
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
