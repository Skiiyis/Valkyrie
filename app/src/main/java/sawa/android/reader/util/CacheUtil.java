package sawa.android.reader.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import sawa.android.reader.db.DataBaseHelper;

/**
 * Created by mc100 on 2017/4/13.
 */

public class CacheUtil {

    public static String cache(String key, String value) {
        SQLiteDatabase database = DataBaseHelper.instance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_NAME_URL_CACHE, key);
        values.put(DataBaseHelper.COLUMN_NAME_CONTENT_CACHE, value);
        values.put(DataBaseHelper.COLUMN_NAME_TIME, System.currentTimeMillis());
        String result = database.insert(DataBaseHelper.TABLE_NAME_CACHE, null, values) + "";
        database.close();
        return result;
    }

    public static String cache(String key) {
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
        Cursor cursor = database.query(DataBaseHelper.TABLE_NAME_CACHE,
                new String[]{DataBaseHelper.COLUMN_NAME_URL_CACHE, DataBaseHelper.COLUMN_NAME_CONTENT_CACHE},
                DataBaseHelper.COLUMN_NAME_URL_CACHE + "=?",
                new String[]{key},
                null, null, null);
        // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
        while (cursor.moveToNext()) {
            String result = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_NAME_CONTENT_CACHE));
            cursor.close();
            database.close();
            return result;
        }
        cursor.close();
        database.close();
        return null;
    }

    public static <T> T cache(String key, Class<T> clazz) {
        String cache = cache(key);
        if (TextUtils.isEmpty(cache)) {
            return null;
        } else {
            return new Gson().fromJson(cache, clazz);
        }
    }

    public static <T> List<T> cacheList(String key, TypeToken token) {
        String cache = cache(key);
        if (TextUtils.isEmpty(cache)) {
            return null;
        } else {
            return new Gson().fromJson(cache, token.getType());
        }
    }

    public static void cacheJson(String key, Object o) {
        cache(key, new Gson().toJson(o));
    }

    public static void cacheJson(Object o) {
        cache(o.getClass().getSimpleName(), new Gson().toJson(o));
    }

    public static boolean clear(String key) {
        SQLiteDatabase database = DataBaseHelper.instance().getWritableDatabase();
        int lines = database.delete(DataBaseHelper.TABLE_NAME_CACHE, DataBaseHelper.COLUMN_NAME_URL_CACHE + "=?", new String[]{key});
        database.close();
        return lines > 0;
    }

    public static boolean clear() {
        SQLiteDatabase database = DataBaseHelper.instance().getWritableDatabase();
        int lines = database.delete(DataBaseHelper.TABLE_NAME_CACHE, null, null);
        database.close();
        return lines > 0;
    }
}
