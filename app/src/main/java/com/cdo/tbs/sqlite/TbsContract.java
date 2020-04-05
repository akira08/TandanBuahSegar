package com.cdo.tbs.sqlite;

import android.provider.BaseColumns;

public final class TbsContract {
    private TbsContract() {}

    public static class TbsReport implements BaseColumns {
        public static final String TABLE_NAME = "report";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DATE_CREATED = "date_created";
    }

    public static class TbsReportData implements BaseColumns {
        public static final String TABLE_NAME = "reportdata";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_UNIT = "unit";
        public static final String COLUMN_NAME_TONASE = "tonase";
        public static final String COLUMN_NAME_MARGIN = "margin";
        public static final String COLUMN_NAME_ID_REPORT = "id_report";
    }
}