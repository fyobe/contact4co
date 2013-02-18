package com.vouov.ailk.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import com.vouov.ailk.app.db.EmployeeInfoDatabase.*;

/**
 * User: yuml
 * Date: 13-1-28
 * Time: 上午12:50
 */
public class EmployeeInfoDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ailk_db_helper";
    private static final String DATABASE_NAME = "employee_info";
    private static final int DATABASE_VERSION = 1;

    public EmployeeInfoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "db create");
        // Create the employee table
        db.execSQL("CREATE TABLE " + Employee.TABLE_NAME+ " ("
                + Employee._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Employee.ID + " TEXT,"
                + Employee.NAME + " TEXT,"
                + Employee.ACCOUNT + " TEXT,"
                + Employee.DEPT_NAME + " TEXT,"
                + Employee.MOBILE + " TEXT,"
                + Employee.OFFICE_PHONE + " TEXT,"
                + Employee.HOME_CITY + " TEXT,"
                + Employee.EMAIL + " TEXT,"
                + Employee.PARENT_EMPLOYEE_ID + " TEXT,"
                + Employee.PARENT_EMPLOYEE_NAME + " TEXT,"
                + Employee.DESCRIPTION + " TEXT ,"
                + Employee.IS_FAVORITE + " BOOLEAN,"
                + Employee.UPDATE_TIME + " DATETIME "
                + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "db update");
    }
}
