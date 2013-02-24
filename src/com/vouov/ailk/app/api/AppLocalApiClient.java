package com.vouov.ailk.app.api;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.vouov.ailk.app.db.EmployeeInfoDatabase;
import com.vouov.ailk.app.db.EmployeeInfoDatabaseHelper;
import com.vouov.ailk.app.model.Employee;
import com.vouov.ailk.app.model.User;
import com.vouov.ailk.app.util.DBUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: yuml
 * Date: 13-2-5
 * Time: 下午5:51
 */
public class AppLocalApiClient {
    private static final String TAG = "ailk_local_api_client";
    private final static String PREFERENCES_USER_INFO = "userinfo";

    public static void storeEmployee(Context context, Employee employee) throws IllegalAccessException, InstantiationException {
        List<Employee> data = new ArrayList<Employee>();
        data.add(employee);
        storeEmployees(context, data);
    }

    public static void storeEmployees(Context context, List<Employee> employees) throws InstantiationException, IllegalAccessException {
        SQLiteDatabase db = new EmployeeInfoDatabaseHelper(context).getWritableDatabase();

        //get by employId
        List<String> params = new ArrayList<String>();
        StringBuilder sb = new StringBuilder(EmployeeInfoDatabase.Employee.ID);
        sb.append(" IN (");
        for (Employee employee : employees) {
            if (!params.isEmpty()) sb.append(',');
            sb.append('?');
            params.add(employee.getId());
        }
        sb.append(")");
        Cursor cursor = db.query(EmployeeInfoDatabase.Employee.TABLE_NAME, null,
                sb.toString(), params.toArray(new String[params.size()]), null, null, null, null);
        List<Employee> values = DBUtils.handleCursor(cursor, Employee.class);
        HashMap<String, Employee> employeeMap = new HashMap<String, Employee>();
        if (!values.isEmpty()) {
            for (Employee employee : values) {
                employeeMap.put(employee.getId(), employee);
            }
            values.clear();
        }
        for (Employee employee : employees) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(EmployeeInfoDatabase.Employee.ID, employee.getId());
            contentValues.put(EmployeeInfoDatabase.Employee.NAME, employee.getName());
            contentValues.put(EmployeeInfoDatabase.Employee.ACCOUNT, employee.getAccount());
            contentValues.put(EmployeeInfoDatabase.Employee.DEPT_NAME, employee.getDeptName());
            contentValues.put(EmployeeInfoDatabase.Employee.MOBILE, employee.getMobile());
            contentValues.put(EmployeeInfoDatabase.Employee.OFFICE_PHONE, employee.getOfficePhone());
            contentValues.put(EmployeeInfoDatabase.Employee.HOME_CITY, employee.getHomeCity());
            contentValues.put(EmployeeInfoDatabase.Employee.EMAIL, employee.getEmail());
            contentValues.put(EmployeeInfoDatabase.Employee.PARENT_EMPLOYEE_ID, employee.getParentEmployeeId());
            contentValues.put(EmployeeInfoDatabase.Employee.PARENT_EMPLOYEE_NAME, employee.getParentEmployeeName());
            contentValues.put(EmployeeInfoDatabase.Employee.DESCRIPTION, employee.getDesc());
            contentValues.put(EmployeeInfoDatabase.Employee.UPDATE_TIME, new Date().getTime());

            //if got, update, else insert
            if (!employeeMap.containsKey(employee.getId())) {
                contentValues.put(EmployeeInfoDatabase.Employee.IS_FAVORITE, false);
                long key = db.insertOrThrow(EmployeeInfoDatabase.Employee.TABLE_NAME, null, contentValues);
                employee.set_id(key);
            } else {
                db.update(EmployeeInfoDatabase.Employee.TABLE_NAME, contentValues,
                        EmployeeInfoDatabase.Employee._ID + "=?", new String[]{String.valueOf(employeeMap.get(employee.getId()).get_id())});
            }
        }
        db.close();
    }

    public static void addFavorites(Context context, Employee employee) {
        SQLiteDatabase db = new EmployeeInfoDatabaseHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmployeeInfoDatabase.Employee.IS_FAVORITE, true);
        db.update(EmployeeInfoDatabase.Employee.TABLE_NAME, contentValues,
                EmployeeInfoDatabase.Employee.ID + "=?", new String[]{employee.getId()});
        db.close();
    }

    public static Employee getEmployeeById(Context context, String id) throws InstantiationException, IllegalAccessException {
        SQLiteDatabase db = new EmployeeInfoDatabaseHelper(context).getReadableDatabase();
        Cursor cursor = db.query(EmployeeInfoDatabase.Employee.TABLE_NAME, null,
                EmployeeInfoDatabase.Employee.ID + "=?", new String[]{id}, null, null, null, " 1 ");
        List<Employee> employees = DBUtils.handleCursor(cursor, Employee.class);
        cursor.close();
        db.close();
        return employees.isEmpty() ? null : employees.get(0);
    }

    public static List<Employee> queryFavoriteContacts(Context context, String columnName, String condition, int currentPage) throws Exception {
        SQLiteDatabase db = new EmployeeInfoDatabaseHelper(context).getReadableDatabase();
        String sql = EmployeeInfoDatabase.Employee.IS_FAVORITE + " = 1 ";
        if (condition != null && !"".equals(condition.trim())) {
            sql += " AND " + columnName + " LIKE '%" + condition + "%' ";
            Log.d(TAG, sql);
        }
        Cursor cursor = db.query(EmployeeInfoDatabase.Employee.TABLE_NAME, null,
                sql, null, null, null, null, (currentPage - 1) * 20 + " , 20 ");
        List<Employee> data = DBUtils.handleCursor(cursor, Employee.class);
        return data;
    }

    public static User fetchUser(Context context) {
        User user = null;
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_USER_INFO, Activity.MODE_PRIVATE);
        if (preferences != null) {
            user = new User();
            user.setUserName(preferences.getString("user_name", ""));
            user.setPassword(preferences.getString("password", ""));
            user.setRemember(preferences.getBoolean("remember", false));
            user.setAutoLogin(preferences.getBoolean("auto_login", false));
            user.setLastUpdateTime(new Date(preferences.getLong("last_update_time", 0)));
        }
        return user;
    }

    public static void saveUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_USER_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_name", user.getUserName());
        editor.putString("password", user.getPassword());
        editor.putBoolean("remember", user.isRemember());
        editor.putBoolean("auto_login", user.isAutoLogin());
        editor.putLong("last_update_time", user.getLastUpdateTime().getTime());
        editor.commit();
    }

    public static void testTable(Context context) {
        SQLiteDatabase db = new EmployeeInfoDatabaseHelper(context).getWritableDatabase();
        Cursor cursor = db.query(EmployeeInfoDatabase.Employee.TABLE_NAME, null,
                null, null, null, null, null, null);
        printCursorInfo(cursor);
    }

    public static void printCursorInfo(Cursor c) {
        Log.i(TAG, "*** Cursor Begin *** " + " Results:" + c.getCount() + " Columns: " + c.getColumnCount());

        // Print column names
        String rowHeaders = "|| ";
        for (int i = 0; i < c.getColumnCount(); i++) {

            rowHeaders = rowHeaders.concat(c.getColumnName(i) + " || ");
        }
        Log.i(TAG, "COLUMNS " + rowHeaders);

        // Print records
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            String rowResults = "|| ";
            for (int i = 0; i < c.getColumnCount(); i++) {
                rowResults = rowResults.concat(c.getString(i) + " || ");
            }
            Log.i(TAG, "Row " + c.getPosition() + ": " + rowResults);

            c.moveToNext();
        }
        Log.i(TAG, "*** Cursor End ***");
    }
}
