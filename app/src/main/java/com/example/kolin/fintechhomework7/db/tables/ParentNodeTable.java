package com.example.kolin.fintechhomework7.db.tables;

import android.content.ContentValues;

/**
 * Created by kolin on 12.11.2017.
 */

public class ParentNodeTable {

    public static final String TABLE_NAME = "parent_table_node";

    public static final String ID_CHILD = "id_child";
    public static final String ID_PARENT = "id_parent";

    public static final String TABLE_NAME_ID_CHILD = TABLE_NAME + "." + ID_CHILD;
    public static final String TABLE_NAME_ID_PARENT = TABLE_NAME + "." + ID_PARENT;
    public static final String TABLE_ALL = TABLE_NAME + ".*";

    public static String createTable() {
        return "CREATE TABLE "
                + TABLE_NAME + " ("
                + ID_CHILD + " LONG,"
                + ID_PARENT + " LONG, "
                + " FOREIGN KEY (" + ID_CHILD + ") REFERENCES "
                + MainNodeTable.TABLE_NAME + "(" + MainNodeTable.ID + ")"
                + " ON DELETE CASCADE,"
                + " FOREIGN KEY (" + ID_PARENT + ") REFERENCES "
                + MainNodeTable.TABLE_NAME + "(" + MainNodeTable.ID + ")"
                + " ON DELETE CASCADE)";
    }


    /**
     * Метод возвращает запрос, который выводит все связи с родителями и их значения. Так же выводит
     * в итоговый курсор ПОТЕНЦИАЛЬНЫХ родителей. Потенциальные родители фильтруются
     * для предотвращения циклических ссылок друг на друга, путем проверки child связей данной сущности.
     *
     * Пример вывода: поиск родителей для узла 2
     * |          |           |            |
     * | id_child | id_parent | value_node |
     * --------------------------------------
     * |     2    |     1     |    154     |
     * --------------------------------------
     * |     2    |     3     |    666     |
     * --------------------------------------
     * |          |     6     |    123     |     <---- потенциальный узел для установки родственных связей
     *                                                  (проверенный на связь 6->2 = false, если установим связь
     *                                                  цикла не будет)
     *
     * @param id ID of searchable entity
     * @return SQL String
     *
     * EXAMPLE OF QUERY
     *
     * SELECT parent_table_node.*, main_table_node.value_node FROM parent_table_node
     * INNER JOIN main_table_node ON parent_table_node.id_parent = main_table_node.id_node
     * WHERE parent_table_node.id_child = 2
     * UNION ALL
     * SELECT null, main_table_node.id_node, main_table_node.value_node FROM main_table_node
     * WHERE main_table_node.id_node NOT IN
     *  (SELECT parent_table_node.id_parent FROM parent_table_node WHERE parent_table_node.id_child = 2)
     * AND main_table_node.id_node NOT IN
     *  (SELECT parent_table_node.id_child FROM parent_table_node WHERE parent_table_node.id_parent = 2)
     * AND main_table_node.id_node <> 2
     */
    public static String selectParentsReferences(long id){
        return "SELECT " + TABLE_ALL + ", " + MainNodeTable.TABLE_NAME_VALUE
                + " FROM " + TABLE_NAME
                + " INNER JOIN " + MainNodeTable.TABLE_NAME + " ON " + TABLE_NAME_ID_PARENT + " = " + MainNodeTable.TABLE_NAME_ID
                + " WHERE " + TABLE_NAME_ID_CHILD + " = " + id
                + " UNION ALL"
                + " SELECT null, " + MainNodeTable.TABLE_NAME_ID + ", " + MainNodeTable.TABLE_NAME_VALUE
                + " FROM " + MainNodeTable.TABLE_NAME
                + " WHERE " + MainNodeTable.TABLE_NAME_ID + " NOT IN (" + selectParents(id) + ")"
                + " AND " + MainNodeTable.TABLE_NAME_ID + " NOT IN (" + selectChildren(id) + ")"
                + " AND " + MainNodeTable.TABLE_NAME_ID + " <> " + id;
    }

