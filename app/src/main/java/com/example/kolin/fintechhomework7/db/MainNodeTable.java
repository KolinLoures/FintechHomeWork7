package com.example.kolin.fintechhomework7.db;

import android.content.ContentValues;

/**
 * Created by kolin on 12.11.2017.
 */

public class MainNodeTable {

    public static final String TABLE_NAME = "main_table_node";

    public static final String ID = "id_node";

    public static String createTable() {
        return "CREATE TABLE "
                + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY);";
    }

    public static String selectAllNodes() {
        return "SELECT * FROM " + TABLE_NAME;
    }

    public static String countNodesWithId(int id) {
        return "SELECT COUNT(*) FROM "
                + TABLE_NAME + " WHERE "
                + ID + " = " + id;
    }

    public static String dropTable() {
        return "DROP TABLE IF EXIST " + TABLE_NAME;
    }

    public static String getWhereWithID(int id){
        return ID + " = " + id;
    }

    public static ContentValues getContentValues(int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        return contentValues;
    }
}
