package sawa.android.reader.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import sawa.android.reader.db.DataBaseHelper;
import sawa.android.reader.main.bean.DouBanFMSongList;

/**
 * Created by hasee on 2017/4/15.
 */
public class StarUtil {

    public static final String STAR_TYPE_SONG_LIST = "songList";

    public static boolean star(DouBanFMSongList songList) {
        SQLiteDatabase writableDatabase = DataBaseHelper.instance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_NAME_TYPE_FAVOURITE, STAR_TYPE_SONG_LIST);
        values.put(DataBaseHelper.COLUMN_NAME_ID_FAVOURITE, songList.getId());
        values.put(DataBaseHelper.COLUMN_NAME_TIME, System.currentTimeMillis());
        long id = writableDatabase.insert(DataBaseHelper.TABLE_NAME_FAVOURITE, null, values);
        writableDatabase.close();
        return id > 0;
    }

    public static boolean isStar(DouBanFMSongList songList) {
        SQLiteDatabase readableDatabase = DataBaseHelper.instance().getReadableDatabase();
        Cursor cursor = readableDatabase.query(DataBaseHelper.TABLE_NAME_FAVOURITE,
                new String[]{DataBaseHelper.COLUMN_NAME_ID_FAVOURITE},
                DataBaseHelper.COLUMN_NAME_ID_FAVOURITE + "=? AND " + DataBaseHelper.COLUMN_NAME_TYPE_FAVOURITE + "=?",
                new String[]{songList.getId() + "", STAR_TYPE_SONG_LIST},
                null, null, null);
        boolean hasResult = cursor.moveToNext();
        cursor.close();
        readableDatabase.close();
        return hasResult;
    }

    public static boolean unStar(DouBanFMSongList songList) {
        SQLiteDatabase writableDatabase = DataBaseHelper.instance().getWritableDatabase();
        int lines = writableDatabase.delete(DataBaseHelper.TABLE_NAME_FAVOURITE,
                DataBaseHelper.COLUMN_NAME_ID_FAVOURITE + "=?",
                new String[]{songList.getId() + ""});
        writableDatabase.close();
        return lines > 0;
    }
}
