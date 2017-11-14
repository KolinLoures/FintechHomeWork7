package com.example.kolin.fintechhomework7.db.query;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.kolin.fintechhomework7.db.DataBaseHelper;
import com.example.kolin.fintechhomework7.db.tables.MainNodeTable;
import com.example.kolin.fintechhomework7.db.tables.ParentNodeTable;
import com.example.kolin.fintechhomework7.model.Node;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kolin on 12.11.2017.
 */

public class Queries extends BaseQueries implements NodeDAO {

    public static final String TAG = Queries.class.getSimpleName();

    private static Queries instance = null;

    private DataBaseHelper db;

    private Queries(Context context) {
        this.db = new DataBaseHelper(context);
    }

    public static Queries getInstance() {
        if (instance == null)
            throw new IllegalStateException("Instance is null! Use initWithContext() method to initialize instance!");

        return instance;
    }

    public static void initWithContext(Context context) {
        if (instance == null)
            instance = new Queries(context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Single<Node> addNode(long afterId, int value) {
        return super.addSchedulers(Single
                .fromCallable(() -> db.insert(MainNodeTable.TABLE_NAME, MainNodeTable.getContentValues(value)))
                .map(aLong -> aLong != -1 ? db.getCursor(MainNodeTable.selectNodeAfterID(afterId)) : null)
                .map(cursor -> cursorToNode(cursor, MainNodeTable.ID, MainNodeTable.VALUE).get(0)))
                .doOnError(throwable -> Log.e(TAG, "addNode: Error to add Node and then to get it!", (Throwable) throwable));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Single<List<Node>> getNodes() {

        return super.addSchedulers(Single.zip(
                Single.fromCallable(() -> db.getCursor(MainNodeTable.selectNodeCountParent())),
                Single.fromCallable(() -> db.getCursor(MainNodeTable.selectNodeCountChilds())),
                this::cursorToNodeWithCountReferences
        ));

    }

    @SuppressWarnings("unchecked")
    @Override
    public Single<Node> getParentsReferences(Node node) {
        return super.addSchedulers(Single
                .fromCallable(() -> db.getCursor(ParentNodeTable.selectParentsReferences(node.getId())))
                .map(cursor -> new Node(node.getId(), node.getValue(), cursorToParentReferences(cursor))));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Single<Node> getChildrenReferences(Node node) {
        return super.addSchedulers(Single
                .fromCallable(() -> db.getCursor(ParentNodeTable.selectChildrenReferences(node.getId())))
                .map(cursor -> new Node(node.getId(), node.getValue(), cursorToChildReferences(cursor))));
    }

    @Override
    public Completable addReference(long idChild, long idParent) {
        return Completable
                .fromAction(()-> db.insert(ParentNodeTable.TABLE_NAME, ParentNodeTable.getContentValues(idChild, idParent)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable removeReference(long idChild, long idParent) {
        return Completable
                .fromAction(()-> db.executeSql(ParentNodeTable.deleteReference(idChild, idParent)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    private List<Node> cursorToNodeWithCountReferences(Cursor cursor1, Cursor cursor2) {
        List<Node> list = new ArrayList<>();

        if (cursor1 != null && cursor1.moveToFirst())
            try {
                cursor2.moveToFirst();
                do {

                    long id = cursor1.getInt(cursor1.getColumnIndex(MainNodeTable.ID));
                    int value = cursor1.getInt(cursor1.getColumnIndex(MainNodeTable.VALUE));
                    int haveParent = cursor1.getInt(cursor1.getColumnIndex(MainNodeTable.AS_COUNT_PARENT)) >= 1 ? 1 : 0;
                    int haveChild = cursor2.getInt(cursor2.getColumnIndex(MainNodeTable.AS_COUNT_CHILD)) >= 1 ? 1 : 0;

                    ArrayList<Node> temp = new ArrayList<>(2);
                    temp.add(new Node(-666, haveParent, null));
                    temp.add(new Node(-666, haveChild, null));

                    list.add(new Node(id, value, temp));

                    cursor2.moveToNext();

                } while (cursor1.moveToNext());

            } finally {
                cursor1.close();
                cursor2.close();
            }

        return list;
    }


    private List<Node> cursorToNode(Cursor cursor, String idColumn, String valueColumn) {
        List<Node> list = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
            try {

                do {
                    long id = cursor.getLong(cursor.getColumnIndex(idColumn));
                    int value = cursor.getInt(cursor.getColumnIndex(valueColumn));
                    list.add(new Node(id, value, null));
                } while (cursor.moveToNext());

            } finally {
                cursor.close();
            }

        return list;
    }

    private List<Node> cursorToParentReferences(Cursor cursor) {
        List<Node> list = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
            try {

                do {
                    Long id1 = null;
                    if (!cursor.isNull(cursor.getColumnIndex(ParentNodeTable.ID_CHILD)))
                        id1 = cursor.getLong(cursor.getColumnIndex(ParentNodeTable.ID_CHILD));

                    long id2 = cursor.getLong(cursor.getColumnIndex(ParentNodeTable.ID_PARENT));
                    int value = cursor.getInt(cursor.getColumnIndex(MainNodeTable.VALUE));

                    Node e = null;
                    if (id1 != null)
                        //For current Node
                        e = new Node(id2, value, new ArrayList<>());
                    else
                        //For potentional Node
                        e = new Node(id2, value, null);

                    list.add(e);
                } while (cursor.moveToNext());

            } finally {
                cursor.close();
            }

        return list;
    }

    //todo rewrite this method, two same method in this class (cursorToParentReferences)
    private List<Node> cursorToChildReferences(Cursor cursor) {
        List<Node> list = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
            try {

                do {
                    long id2 = cursor.getLong(cursor.getColumnIndex(ParentNodeTable.ID_CHILD));

                    Long id1 = null;
                    if (!cursor.isNull(cursor.getColumnIndex(ParentNodeTable.ID_PARENT))) {
                        id1 = cursor.getLong(cursor.getColumnIndex(ParentNodeTable.ID_PARENT));
                    }

                    int value = cursor.getInt(cursor.getColumnIndex(MainNodeTable.VALUE));

                    Node e = null;
                    if (id1 != null)
                        //For current Node
                        e = new Node(id2, value, new ArrayList<>());
                    else
                        //For potentional Node
                        e = new Node(id2, value, null);

                    list.add(e);
                } while (cursor.moveToNext());

            } finally {
                cursor.close();
            }

        return list;
    }


}
