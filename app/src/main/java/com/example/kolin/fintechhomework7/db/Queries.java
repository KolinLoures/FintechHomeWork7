package com.example.kolin.fintechhomework7.db;

import android.content.Context;
import android.database.Cursor;

import com.example.kolin.fintechhomework7.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by kolin on 12.11.2017.
 */

public class Queries implements NodeDAO {

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

    @Override
    public Completable addNode(int id) {
        return Completable
                .fromCallable(() -> db.insert(MainNodeTable.TABLE_NAME, MainNodeTable.getContentValues(id)));
    }

    @Override
    public Single<List<Node>> getNodes() {
        return null;
    }

    @Override
    public Single<Boolean> checkNodeIsAdded(int id) {
        return Single
                .fromCallable(() -> db.queryNumEntries(MainNodeTable.TABLE_NAME, MainNodeTable.getWhereWithID(id)))
                .map(aLong -> aLong >= 1);
    }

    @Override
    public Single<Node> getParents(int id) {
        return Single
                .fromCallable(() -> db.getCursor(ParentNodeTable.selectParentsReferences(id)))
                .map(cursor -> new Node(id, cursorToNode(cursor, ParentNodeTable.ID_PARENT)));
    }

    @Override
    public Single<Node> getChildren(int id) {
        return Single
                .fromCallable(() -> db.getCursor(ParentNodeTable.selectChildrenReferences(id)))
                .map(cursor -> new Node(id, cursorToNode(cursor, ParentNodeTable.ID_CHILD)));
    }

    private List<Node> cursorToNode(Cursor cursor, String parseColumn) {
        List<Node> list = null;
        if (cursor != null && cursor.moveToFirst())
            try {
                list = new ArrayList<>();

                do {
                    int id = cursor.getInt(cursor.getColumnIndex(parseColumn));
                    list.add(new Node(id,null));
                } while (cursor.moveToNext());


            } finally {
                cursor.close();
            }

        return list;
    }
}
