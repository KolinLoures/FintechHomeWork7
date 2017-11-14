package com.example.kolin.fintechhomework7.db.tables;

import android.content.ContentValues;

/**
 * Created by kolin on 12.11.2017.
 */

public class MainNodeTable {

    public static final String TABLE_NAME = "main_table_node";

    public static final String ID = "id_node";
    public static final String VALUE = "value_node";

    public static final String TABLE_NAME_ID = TABLE_NAME + "." + ID;
    public static final String TABLE_NAME_VALUE = TABLE_NAME + "." + VALUE;
    public static final String TABLE_ALL = TABLE_NAME + ".*";

    public static final String AS_COUNT_PARENT = "count_parent";
    public static final String AS_COUNT_CHILD = "count_child";

    public static String createTable() {
        return "CREATE TABLE "
                + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VALUE + " INTEGER);";
    }

    public static String selectAllNodes() {
        return "SELECT * FROM " + TABLE_NAME;
    }

    public static String selectNodeAfterID(long afterID) {
        return "SELECT * FROM " + TABLE_NAME
                + " WHERE " + ID + ">" + afterID;
    }


    public static String selectNode(long id) {
        return "SELECT * FROM " + TABLE_NAME
                + " WHERE " + ID + " = " + id;
    }

    public static String countNodesWithId(long id) {
        return "SELECT COUNT(*) FROM "
                + TABLE_NAME + " WHERE "
                + ID + " = " + id;
    }

    /**
     * Считывает все узлы в базе данных, так же подсчитывая все связи из таблицы {@link ParentNodeTable}
     *
     * @return SQL STRING
     */
    @Deprecated
    public static String selectNodeWithCountParentChild() {
        return "SELECT " + MainNodeTable.TABLE_NAME + ".*," +
                " COUNT(" + ParentNodeTable.TABLE_NAME + "." + ParentNodeTable.ID_CHILD + ") AS '" + AS_COUNT_PARENT + "'," +
                " COUNT(" + ParentNodeTable.TABLE_NAME + "." + ParentNodeTable.ID_PARENT + ") AS '" + AS_COUNT_CHILD + "' " +
                " FROM " + MainNodeTable.TABLE_NAME +
                " LEFT JOIN " + ParentNodeTable.TABLE_NAME + " ON ("
                + MainNodeTable.TABLE_NAME + "." + MainNodeTable.ID + "=" + ParentNodeTable.TABLE_NAME + "." + ParentNodeTable.ID_CHILD
                + " AND " + MainNodeTable.TABLE_NAME + "." + MainNodeTable.ID + "=" + ParentNodeTable.TABLE_NAME + "." + ParentNodeTable.ID_PARENT + ")"
                + " GROUP BY " + MainNodeTable.TABLE_NAME + "." + MainNodeTable.ID;
    }

    public static String selectNodeCountParent(){
        return "SELECT " + MainNodeTable.TABLE_ALL + ","
                + " COUNT(" + ParentNodeTable.TABLE_NAME_ID_PARENT + ") AS " + AS_COUNT_PARENT
                + " FROM " + MainNodeTable.TABLE_NAME
                + " LEFT JOIN " + ParentNodeTable.TABLE_NAME
                + " ON " + ParentNodeTable.TABLE_NAME_ID_CHILD + " = " + MainNodeTable.TABLE_NAME_ID
                + " GROUP BY " + MainNodeTable.TABLE_NAME_ID;
    }

    public static String selectNodeCountChilds(){
        return "SELECT " + MainNodeTable.TABLE_ALL + ","
                + " COUNT(" + ParentNodeTable.TABLE_NAME_ID_CHILD + ") AS " + AS_COUNT_CHILD
                + " FROM " + MainNodeTable.TABLE_NAME
                + " LEFT JOIN " + ParentNodeTable.TABLE_NAME
                + " ON " + ParentNodeTable.TABLE_NAME_ID_PARENT + " = " + MainNodeTable.TABLE_NAME_ID
                + " GROUP BY " + MainNodeTable.TABLE_NAME_ID;
    }



    public static String dropTable() {
        return "DROP TABLE IF EXIST " + TABLE_NAME;
    }

    public static String getWhereWithID(long id) {
        return ID + " = " + id;
    }

    public static ContentValues getContentValues(int value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VALUE, value);
        return contentValues;
    }
}
