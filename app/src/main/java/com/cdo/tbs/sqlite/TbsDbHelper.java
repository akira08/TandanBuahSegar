package com.cdo.tbs.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TbsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tbs.db";

    private static final String SQL_CREATE_REPORT =
            "CREATE TABLE " + TbsContract.TbsReport.TABLE_NAME + " (" +
                    TbsContract.TbsReport._ID + " INTEGER PRIMARY KEY," +
                    TbsContract.TbsReport.COLUMN_NAME_TITLE + " TEXT," +
                    TbsContract.TbsReport.COLUMN_NAME_DATE_CREATED + " TEXT)";

    private static final String SQL_CREATE_REPORT_DATA =
            "CREATE TABLE " + TbsContract.TbsReportData.TABLE_NAME + " (" +
                    TbsContract.TbsReportData._ID + " INTEGER PRIMARY KEY," +
                    TbsContract.TbsReportData.COLUMN_NAME_DATE + " INTEGER," +
                    TbsContract.TbsReportData.COLUMN_NAME_UNIT + " INTEGER," +
                    TbsContract.TbsReportData.COLUMN_NAME_TONASE + " INTEGER," +
                    TbsContract.TbsReportData.COLUMN_NAME_MARGIN + " INTEGER," +
                    TbsContract.TbsReportData.COLUMN_NAME_ID_REPORT + " INTEGER," +
                    " FOREIGN KEY ("+ TbsContract.TbsReportData.COLUMN_NAME_ID_REPORT +") REFERENCES "+ TbsContract.TbsReport.TABLE_NAME +"("+ TbsContract.TbsReport._ID +")" +
            ")";

    private static final String SQL_DELETE_REPORT =
            "DROP TABLE IF EXISTS " + TbsContract.TbsReport.TABLE_NAME;

    private static final String SQL_DELETE_REPORT_DATA =
            "DROP TABLE IF EXISTS " + TbsContract.TbsReportData.TABLE_NAME;

    public TbsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_REPORT);
        db.execSQL(SQL_CREATE_REPORT_DATA);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_REPORT);
        db.execSQL(SQL_DELETE_REPORT_DATA);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
