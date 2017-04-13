package sawa.android.reader.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mc100 on 2017/4/13.
 */

public enum CacheManager {

    INSTANCE;

    public String cache(String key, String value) {
        SQLiteDatabase database = DataBaseHelper.instance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_NAME_KEY, key);
        values.put(DataBaseHelper.COLUMN_NAME_VALUE, value);
        values.put(DataBaseHelper.COLUMN_NAME_TIME, System.currentTimeMillis());
        return database.insert(DataBaseHelper.TABLE_NAME, null, values) + "";
    }

    public String cache(String key) {

        //创建DatabaseHelper对象
        // 得到一个只读的SQLiteDatabase对象
        SQLiteDatabase database = DataBaseHelper.instance().getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象
        // 第一个参数String：表名
        // 第二个参数String[]:要查询的列名
        // 第三个参数String：查询条件
        // 第四个参数String[]：查询条件的参数
        // 第五个参数String:对查询的结果进行分组
        // 第六个参数String：对分组的结果进行限制
        // 第七个参数String：对查询的结果进行排序
        Cursor cursor = database.query(DataBaseHelper.TABLE_NAME,
                new String[]{DataBaseHelper.COLUMN_NAME_KEY},
                DataBaseHelper.COLUMN_NAME_KEY + "=?",
                new String[]{key},
                null, null, null);
        // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
        while (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_NAME_VALUE));
        }
        return null;
    }

    public boolean clear(String key) {
        SQLiteDatabase database = DataBaseHelper.instance().getWritableDatabase();
        return database.delete(DataBaseHelper.TABLE_NAME, DataBaseHelper.COLUMN_NAME_KEY + "=?", new String[]{key}) > 0;
    }

    public boolean clear() {
        SQLiteDatabase database = DataBaseHelper.instance().getWritableDatabase();
        return database.delete(DataBaseHelper.TABLE_NAME, null, null) > 0;
    }
}