    /**
     * Метод возвращает запрос, который выводит все связи с детьми и их значения. Так же выводит
     * в итоговый курсор ПОТЕНЦИАЛЬНЫХ детей. Потенциальные дети фильтруются
     * для предотвращения циклических ссылок друг на друга, путем проверки parent связей данной сущности.
     *
     * * Пример вывода: поиск детей для узла 2
     * |          |           |            |
     * | id_child | id_parent | value_node |
     * --------------------------------------
     * |     4    |     2     |    154     |
     * --------------------------------------
     * |     5    |     2     |    666     |
     * --------------------------------------
     * |     6    |           |    123     |     <---- потенциальный узел для установки child связей
     *                                                  (проверенный на связь 2->6 = false, если установим связь
     *                                                  цикла не будет)
     *
     *
     *
     * @param id ID of searchable entity
     * @return SQL String
     *
     * EXAMPLE SQL
     *
     * SELECT parent_table_node.*, main_table_node.value_node FROM parent_table_node
     * INNER JOIN main_table_node ON parent_table_node.id_child = main_table_node.id_node
     * WHERE parent_table_node.id_parent = 2
     * UNION ALL
     * SELECT main_table_node.id_node, null, main_table_node.value_node FROM main_table_node
     * WHERE main_table_node.id_node NOT IN
     *  (SELECT parent_table_node.id_parent FROM parent_table_node WHERE parent_table_node.id_child = 2)
     * AND main_table_node.id_node NOT IN
     *  (SELECT parent_table_node.id_child FROM parent_table_node WHERE parent_table_node.id_parent = 2)
     * AND main_table_node.id_node <> 2
     *
     */
    public static String selectChildrenReferences(long id){
        return "SELECT " + TABLE_ALL + ", " + MainNodeTable.TABLE_NAME_VALUE
                + " FROM " + TABLE_NAME
                + " INNER JOIN " + MainNodeTable.TABLE_NAME + " ON " + TABLE_NAME_ID_CHILD + " = " + MainNodeTable.TABLE_NAME_ID
                + " WHERE " + TABLE_NAME_ID_PARENT + " = " + id
                + " UNION ALL"
                + " SELECT " + MainNodeTable.TABLE_NAME_ID + ", null, " + MainNodeTable.TABLE_NAME_VALUE
                + " FROM " + MainNodeTable.TABLE_NAME
                + " WHERE " + MainNodeTable.TABLE_NAME_ID + " NOT IN (" + selectParents(id) + ")"
                + " AND " + MainNodeTable.TABLE_NAME_ID + " NOT IN (" + selectChildren(id) + ")"
                + " AND " + MainNodeTable.TABLE_NAME_ID + " <> " + id;
    }

    public static String selectParents(long id){
        return "SELECT " + ParentNodeTable.TABLE_NAME_ID_PARENT
                + " FROM " + ParentNodeTable.TABLE_NAME
                + " WHERE " + ParentNodeTable.TABLE_NAME_ID_CHILD + " = " + id;
    }

    public static String selectChildren(long id){
        return "SELECT " + ParentNodeTable.TABLE_NAME_ID_CHILD
                + " FROM " + ParentNodeTable.TABLE_NAME
                + " WHERE " + ParentNodeTable.TABLE_NAME_ID_PARENT + " = " + id;
    }

    public static String deleteReference(long idChild, long idParent){
        return "DELETE FROM " + ParentNodeTable.TABLE_NAME
                + " WHERE " + ParentNodeTable.TABLE_NAME_ID_CHILD + " = " + idChild
                + " AND " + ParentNodeTable.TABLE_NAME_ID_PARENT + " = " + idParent;
    }

    public static String dropTable() {
        return "DROP TABLE IF EXIST " + TABLE_NAME;
    }


    public static ContentValues getContentValues(long childId, long parentId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_CHILD, childId);
        contentValues.put(ID_PARENT, parentId);
        return contentValues;
    }
}
