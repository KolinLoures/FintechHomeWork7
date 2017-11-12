package com.example.kolin.fintechhomework7.db;

import android.content.ContentValues;

/**
 * Created by kolin on 12.11.2017.
 */

public class ParentNodeTable {

    public static final String TABLE_NAME = "parent_table_node";

    public static final String ID_CHILD = "id_child";
    public static final String ID_PARENT = "id_parent";

    public static String createTable() {
        return "CREATE TABLE "
                + TABLE_NAME + " ("
                + ID_CHILD + " INTEGER,"
                + ID_PARENT + " INTEGER,"
                + " FOREIGN KEY (" + ID_CHILD + ") REFERENCES "
                + MainNodeTable.TABLE_NAME + "(" + MainNodeTable.ID + ")"
                + " ON DELETE CASCADE,"
                + " FOREIGN KEY (" + ID_PARENT + ") REFERENCES "
                + MainNodeTable.TABLE_NAME + "(" + MainNodeTable.ID + ")"
                + " ON DELETE CASCADE;";
    }

    public static String selectParentsReferences(int id){
        return "SELECT " + ID_PARENT
                + " FROM " + TABLE_NAME
                + " WHERE " + ID_CHILD + " = " + id;
    }

    public static String selectChildrenReferences(int id){
        return "SELECT " + ID_CHILD
                + " FROM " + TABLE_NAME
                + " WHERE " + ID_PARENT + " = " + id;
    }

    public static String dropTable() {
        return "DROP TABLE IF EXIST " + TABLE_NAME;
    }


    public static ContentValues getContentValues(int childId, int parentId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_CHILD, childId);
        contentValues.put(ID_PARENT, parentId);
        return contentValues;
    }
}
