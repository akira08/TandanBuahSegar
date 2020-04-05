package com.cdo.tbs.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.cdo.tbs.model.Report;
import com.cdo.tbs.util.CommonHelper;

import java.util.ArrayList;
import java.util.List;

public class TbsReportCrud {
    private TbsDbHelper dbHelper;

    public TbsReportCrud(Context context) {
        dbHelper = new TbsDbHelper(context);
    }

    public long insertReport(String title){
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TbsContract.TbsReport.COLUMN_NAME_TITLE, title);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TbsContract.TbsReport.TABLE_NAME, null, values);
        dbHelper.close();

        return newRowId;
    }

    public List<Report> getReportAll(){
        List<Report> reportList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
            BaseColumns._ID,
            TbsContract.TbsReport.COLUMN_NAME_TITLE,
            TbsContract.TbsReport.COLUMN_NAME_DATE_CREATED
        };

        Cursor cursor = db.query(
            TbsContract.TbsReport.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                    // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        );

        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(TbsContract.TbsReport._ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(TbsContract.TbsReport.COLUMN_NAME_TITLE));
            String dateCreated = CommonHelper.getCurrentDate();
            Report report = new Report(itemId, itemTitle, dateCreated);
            reportList.add(report);
        }
        cursor.close();
        dbHelper.close();

        return reportList;
    }

    public int updateReport(Report report, String title){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TbsContract.TbsReport.COLUMN_NAME_TITLE, title);

        String selection = TbsContract.TbsReport._ID + " = ?";
        String[] selectionArgs = { String.valueOf(report.getId()) };

        int count = db.update(
                TbsContract.TbsReport.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        dbHelper.close();

        return count;
    }

    public int deleteReport(Report report){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = TbsContract.TbsReport._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(report.getId()) };
        int deletedRows = db.delete(TbsContract.TbsReport.TABLE_NAME, selection, selectionArgs);

        String selection1 = TbsContract.TbsReportData.COLUMN_NAME_ID_REPORT + " LIKE ?";
        String[] selectionArgs1 = { String.valueOf(report.getId()) };
        int deletedRows1 = db.delete(TbsContract.TbsReportData.TABLE_NAME, selection1, selectionArgs1);
        dbHelper.close();

        return deletedRows + deletedRows1;
    }
}
