package com.example.sleepytimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SleepDataDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sleep_data.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SLEEP_DATA = "sleep_data";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_MINUTES_SLEPT = "minutes_slept";

    public SleepDataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_SLEEP_DATA +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_MINUTES_SLEPT + " INTEGER)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }

    public void insert(Context context, String date, int minutesSlept) {
        SleepDataDbHelper dbHelper = new SleepDataDbHelper(context);

        // Insert sleep data for a specific date and hours slept
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_MINUTES_SLEPT, minutesSlept);
        db.insert(TABLE_SLEEP_DATA, null, values);
    }

    public void deleteAllData(Context context) {
        SleepDataDbHelper dbHelper = new SleepDataDbHelper(context);

        // Delete all sleep data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_SLEEP_DATA, null, null);
        String resetAutoIncrementSql = "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_SLEEP_DATA + "'";
        db.execSQL(resetAutoIncrementSql);

        db.close();
    }

    public boolean isDateExists(Context context, String date) {
        SleepDataDbHelper dbHelper = new SleepDataDbHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns to be queried
        String[] projection = {COLUMN_DATE};

        // Define the selection criteria
        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = {date};

        // Execute the query
        Cursor cursor = db.query(TABLE_SLEEP_DATA, projection, selection, selectionArgs, null, null, null);

        boolean exists = cursor.moveToFirst();

        // Close the cursor and database
        cursor.close();
        db.close();

        return exists;
    }

    public int countEntries(Context context, String currentDate, String sevenDaysAgoDate) {
        SleepDataDbHelper dbHelper = new SleepDataDbHelper(context);
        String query = "SELECT COUNT(" + COLUMN_MINUTES_SLEPT + ") FROM " + TABLE_SLEEP_DATA +
                " WHERE " + COLUMN_DATE + " BETWEEN '" + sevenDaysAgoDate + "' AND '" + currentDate + "'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int timeCount = 0;
        if (cursor.moveToFirst()) {
            timeCount = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return timeCount;
    }

    public int getMinSlept(Context context, String currentDate, String sevenDaysAgoDate){
        SleepDataDbHelper dbHelper = new SleepDataDbHelper(context);
        // Query the database to retrieve the sleep data between the two dates
        String query = "SELECT SUM(" + COLUMN_MINUTES_SLEPT + ") FROM " + TABLE_SLEEP_DATA +
                " WHERE " + COLUMN_DATE + " BETWEEN '" + sevenDaysAgoDate + "' AND '" + currentDate + "'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int totalHoursSlept = 0;
        if (cursor.moveToFirst()) {
            totalHoursSlept = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return totalHoursSlept;
    }

    public void printAllData(Context context) {
        SleepDataDbHelper dbHelper = new SleepDataDbHelper(context);

        // Retrieve all sleep data
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SLEEP_DATA, null, null, null, null, null, null);

        // Iterate over the cursor to print the data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                int hoursSlept = cursor.getInt(cursor.getColumnIndex(COLUMN_MINUTES_SLEPT));

                // Print the sleep data
                System.out.println("ID: " + id + ", Date: " + date + ", Minutes Slept: " + hoursSlept);
            } while (cursor.moveToNext());
        }

        // Close the cursor and the database
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    public void updateHoursSlept(Context context, String date, int newMinutesSlept) {
        SleepDataDbHelper dbHelper = new SleepDataDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MINUTES_SLEPT, newMinutesSlept);

        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = { date };

        db.update(TABLE_SLEEP_DATA, values, selection, selectionArgs);
        db.close();
    }

    public long getLatestItemId(Context context) {
        SleepDataDbHelper dbHelper = new SleepDataDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_SLEEP_DATA +
                " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);
        long latestItemId = 0;

        if (cursor.moveToFirst()) {
            latestItemId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        }

        cursor.close();
        db.close();

        return latestItemId;
    }

}

// Use the totalHoursSlept as needed