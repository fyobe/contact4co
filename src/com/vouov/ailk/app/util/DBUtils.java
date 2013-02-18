package com.vouov.ailk.app.util;

import android.database.Cursor;
import com.vouov.ailk.app.db.Column;

import java.lang.reflect.Field;
import java.util.*;

/**
 * User: yuml
 * Date: 13-2-18
 * Time: 下午10:56
 */
public class DBUtils {
    private static void setObjectValue(Object obj, Field field, Object value) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        if (!accessible) field.setAccessible(true);
        field.set(obj, value);
        if (!accessible) field.setAccessible(false);
    }

    public static <T> List<T> handleCursor(Cursor cursor, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        List<T> data = new ArrayList<T>();
        cursor.moveToFirst();
        if (cursor.isAfterLast() == false) {
            String[] columnNames = cursor.getColumnNames();
            HashMap<String, Integer> indexColumns = new HashMap<String, Integer>();
            for (String columnName : columnNames) {
                indexColumns.put(columnName, cursor.getColumnIndex(columnName));
            }

            HashMap<Integer, Field> columnInfoMap = new HashMap<Integer, Field>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnKey = field.getName();
                if (columnAnnotation != null) {
                    columnKey = columnAnnotation.value();
                }
                if (!indexColumns.containsKey(columnKey)) continue;
                columnInfoMap.put(indexColumns.get(columnKey), field);
            }
            indexColumns.clear();
            indexColumns = null;
            while (cursor.isAfterLast() == false) {
                T value = clazz.newInstance();
                for (Integer index : columnInfoMap.keySet()) {
                    Field field = columnInfoMap.get(index);
                    Class fieldType = field.getType();
                    if (fieldType == String.class) {
                        setObjectValue(value, field, cursor.getString(index));
                    } else if (fieldType == int.class) {
                        setObjectValue(value, field, cursor.getInt(index));
                    } else if (fieldType == long.class) {
                        setObjectValue(value, field, cursor.getLong(index));
                    } else if (fieldType == boolean.class) {
                        setObjectValue(value, field, cursor.getShort(index) != 0);
                    } else if (fieldType == float.class) {
                        setObjectValue(value, field, cursor.getFloat(index));
                    } else if (fieldType == double.class) {
                        setObjectValue(value, field, cursor.getDouble(index));
                    } else if (fieldType == Date.class) {
                        setObjectValue(value, field, new Date(cursor.getLong(index)));
                    } else if (fieldType == short.class) {
                        setObjectValue(value, field, cursor.getShort(index));
                    } else if (fieldType == byte[].class) {
                        setObjectValue(value, field, cursor.getBlob(index));
                    } else {
                        throw new IllegalArgumentException("Unsupported field type for column: " + fieldType.getName());
                    }
                }
                data.add(value);
                cursor.moveToNext();
            }
        }
        return data;
    }
}
