package com.anatoliy.gyrospecks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.anatoliy.gyrospecks.model.DbResponse;

import java.util.ArrayList;

/**
 * Date: 20.04.17
 * Time: 16:20
 *
 * @author anatoliy
 */
public class ResultsDataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ResponseWords.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_WORDS_ENTRIES =
            "CREATE TABLE " + ResultsDataBase.TABLE_NAME + " (" +
                    ResultsDataBase._ID + " INTEGER PRIMARY KEY, " +
                    ResultsDataBase.NAME + TEXT_TYPE + "," +
                    ResultsDataBase.DATE + TEXT_TYPE + "," +
                    ResultsDataBase.SPENT_TIME + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_WORDS_ENTRIES =
            "DROP TABLE IF EXISTS " + ResultsDataBase.TABLE_NAME;

    public ResultsDataBaseHelper(@NonNull final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public final void onCreate(@NonNull final SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_WORDS_ENTRIES);
    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion
            , final int newVersion) {
        db.execSQL(SQL_DELETE_WORDS_ENTRIES);
        onCreate(db);
    }


    public void writeResultToDb(final String name, final String date, final String result) {
        SQLiteDatabase sqLiteDatabase = null;

        try {
            sqLiteDatabase= this.getWritableDatabase();
            final ContentValues values = new ContentValues();

            values.put(ResultsDataBase.NAME, name);
            values.put(ResultsDataBase.DATE, date);
            values.put(ResultsDataBase.SPENT_TIME, result);
            sqLiteDatabase.insert(ResultsDataBase.TABLE_NAME, null, values);
        } finally {
            if (null != sqLiteDatabase) {
                sqLiteDatabase.close();
            }
        }
    }

    public ArrayList<Parcelable> readAllResultssDb() {
        SQLiteDatabase sqLiteDatabase = null;
        final ArrayList<Parcelable> result = new ArrayList<>();

        try {
            sqLiteDatabase = this.getReadableDatabase();

            Cursor cursor = null;
            try {
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                        + ResultsDataBase.TABLE_NAME + " ORDER BY "
                        + ResultsDataBase.SPENT_TIME + " ASC", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    final String name
                            = cursor.getString(cursor.getColumnIndex(ResultsDataBase.NAME));
                    final String date
                            = cursor.getString(cursor.getColumnIndex(ResultsDataBase.DATE));
                    final String spentTime
                            = cursor.getString(cursor.getColumnIndex(ResultsDataBase.SPENT_TIME));

                    final DbResponse dbResponse = new DbResponse(name, date, spentTime);
                    result.add(dbResponse);
                    cursor.moveToNext();
                }
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        } finally {
            if (null != sqLiteDatabase) {
                sqLiteDatabase.close();
            }
        }

        return result;
    }
}
