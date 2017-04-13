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

    public static final String DB_NAME = "cache"; //数据库名称
    public static final String TABLE_NAME = "cache"; //数据库名称
    public static final String COLUMN_NAME_KEY = "url";
    public static final String COLUMN_NAME_VALUE = "content";
    public static final String COLUMN_NAME_TIME = "time";

    private static final int DB_VERSION = 1; //数据库版本

    private static DataBaseHelper dataBaseHelper;

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
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER primary key autoincrement, " + COLUMN_NAME_KEY + " VARCHAR(200), " + COLUMN_NAME_VALUE + " TEXT ," + COLUMN_NAME_TIME + " LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static DataBaseHelper instance() {
        synchronized (DataBaseHelper.class) {
            if (dataBaseHelper == null) {
                synchronized (DataBaseHelper.class) {
                    dataBaseHelper = new DataBaseHelper();
                    return dataBaseHelper;
                }
            } else {
                return dataBaseHelper;
            }
        }
    }
}
