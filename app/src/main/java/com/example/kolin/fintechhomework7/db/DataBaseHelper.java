package com.example.kolin.fintechhomework7.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

/**
 * Created by kolin on 12.11.2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "com.example.kolin.fintechhomework7.data_base_home_work";
    private static final int DB_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
        sqLiteDatabase.execSQL(MainNodeTable.createTable());
        sqLiteDatabase.execSQL(ParentNodeTable.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i != i1){
            sqLiteDatabase.execSQL(MainNodeTable.dropTable());
            sqLiteDatabase.execSQL(ParentNodeTable.dropTable());
            this.onCreate(sqLiteDatabase);
        }
    }

    public Cursor getCursor(@NonNull String sql){
        return this.getReadableDatabase().rawQuery(sql, null);
    }

    public long insert(@NonNull String tableName, @NonNull ContentValues contentValues){
       return this.getWritableDatabase().insert(tableName, null, contentValues);
    }

    public void executeSql(@NonNull String sql){
        this.getWritableDatabase().execSQL(sql);
    }

    public long queryNumEntries(@NonNull String tableName, @NonNull String selection){
        return DatabaseUtils.queryNumEntries(this.getReadableDatabase(), tableName, selection);
    }

}
