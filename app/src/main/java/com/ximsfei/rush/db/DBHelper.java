package com.ximsfei.rush.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.ximsfei.rush.widget.RushView.DEFAULT_BORDER_NUM;

/**
 * Created by ximsfei on 17-2-25.
 */

public class DBHelper {
    public static class Score {
        int mMode = DEFAULT_BORDER_NUM;
        int mScore = 0;

        @Override
        public String toString() {
            return mMode + " COLORS: " + mScore;
        }
    }
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "rush.db";
    public static final String TABLE_NAME = "ranking_table";
    public static final String RANKING_ID = "_id";
    public static final String SCORE = "score";
    public static final String MODE = "mode";
    private static volatile DBHelper sInstance;
    private final Context mContext;
    private final DatabaseHelper mDbHelper;
    private final SQLiteDatabase mDb;

    private DBHelper(Context context) {
        mContext = context;
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public static void init(Context context) {
        if (sInstance == null) {
            synchronized (DBHelper.class) {
                if (sInstance == null) {
                    sInstance = new DBHelper(context.getApplicationContext());
                }
            }
        }
    }

    public static DBHelper get() {
        return sInstance;
    }

    public long insert(ContentValues values) {
        return mDb.insert(TABLE_NAME, null, values);
    }

    public List<Score> queryTop() {
        Cursor c = mDb.query(TABLE_NAME, new String[] {MODE, SCORE}, null, null, null, null, SCORE + " DESC");
        List<Score> scores = new ArrayList<>();
        try {
            if (c != null && c.getCount() > 0) {
                c.moveToPosition(-1);
                int index = 0;
                while (c.moveToNext() && index < 8) {
                    Score score = new Score();
                    score.mMode = c.getInt(c.getColumnIndex(MODE));
                    score.mScore = c.getInt(c.getColumnIndex(SCORE));
                    if (score.mScore > 0) {
                        scores.add(score);
                    }
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return scores;
    }

    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + TABLE_NAME
                    + "(" + RANKING_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MODE + " INTEGER DEFAULT " + DEFAULT_BORDER_NUM + ", "
                    + SCORE + " INTEGER)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

}
