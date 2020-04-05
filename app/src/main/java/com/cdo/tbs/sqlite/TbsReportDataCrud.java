package com.cdo.tbs.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.cdo.tbs.model.Report;
import com.cdo.tbs.model.ReportData;
import com.cdo.tbs.util.CommonHelper;

import java.util.ArrayList;
import java.util.List;

public class TbsReportDataCrud {
    private TbsDbHelper dbHelper;

    public TbsReportDataCrud(Context context) {
        dbHelper = new TbsDbHelper(context);
    }

    public long insertReportData(String date, int unit, int tons, int margin, long idReport){
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TbsContract.TbsReportData.COLUMN_NAME_DATE, date);
        values.put(TbsContract.TbsReportData.COLUMN_NAME_UNIT, unit);
        values.put(TbsContract.TbsReportData.COLUMN_NAME_TONASE, tons);
        values.put(TbsContract.TbsReportData.COLUMN_NAME_MARGIN, margin);
        values.put(TbsContract.TbsReportData.COLUMN_NAME_ID_REPORT, idReport);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TbsContract.TbsReportData.TABLE_NAME, null, values);
        dbHelper.close();

        return newRowId;
    }

    public List<ReportData> getReportDataByIdReport(long idReport){
        List<ReportData> reportDataList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
            BaseColumns._ID,
            TbsContract.TbsReportData.COLUMN_NAME_DATE,
            TbsContract.TbsReportData.COLUMN_NAME_UNIT,
            TbsContract.TbsReportData.COLUMN_NAME_TONASE,
            TbsContract.TbsReportData.COLUMN_NAME_MARGIN,
        };

        String selection = TbsContract.TbsReportData.COLUMN_NAME_ID_REPORT + " = ?";
        String[] selectionArgs = { String.valueOf(idReport) };

        Cursor cursor = db.query(
            TbsContract.TbsReportData.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                    // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        );

        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(TbsContract.TbsReportData._ID));
            String itemDate = cursor.getString(cursor.getColumnIndexOrThrow(TbsContract.TbsReportData.COLUMN_NAME_DATE));
            int itemUnit = cursor.getInt(cursor.getColumnIndexOrThrow(TbsContract.TbsReportData.COLUMN_NAME_UNIT));
            int itemTons = cursor.getInt(cursor.getColumnIndexOrThrow(TbsContract.TbsReportData.COLUMN_NAME_TONASE));
            int itemMargin = cursor.getInt(cursor.getColumnIndexOrThrow(TbsContract.TbsReportData.COLUMN_NAME_MARGIN));
            ReportData reportData = new ReportData(itemId, itemDate, itemUnit, itemTons, itemMargin);
            reportDataList.add(reportData);
        }
        cursor.close();
        dbHelper.close();

        return reportDataList;
    }

    public int updateReportData(ReportData reportData, String date, int unit, int tons, int margin){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TbsContract.TbsReportData.COLUMN_NAME_DATE, date);
        values.put(TbsContract.TbsReportData.COLUMN_NAME_UNIT, unit);
        values.put(TbsContract.TbsReportData.COLUMN_NAME_TONASE, tons);
        values.put(TbsContract.TbsReportData.COLUMN_NAME_MARGIN, margin);

        String selection = TbsContract.TbsReportData._ID + " = ?";
        String[] selectionArgs = { String.valueOf(reportData.getId()) };

        int count = db.update(
                TbsContract.TbsReportData.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        dbHelper.close();

        return count;
    }

    public int deleteReportData(ReportData reportData){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = TbsContract.TbsReportData._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(reportData.getId()) };
        int deletedRows = db.delete(TbsContract.TbsReportData.TABLE_NAME, selection, selectionArgs);
        dbHelper.close();

        return deletedRows;
    }
}
