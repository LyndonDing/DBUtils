package com.lyndon.dbutils.db.table;

import java.lang.reflect.Field;
import java.util.List;

import android.database.Cursor;
import android.util.Log;

import com.lyndon.dbutils.db.sqlite.ColumnDbType;
import com.lyndon.dbutils.db.sqlite.FinderLazyLoader;
import com.lyndon.dbutils.exception.DbException;

/**
 * Author: wyouflf
 * Date: 13-9-10
 * Time: 下午7:43
 */
public class Finder extends Column {

    private final String valueColumnName;
    private final String targetColumnName;

    /* package */ Finder(Class<?> entityType, Field field) {
        super(entityType, field);

        com.lyndon.dbutils.db.annotation.Finder finder =
                field.getAnnotation(com.lyndon.dbutils.db.annotation.Finder.class);
        this.valueColumnName = finder.valueColumn();
        this.targetColumnName = finder.targetColumn();
    }

    public Class<?> getTargetEntityType() {
        return ColumnUtils.getFinderTargetEntityType(this);
    }

    public String getTargetColumnName() {
        return targetColumnName;
    }

    @Override
    public void setValue2Entity(Object entity, Cursor cursor, int index) {
        Object value = null;
        Class<?> columnType = columnField.getType();
        Object finderValue = TableUtils.getColumnOrId(entity.getClass(), this.valueColumnName).getColumnValue(entity);
        if (columnType.equals(FinderLazyLoader.class)) {
            value = new FinderLazyLoader(this, finderValue);
        } else if (columnType.equals(List.class)) {
            try {
                value = new FinderLazyLoader(this, finderValue).getAllFromDb();
            } catch (DbException e) {
                Log.e(e.getMessage(), e.toString());
            }
        } else {
            try {
                value = new FinderLazyLoader(this, finderValue).getFirstFromDb();
            } catch (DbException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }

        if (setMethod != null) {
            try {
                setMethod.invoke(entity, value);
            } catch (Throwable e) {
                Log.e(e.getMessage(), e.toString());
            }
        } else {
            try {
                this.columnField.setAccessible(true);
                this.columnField.set(entity, value);
            } catch (Throwable e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
    }

    @Override
    public Object getColumnValue(Object entity) {
        return null;
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.TEXT;
    }
}
