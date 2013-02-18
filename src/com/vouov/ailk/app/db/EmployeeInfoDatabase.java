package com.vouov.ailk.app.db;

import android.provider.BaseColumns;

/**
 * User: yuml
 * Date: 13-2-4
 * Time: 下午10:20
 */
public final class EmployeeInfoDatabase {
    private EmployeeInfoDatabase(){};
    public static final class Employee implements BaseColumns{
        private Employee(){}
        public static final String TABLE_NAME = "employee";
        public static final String NAME = "name";
        public static final String ID = "id";
        public static final String ACCOUNT = "account";
        public static final String DEPT_NAME = "dept_name";
        public static final String MOBILE = "mobile";
        public static final String OFFICE_PHONE = "office_phone";
        public static final String HOME_CITY = "home_city";
        public static final String EMAIL = "email";
        public static final String PARENT_EMPLOYEE_ID = "parent_employee_id";
        public static final String PARENT_EMPLOYEE_NAME  = "parent_employee_name";
        public static final String DESCRIPTION  = "description";
        public static final String UPDATE_TIME  = "update_time";
        public static final String IS_FAVORITE = "is_favorite";
        public static final String DEFAULT_SORT_ORDER = "id ASC";
    }
}
