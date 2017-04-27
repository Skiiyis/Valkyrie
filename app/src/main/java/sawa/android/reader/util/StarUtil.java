package sawa.android.reader.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import sawa.android.reader.db.DataBaseHelper;
import sawa.android.reader.douban.bean.DouBanFMSongListDetail;
import sawa.android.reader.main.bean.DouBanFMSongList;

/**
 * Created by hasee on 2017/4/15.
 */
public class StarUtil {

    private static final String STAR_TYPE_SONG_LIST = "songList";
    private static final String STAR_TYPE_SONG = "song";

    private static boolean star(String id, String starType) {
        SQLiteDatabase writableDatabase = DataBaseHelper.instance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_NAME_TYPE_FAVOURITE, starType);
        values.put(DataBaseHelper.COLUMN_NAME_ID_FAVOURITE, id);
        values.put(DataBaseHelper.COLUMN_NAME_TIME, System.currentTimeMillis());
        long resultId = writableDatabase.insert(DataBaseHelper.TABLE_NAME_FAVOURITE, null, values);
        writableDatabase.close();
        return resultId > 0;
    }

    private static boolean isStar(String id, String starType) {
        SQLiteDatabase readableDatabase = DataBaseHelper.instance().getReadableDatabase();
        Cursor cursor = readableDatabase.query(DataBaseHelper.TABLE_NAME_FAVOURITE,
                new String[]{DataBaseHelper.COLUMN_NAME_ID_FAVOURITE},
                DataBaseHelper.COLUMN_NAME_ID_FAVOURITE + "=? AND " + DataBaseHelper.COLUMN_NAME_TYPE_FAVOURITE + "=?",
                new String[]{id, starType},
                null, null, null);
        boolean hasResult = cursor.moveToNext();
        cursor.close();
        readableDatabase.close();
        return hasResult;
    }

    private static boolean unStar(String id, String starType) {
        SQLiteDatabase writableDatabase = DataBaseHelper.instance().getWritableDatabase();
        int lines = writableDatabase.delete(DataBaseHelper.TABLE_NAME_FAVOURITE,
                DataBaseHelper.COLUMN_NAME_ID_FAVOURITE + "=? AND " + DataBaseHelper.COLUMN_NAME_TYPE_FAVOURITE + "=?",
                new String[]{id, starType});
        writableDatabase.close();
        return lines > 0;
    }

    public static boolean star(DouBanFMSongList songList) {
        return star(songList.getId() + "", STAR_TYPE_SONG_LIST);
    }

    public static boolean isStar(DouBanFMSongList songList) {
        return isStar(songList.getId() + "", STAR_TYPE_SONG_LIST);
    }

    public static boolean unStar(DouBanFMSongList songList) {
        return unStar(songList.getId() + "", STAR_TYPE_SONG_LIST);
    }

    public static boolean star(DouBanFMSongListDetail.Song song) {
        return star(song.getSid() + "", STAR_TYPE_SONG);
    }

    public static boolean isStar(DouBanFMSongListDetail.Song song) {
        return isStar(song.getSid() + "", STAR_TYPE_SONG);
    }

    public static boolean unStar(DouBanFMSongListDetail.Song song) {
        return unStar(song.getSid() + "", STAR_TYPE_SONG);
    }
}
