package com.something.patrick.inmobiles;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by patrick on 3/3/2018.
 */

public class ItemsProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.something.patrick.inmobiles.ItemsProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/items";
    static final Uri CONTENT_URI = Uri.parse(URL);

    //Database related
    private SQLiteDatabase db;
    public static final String DATABASE_NAME="inMobiles";
    public static final String TABLE_NAME="items";
    public static final String COL_ID="Id";
    public static final String COL_LINK="link";
    public static final String COL_TITLE="title";
    public static final String COL_DESCRIPTION="description";
    public static final int DATABASE_VERSION = 1;
    public static final String DEFAULT_SORT_COLUMN = "COL_TITLE";


    private static HashMap<String, String> ITEMS_PROJECTION_MAP;

    static final int ITEMS = 1;
    static final int ITEM_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "items", ITEMS);
        uriMatcher.addURI(PROVIDER_NAME, "items/#", ITEM_ID);
    }

    /**
     * Helper class creates and manages the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL
                    ("create table "+TABLE_NAME+
                            "(Id INTEGER PRIMARY KEY,title TEXT,link TEXT,description TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
            onCreate(db);
        }
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ITEMS:
                qb.setProjectionMap(ITEMS_PROJECTION_MAP);
                break;

            case ITEM_ID:
                qb.appendWhere( COL_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on items titles
             */
            sortOrder = DEFAULT_SORT_COLUMN;
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all items records
             */
            case ITEMS:
                return "vnd.android.cursor.dir/vnd.example.students";
            /**
             * Get a particular item
             */
            case ITEM_ID:
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        /**
         * Add a new student record
         */
        long rowID = db.insert(	TABLE_NAME, "", contentValues);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case ITEMS:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;

            case ITEM_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( TABLE_NAME, COL_ID +  " = " + id +  (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
            int count = 0;
            switch (uriMatcher.match(uri)) {
                case ITEMS:
                    count = db.update(TABLE_NAME, contentValues, selection, selectionArgs);
                    break;

                case ITEM_ID:
                    count = db.update(TABLE_NAME, contentValues,
                            COL_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri );
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
    }
}
