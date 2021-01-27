package com.example.trackmypay.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    public class  DataBaseHelper extends SQLiteOpenHelper {


        private static final String DATABASE_NAME = "shift_database.db";
        private static final int DATABASE_VERSION = 1;
        public static final String ID = "_id";
        public static final String TABLE_NAME = "shifts";
        public static String DATE = "date";
        public static String START_TIME = "start_time";
        public static String END_TIME = "end_time";
        public static String COMMENTS = "comments";
        public static String HOURLY_RATE = "hourly_rate";
        public static String PAID_BREAK_DURATION = "paid_break_duration";
        public static String UNPAID_BREAK_DURATION = "unpaid_break_duration";
        public static String BONUS = "bonus_amount";
        public static String EXPENSES = "expenses_amount";
        public static String IS_COMMISSION = "is_commission";
        public static String SALES_MADE_AMOUNT = "sales_made_amount";
        public static String TARGET_AMOUNT = "target_amount";

        public static DataBaseHelper instance = null;
        public static DataBaseHelper getInstance(Context context)
        {
            if (instance == null)
            {
                instance = new DataBaseHelper(context);
            }
            return instance;
        }

        private DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }



        @Override
        public void onCreate(SQLiteDatabase db) {

            String createQuery = "CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "date INTEGER NOT NULL, " +
                    "start_time INTEGER NOT NULL, " +
                    "end_time INTEGER NOT NULL, " +
                    "hourly_rate REAL NOT NULL, " +
                    "paid_break_duration INTEGER DEFAULT 0, " +
                    "unpaid_break_duration INTEGER DEFAULT 0," +
                    "bonus_amount REAL DEFAULT 0," +
                    "expenses_amount REAL DEFAULT 0," +
                    "comments TEXT," +
                    "is_commission INTEGER DEFAULT 0," +
                    "sales_made_amount INTEGER DEFAULT 0," +
                    "target_amount INTEGER DEFAULT 0)";
            db.execSQL(createQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

