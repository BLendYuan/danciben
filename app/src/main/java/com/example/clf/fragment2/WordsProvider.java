package com.example.clf.fragment2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.UserDictionary;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class WordsProvider extends ContentProvider {
    private WordsDBHelper dbHelper;

    private static final int USER_MATCH_CODE = 1;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI("com.example.clf.fragment2.WordsProvider","user",USER_MATCH_CODE);
    }
    @Override
    public boolean onCreate() {
        dbHelper = new WordsDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e("TAG","query()");
        int result = uriMatcher.match(uri);
        if (result == USER_MATCH_CODE) {
            //匹配规则
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(Words.Word.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            return cursor;
        }else{
            //不匹配规则
            throw new IllegalArgumentException("参数错误");
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int result = uriMatcher.match(uri);
        if (result==USER_MATCH_CODE) {
            //匹配规则
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            long id = db.insert(Words.Word.TABLE_NAME,null,values);
            Uri resultUri = Uri.parse("content://com.example.clf.fragment2.WordsProvider/user/"+id);
            //插入数据库成功，数据已变化，通知其他地方
            getContext().getContentResolver().notifyChange(resultUri,null);
            return resultUri;
        }else{
            //不匹配规则
            throw new IllegalArgumentException("参数错误");
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
