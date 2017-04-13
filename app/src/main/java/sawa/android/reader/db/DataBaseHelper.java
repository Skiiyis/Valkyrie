package sawa.android.reader.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sawa.android.reader.global.Application;

/**
 * Created by mc100 on 2017/4/13.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cache"; //数据库名称
    private static final int DB_VERSION = 2; //数据库版本

    public static final String TABLE_NAME_CACHE = "cache"; //缓存表名称
    public static final String COLUMN_NAME_URL_CACHE = "url";
    public static final String COLUMN_NAME_CONTENT_CACHE = "content";

    public static final String TABLE_NAME_FAVOURITE = "favourite"; //收藏表名称
    public static final String COLUMN_NAME_TYPE_FAVOURITE = "type";
    public static final String COLUMN_NAME_ID_FAVOURITE = "content_id";
    public static final String COLUMN_NAME_URL_FAVOURITE = "url";

    public static final String COLUMN_NAME_TIME = "time";


    private DataBaseHelper() {
        this(Application.get(), DB_NAME, null, DB_VERSION);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CACHE
                + " (id INTEGER primary key autoincrement, "
                + COLUMN_NAME_URL_CACHE + " VARCHAR(200), "
                + COLUMN_NAME_CONTENT_CACHE + " TEXT ,"
                + COLUMN_NAME_TIME + " LONG)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FAVOURITE
                + " (id INTEGER primary key autoincrement, "
                + COLUMN_NAME_ID_FAVOURITE + " VARCHAR(200), "
                + COLUMN_NAME_URL_FAVOURITE + " VARCHAR(200), "
                + COLUMN_NAME_TYPE_FAVOURITE + " VARCHAR(200), "
                + COLUMN_NAME_TIME + " LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FAVOURITE
                + " (id INTEGER primary key autoincrement, "
                + COLUMN_NAME_ID_FAVOURITE + " VARCHAR(200), "
                + COLUMN_NAME_URL_FAVOURITE + " VARCHAR(200), "
                + COLUMN_NAME_TYPE_FAVOURITE + " VARCHAR(200), "
                + COLUMN_NAME_TIME + " LONG)");
    }

    public static DataBaseHelper instance() {
        return new DataBaseHelper();
    }
}
