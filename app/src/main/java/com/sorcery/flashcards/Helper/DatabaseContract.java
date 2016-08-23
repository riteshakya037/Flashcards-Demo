package com.sorcery.flashcards.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.File;

/**
 * Created by Ritesh Shakya on 8/17/2016.
 */

public class DatabaseContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CollectionEntry.TABLE_NAME + " (" +
                    CollectionEntry._ID + " INTEGER PRIMARY KEY," +
                    CollectionEntry.COLUMN_NAME_FIREBASE_LINK + TEXT_TYPE + COMMA_SEP +
                    CollectionEntry.COLUMN_NAME_CACHE_LINK + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CollectionEntry.TABLE_NAME;

    public DatabaseContract() {
    }

    static abstract class CollectionEntry implements BaseColumns {
        static final String TABLE_NAME = "mp3_collection";
        static final String COLUMN_NAME_FIREBASE_LINK = "firebaseLink";
        static final String COLUMN_NAME_CACHE_LINK = "cache_link";
    }

    public static class DbHelper extends SQLiteOpenHelper {

        static final int DATABASE_VERSION = 1;
        static final String DATABASE_NAME = "Flashcards.db";
        private String TAG = "DbHelper";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        public void onInsert(String firebaseLink, String cacheLink) {
            SQLiteDatabase db = getWritableDatabase();
            if (checkExist(firebaseLink))
                onDelete(firebaseLink);
            ContentValues values = new ContentValues();
            values.put(CollectionEntry.COLUMN_NAME_FIREBASE_LINK, firebaseLink);
            values.put(CollectionEntry.COLUMN_NAME_CACHE_LINK, cacheLink);

            db.insert(
                    CollectionEntry.TABLE_NAME,
                    null,
                    values);

        }

        public void onDelete(String firebaseLink) {
            SQLiteDatabase db = getWritableDatabase();

            String cacheLink = onSelect(firebaseLink);
            String selection = CollectionEntry.COLUMN_NAME_FIREBASE_LINK + " = ?";
            String[] selectionArgs = {String.valueOf(firebaseLink)};
            db.delete(CollectionEntry.TABLE_NAME, selection, selectionArgs);
            File dir = new File(cacheLink);
            if (dir.exists()) {
                dir.delete();
            }
        }

        public String onSelect(String firebaseLink) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    CollectionEntry.COLUMN_NAME_CACHE_LINK,
            };
            String selection = CollectionEntry.COLUMN_NAME_FIREBASE_LINK + " =?";
            String sortOrder =
                    CollectionEntry.COLUMN_NAME_CACHE_LINK + " DESC";

            String[] selectionArgs = {firebaseLink};

            Cursor res = db.query(
                    true,
                    CollectionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder,
                    null
            );
            if (res.moveToFirst()) {
                String returnValue = res.getString(res.getColumnIndex((CollectionEntry.COLUMN_NAME_CACHE_LINK)));
                res.close();
                return returnValue;
            } else {
                res.close();
                return null;
            }
        }

        public boolean checkExist(String firebaseLink) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    CollectionEntry.COLUMN_NAME_CACHE_LINK,
            };
            String selection = CollectionEntry.COLUMN_NAME_FIREBASE_LINK + " =?";
            String sortOrder =
                    CollectionEntry.COLUMN_NAME_CACHE_LINK + " DESC";

            String[] selectionArgs = {firebaseLink};

            Cursor res = db.query(
                    true,
                    CollectionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder,
                    null
            );
            boolean returnValue = res.moveToFirst();
            res.close();
            return returnValue;
        }

    }

}
